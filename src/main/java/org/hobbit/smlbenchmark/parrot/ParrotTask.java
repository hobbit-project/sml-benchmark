package org.hobbit.smlbenchmark.parrot;

import org.hobbit.smlbenchmark.parrot.io.InMemoryCommunication;
import org.hobbit.smlbenchmark.common.TaskBasedBenchmarkController;
import org.hobbit.smlbenchmark.common.TerminationMessageProtocol;
import org.hobbit.smlbenchmark.common.benchmark.CorrectnessBenchmarkProtocol;
import org.hobbit.smlbenchmark.common.benchmark.DataCheckerImpl;
import org.hobbit.smlbenchmark.parrot.io.Repeater;
import org.hobbit.smlbenchmark.common.benchmark.BenchmarkTask;
import org.hobbit.sdk.JenaKeyValue;
import org.hobbit.sdk.KeyValue;
import org.hobbit.sdk.io.Communication;
import org.hobbit.sdk.io.RabbitMqCommunication;
import org.hobbit.smlbenchmark.common.benchmark.ComparablesMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Roman Katerinenko
 */
public class ParrotTask extends CorrectnessBenchmarkProtocol implements BenchmarkTask {
    private static final Logger logger = LoggerFactory.getLogger(ParrotTask.class);

    public static final int MESSAGE_COUNT = 17;
    public static final String MESSAGE_COUNT_INPUT_NAME = "http://www.example.org/exampleBenchmark/messageCount";
    public static final String CHECKED_MESSAGES_OUTPUT_NAME = "http://agtinternational.com/checked_messages";
    public static final String CORRECTNESS_KPI_OUTPUT_NAME = "http://www.example.org/exampleBenchmark/correctness";

    private int messageCount;
    private StringDataGenerator generator;
    private DataCheckerImpl<PlainTextAnomaly> dataChecker;
    private Communication dataCheckerInput;
    private boolean exceptionWasThrown = false;

    public ParrotTask(KeyValue inputParameters) throws Exception {
        super(new RabbitMqCommunication.Builder());
        messageCount = inputParameters.getIntValueFor(MESSAGE_COUNT_INPUT_NAME);
        dataChecker = new DataCheckerImpl<>(messageCount, new StrictStringMatcher());
    }

    @Override
    protected void initProtocol() throws Exception {
        dataCheckerInput = initDataCheckerInput();
        generator = new StringDataGenerator(messageCount, newRepeater());
    }

    private Communication initDataCheckerInput() throws Exception {
        return new InMemoryCommunication.Builder()
                .charset(TaskBasedBenchmarkController.CHARSET)
                .name("DataCheckerInput")
                .consumer(bytes -> dataChecker.addGold(new PlainTextAnomaly(bytes)))
                .build();
    }

    private Communication newRepeater() throws Exception {
        Repeater.Builder repeaterBuilder = new Repeater.Builder();
        return repeaterBuilder
                .outputCommunication1(new TerminationMessageProtocol.OutputSender())
                .outputCommunication2(dataCheckerInput)
                .charset(TaskBasedBenchmarkController.CHARSET)
                .name("Repeater")
                .build();
    }

    @Override
    public boolean isSuccessful() {
        return dataChecker.isCorrect() && !exceptionWasThrown;
    }

    @Override
    public JenaKeyValue getResult() {
        JenaKeyValue jenaKeyValue = new JenaKeyValue();
        jenaKeyValue.setValue(CORRECTNESS_KPI_OUTPUT_NAME, composeResultMessage());
        jenaKeyValue.setValue(CHECKED_MESSAGES_OUTPUT_NAME, dataChecker.getCheckedCount());
        return jenaKeyValue;
    }

    private String composeResultMessage() {
        return String.format("Executed on %d messages, checked %d messages. Result: %s\n", messageCount,
                dataChecker.getCheckedCount(),
                isSuccessful());
    }

    @Override
    public void run() {
        try {
            execute();
        } catch (Exception e) {
            exceptionWasThrown = true;
            logger.error("Exception", e);
        }
    }

    @Override
    protected void startDataGeneration() throws Exception {
        generator.run();
    }

    @Override
    protected Communication.Consumer getInputConsumer() {
        return bytes -> dataChecker.addActual(new PlainTextAnomaly(bytes));
    }

    private static class StrictStringMatcher implements ComparablesMatcher<PlainTextAnomaly> {
        @Override
        public int compare(PlainTextAnomaly o1, PlainTextAnomaly o2) {
            String anomaly1 = o1.getAnomaly();
            String anomaly2 = o2.getAnomaly();
            return anomaly1.compareTo(anomaly2);
        }
    }
}