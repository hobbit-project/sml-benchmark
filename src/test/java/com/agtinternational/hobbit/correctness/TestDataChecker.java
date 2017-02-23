package com.agtinternational.hobbit.correctness;

import com.agtinternational.hobbit.benchmark.AnomalyDetectionBenchmarkController;
import com.agtinternational.hobbit.benchmark.Communication;
import com.agtinternational.hobbit.benchmark.DataChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.concurrent.CountDownLatch;

/**
 * @author Roman Katerinenko
 */
class TestDataChecker implements DataChecker {
    private static final Logger logger = LoggerFactory.getLogger(TestDataChecker.class);

    private final Communication.Consumer inputConsumer;
    private final Communication.Consumer goldStandardConsumer;

    private boolean correct = true;

    private final LinkedList<String> goldStandardStrings = new LinkedList<>();
    private final LinkedList<String> actualStrings = new LinkedList<>();
    private final CountDownLatch inputClosedBarrier = new CountDownLatch(1);

    TestDataChecker() {
        inputConsumer = new Communication.Consumer() {
            @Override
            public void handleDelivery(byte[] bytes) {
                String actual = new String(bytes, AnomalyDetectionBenchmarkController.CHARSET);
                addActualString(actual);
                checkSameStrings();
            }

            @Override
            public void onDelete() {
                finalCheck();
                inputClosedBarrier.countDown();
            }
        };
        goldStandardConsumer = new Communication.Consumer() {
            @Override
            public void handleDelivery(byte[] bytes) {
                String string = new String(bytes, AnomalyDetectionBenchmarkController.CHARSET);
                addGoldStandardString(string);
                checkSameStrings();
            }

            @Override
            public void onDelete() {
            }
        };

    }

    private synchronized void addActualString(String actual) {
        actualStrings.addLast(actual);
    }

    private synchronized void addGoldStandardString(String goldStandard) {
        goldStandardStrings.add(goldStandard);
    }

    @Override
    public Communication.Consumer getInputConsumer() {
        return inputConsumer;
    }

    @Override
    public Communication.Consumer getGoldStandardConsumer() {
        return goldStandardConsumer;
    }

    private void setCorrect(boolean correct) {
        this.correct = correct;
    }

    @Override
    public boolean isCorrect() {
        return correct;
    }

    @Override
    public void run() {
        try {
            inputClosedBarrier.await();
        } catch (Exception e) {
            logger.error("Exception", e);
        }
    }

    private synchronized void finalCheck() {
        boolean allEmpty = goldStandardStrings.isEmpty() && actualStrings.isEmpty();
        setCorrect(allEmpty);
    }

    private synchronized void checkSameStrings() {
        if (isCorrect()) {
            if (!goldStandardStrings.isEmpty() && !actualStrings.isEmpty()) {
                String goldStandardString = goldStandardStrings.getFirst();
                String actualString = actualStrings.getFirst();
                boolean match = goldStandardString.contentEquals(actualString);
                if (match) {
                    goldStandardStrings.removeFirst();
                    actualStrings.removeFirst();
                }
                setCorrect(match);
            }
        }
    }
}