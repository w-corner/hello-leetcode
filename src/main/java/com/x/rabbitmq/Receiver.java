package com.x.rabbitmq;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Receiver {

    private static final String HELLO_QUEUE = "hello";

    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        try {
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            factory.setHost("localhost");
            channel.queueDeclare(HELLO_QUEUE, true, false, false, null);
            System.out.println("Waiting for messages...");

            channel.basicQos(1);

            final Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String message = new String(body, StandardCharsets.UTF_8);

                    System.out.println(" [x] Received '" + message + "'");
                    try {
                        TimeUnit.SECONDS.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        System.out.println(" [x] Done");
                        channel.basicAck(envelope.getDeliveryTag(), false);
                    }
                }
            };
            channel.basicConsume(HELLO_QUEUE, false, consumer);
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }

}
