package com.agtinternational.hobbit.correctness;

import com.agtinternational.hobbit.Utils;
import com.agtinternational.hobbit.benchmark.AnomalyDetectionBenchmarkController;
import com.agtinternational.hobbit.benchmark.Communication;
import com.agtinternational.hobbit.benchmark.DataChecker;
import com.agtinternational.hobbit.benchmark.DataGenerator;
import com.agtinternational.hobbit.io.InMemoryCommunication;
import com.agtinternational.hobbit.io.Repeater;
import org.hobbit.core.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Roman Katerinenko
 */
class CorrectnessCheckProtocol extends BenchmarkSideProtocol {
    private static final Logger logger = LoggerFactory.getLogger(CorrectnessCheckProtocol.class);

    private final DataGenerator.Builder dataGeneratorBuilder;

    private DataGenerator dataGenerator;
    private DataChecker dataChecker;

    CorrectnessCheckProtocol(DataGenerator.Builder dataGeneratorBuilder, DataChecker dataChecker, Communication.Builder communicationBuilder) {
        super(communicationBuilder);
        this.dataGeneratorBuilder = dataGeneratorBuilder;
        this.dataChecker = dataChecker;
        setWaitingNanos(1000);
    }

    @Override
    public boolean init() {
        return true;
    }

    @Override
    protected void initCommunication() throws Exception {
        setInputCommunication(createInputCommunication());
        setOutputCommunication(createOutputCommunication());
        Communication dataGen2Checker = createInMemoryCommunication();
        Repeater repeater = new Repeater(getOutputCommunication(), dataGen2Checker);
        dataGenerator = dataGeneratorBuilder
                .outputCommunication(repeater)
                .build();
    }

    private Communication createInputCommunication() {
        String inputQueueName = Utils.toPlatformQueueName(Constants.SYSTEM_2_EVAL_STORAGE_QUEUE_NAME);
        String host = System.getenv().get(Constants.RABBIT_MQ_HOST_NAME_KEY);
        try {
            return getCommunicationBuilder()
                    .name(inputQueueName)
                    .host(host)
                    .charset(AnomalyDetectionBenchmarkController.CHARSET)
                    .prefetchCount(1)
                    .consumer(new Communication.Consumer() {
                        @Override
                        public void handleDelivery(byte[] bytes) {
                            CorrectnessCheckProtocol.this.handleInputDelivery(bytes);
                        }

                        @Override
                        public void onDelete() {
                            CorrectnessCheckProtocol.this.handleInputCommunicationDelete();
                        }
                    })
                    .build();
        } catch (Exception e) {
            logger.error("Exception", e);
        }
        return null;
    }

    private Communication createOutputCommunication() {
        String outputQueueName = Utils.toPlatformQueueName(Constants.DATA_GEN_2_SYSTEM_QUEUE_NAME);
        String host = System.getenv().get(Constants.RABBIT_MQ_HOST_NAME_KEY);
        try {
            return getCommunicationBuilder()
                    .name(outputQueueName)
                    .host(host)
                    .charset(AnomalyDetectionBenchmarkController.CHARSET)
                    .prefetchCount(1)
                    .build();
        } catch (Exception e) {
            logger.error("Exception", e);
        }
        return null;
    }

    private Communication createInMemoryCommunication() {
        Communication.Builder builder = new Communication.Builder()
                .name("DataGen2Checker")
                .consumer(dataChecker.getGoldStandardConsumer());
        return new InMemoryCommunication(builder);
    }

    @Override
    protected void sendData() throws Exception {
        logger.debug("Sending data");
        dataGenerator.run();
    }

    private void handleInputDelivery(byte[] bytes) {
        dataChecker.getInputConsumer().handleDelivery(bytes);
    }

    private void handleInputCommunicationDelete() {
        dataChecker.getInputConsumer().onDelete();
        getInputCommunicationClosedBarrier().countDown();
    }

    @Override
    public boolean isSuccessful() {
        return dataChecker.isCorrect();
    }
}