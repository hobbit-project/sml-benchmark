package com.agtinternational.hobbit.benchmark;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.TimeoutException;

/**
 * @author Roman Katerinenko
 */
public class Communication {
    private final Consumer consumer;
    private final String name;
    private final String host;
    private final Charset charset;
    private final int prefetchCount;

    protected Communication(Builder builder) {
        consumer = builder.consumer;
        name = builder.name;
        host = builder.host;
        charset = builder.charset;
        prefetchCount = builder.prefetchCount;
    }

    public String getName() {
        return name;
    }

    public String getHost() {
        return host;
    }

    public Charset getCharset() {
        return charset;
    }

    public int getPrefetchCount() {
        return prefetchCount;
    }

    protected Consumer getConsumer() {
        return consumer;
    }

    public void delete() throws Exception {
    }

    public void close() throws Exception {
    }

    public void send(byte[] bytes) throws Exception {
    }

    public void send(String string) throws Exception {
    }

    public long getMessageCount() {
        throw new IllegalStateException("Must be implemented");
    }

    public interface Consumer {
        void handleDelivery(byte[] bytes);

        void onDelete();
    }

    public static class Builder {
        private Consumer consumer;
        private String name;
        private String host;
        private Charset charset = Charset.forName("UTF-8");
        private int prefetchCount = 1;

        public Builder consumer(Consumer consumer) {
            this.consumer = consumer;
            return this;
        }

        protected Consumer getConsumer() {
            return consumer;
        }

        protected String getName() {
            return name;
        }

        protected String getHost() {
            return host;
        }

        protected Charset getCharset() {
            return charset;
        }

        protected int getPrefetchCount() {
            return prefetchCount;
        }

        public Builder prefetchCount(int prefetchCount) {
            this.prefetchCount = prefetchCount;
            return this;
        }

        public Builder charset(Charset charset) {
            this.charset = charset;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder host(String host) {
            this.host = host;
            return this;
        }

        public Communication build() throws IOException, TimeoutException {
            if (consumer == null) {
                consumer = new Consumer() {
                    @Override
                    public void handleDelivery(byte[] bytes) {
                        throw new IllegalStateException("This is a default consumer which must not be called");
                    }

                    @Override
                    public void onDelete() {
                        throw new IllegalStateException("This is a default consumer which must not be called");
                    }
                };
            }
            return new Communication(this);
        }
    }
}