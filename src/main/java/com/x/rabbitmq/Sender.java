package com.x.rabbitmq;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.*;

public class Sender {

    private static final String HELLO_QUEUE = "hello";

    public static void main(String[] args) {


        ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
        service.scheduleAtFixedRate(() -> publish(LocalDateTime.now().toString()), 3, 1, TimeUnit.SECONDS);
    }

    private static void publish(String msg) {
        ConnectionFactory factory = new ConnectionFactory();
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel();) {

            factory.setHost("localhost");
            channel.queueDeclare(HELLO_QUEUE, true, false, false, null);
            channel.basicPublish("", HELLO_QUEUE, MessageProperties.PERSISTENT_TEXT_PLAIN, msg.getBytes("UTF-8"));
            System.out.println(HELLO_QUEUE + " published: " + msg);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
}
