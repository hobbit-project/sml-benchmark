package com.agtinternational.hobbit.correctness;

import com.agtinternational.hobbit.Utils;
import com.agtinternational.hobbit.benchmark.AnomalyDetectionBenchmarkController;
import com.agtinternational.hobbit.benchmark.Communication;
import com.agtinternational.hobbit.benchmark.DataChecker;
import com.agtinternational.hobbit.benchmark.DataGenerator;
import com.agtinternational.hobbit.benchmark.TerminationMessageProtocol;
import com.agtinternational.hobbit.io.InMemoryCommunication;
import com.agtinternational.hobbit.io.NetworkCommunication;
import com.agtinternational.hobbit.io.Repeater;
import org.hobbit.core.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;

/**
 * @author Roman Katerinenko
 */
public class CorrectnessCheckProtocol extends TerminationMessageProtocol {
    private static final Logger logger = LoggerFactory.getLogger(CorrectnessCheckProtocol.class);

    private final DataGenerator.Builder dataGeneratorBuilder;
    private final DataChecker dataChecker;

    private DataGenerator dataGenerator;

    public CorrectnessCheckProtocol(DataGenerator.Builder dataGeneratorBuilder, DataChecker dataChecker, NetworkCommunication.Builder communicationBuilder) {
        super(communicationBuilder, AnomalyDetectionBenchmarkController.TERMINATION_MESSAGE);
        this.dataGeneratorBuilder = dataGeneratorBuilder;
        this.dataChecker = dataChecker;
    }

    @Override
    public void executeProtocol() {
        try {
            logger.debug("Initializing...");
            init();
            logger.debug("Sending data...");
            logger.debug("Running data generator...");
            dataGenerator.run();
            logger.debug("Sending termination data...");
            sendTerminationMessage();
            logger.debug("Waiting for termination message...");
            waitForTerminationMessage();
            logger.debug("Got termination message...");
        } catch (Exception e) {
            logger.error("Exception", e);
            setErrorMessage("Errors while executing the task");
            setSuccessful(false);
        }
        logger.debug("Finished.");
    }

    private void init() throws Exception {
        Communication repeater2Checker = createInMemoryCommunication();
        Repeater.Builder repeaterBuilder = new Repeater.Builder();
        repeaterBuilder.outputCommunication1(new OutputSender())
                .outputCommunication2(repeater2Checker)
                .charset(AnomalyDetectionBenchmarkController.CHARSET)
                .name("Repeater");
        Repeater repeater = repeaterBuilder.build();
        dataGenerator = dataGeneratorBuilder
                .outputCommunication(repeater)
                .build();
    }

    private Communication createInMemoryCommunication() throws Exception {
        return new InMemoryCommunication.Builder()
                .charset(AnomalyDetectionBenchmarkController.CHARSET)
                .name("Repeater2Checker")
                .consumer(dataChecker.getGoldStandardConsumer())
                .build();
    }

    @Override
    protected Communication.Consumer getInputConsumer() {
        return bytes -> dataChecker.getInputConsumer().handleDelivery(bytes);
    }

    @Override
    protected String getInputHost() {
        return System.getenv().get(Constants.RABBIT_MQ_HOST_NAME_KEY);
    }

    @Override
    protected Charset getCharset() {
        return AnomalyDetectionBenchmarkController.CHARSET;
    }

    @Override
    protected int getInputPrefetchCount() {
        return 1;
    }

    @Override
    protected String getInputCommunicationName() {
        return Utils.toPlatformQueueName(Constants.SYSTEM_2_EVAL_STORAGE_QUEUE_NAME);
    }

    @Override
    protected String getOutputCommunicationName() {
        return Utils.toPlatformQueueName(Constants.DATA_GEN_2_SYSTEM_QUEUE_NAME);
    }

    @Override
    protected Communication.Consumer getOutputConsumer() {
        return null;
    }

    @Override
    protected String getOutputHost() {
        return getInputHost();
    }

    @Override
    protected int getOutputPrefetchCount() {
        return 1;
    }

    @Override
    public boolean isSuccessful() {
        return dataChecker.isCorrect();
    }

    private final class OutputSender implements Communication {
        @Override
        public void close() throws Exception {
        }

        @Override
        public String getName() {
            return "OutputSender";
        }

        @Override
        public Charset getCharset() {
            return AnomalyDetectionBenchmarkController.CHARSET;
        }

        @Override
        public Consumer getConsumer() {
            return null;
        }

        @Override
        public void send(byte[] bytes) throws Exception {
            sendBytes(bytes);
        }

        @Override
        public void send(String string) throws Exception {
            sendString(string);
        }
    }
}