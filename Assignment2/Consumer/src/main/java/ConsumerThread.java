import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ConsumerThread implements Runnable{
    private final String queueName;
    private final Connection conn;
    private final Gson gson = new Gson();
    public ConsumerThread(String queueName, Connection conn){
        this.queueName = queueName;
        this.conn = conn;
    }
    @Override
    public void run() {
        try {
            Channel channel = conn.createChannel();
            channel.queueDeclare(queueName, false, false, false, null);
            channel.basicQos(10);
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String msg = new String(delivery.getBody(), StandardCharsets.UTF_8);
                Consumer.logger.info(msg);
                JsonObject json = gson.fromJson(msg, JsonObject.class);
                Integer skierID = json.get("skierID").getAsInt();
                if (Consumer.records.containsKey(skierID)) {
                    Consumer.records.get(skierID).add(json);
                } else {
                    List<JsonObject> newRecord = Collections.synchronizedList(new ArrayList<>());
                    newRecord.add(json);
                    Consumer.records.put(skierID, newRecord);
                }
                Consumer.logger.info("Thread" + Thread.currentThread().getId() + " received: " + json);
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            };
            channel.basicConsume(this.queueName, false ,deliverCallback, consumerTag -> {});
        } catch (IOException e) {
            Consumer.logger.error(Arrays.toString(e.getStackTrace()));
        }
    }
}