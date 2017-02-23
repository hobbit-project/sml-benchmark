package com.agtinternational.hobbit.io;

import com.agtinternational.hobbit.benchmark.Communication;
import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.Charset;
import java.util.concurrent.CountDownLatch;

/**
 * @author Roman Katerinenko
 */
public class CommunicationTest {
    private String actualMessage;

    @Test
    public void checkDataTransmission() throws Exception{
        String queueName = "dummyQueue";
        String host = "127.0.0.1";
        Charset charset = Charset.forName("UTF-8");
        CountDownLatch messageDeliveredLatch = new CountDownLatch(1);
        Communication communication = new RabbitMqCommunication.Builder()
                .name(queueName)
                .host(host)
                .charset(charset)
                .consumer(new Communication.Consumer() {
                    @Override
                    public void handleDelivery(byte[] bytes) {
                        actualMessage = new String(bytes, charset);
                        messageDeliveredLatch.countDown();
                    }

                    @Override
                    public void onDelete() {
                    }
                })
                .build();
        String expectedMessage = "testMessage";
        communication.send(expectedMessage);
        messageDeliveredLatch.await();
        communication.close();
        Assert.assertEquals(expectedMessage, actualMessage);
    }
}