import com.rabbitmq.client.*;

public class Consumer {
    public static String EXCHANGE_NAME="test_exchange";
    public static String QUEUE_NAME="test_queue";
    public static String ROUTING_KEY="test_key";

    public static void main(String[] args) {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");

            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
            /*
             * exchange name
             * type (direct, fanout, topic, headers)
             */


            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            /*
             * queue name,
             * durable,
             * exclusive(Only this connection can use the queue, queue is deleted when connection closes),
             * autoDelete(queue deletes when last consumer disconnects),
             * arguments*/

            channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY);
            /*
             * queue
             * exchange
             * routing key
             */

            // If we set basicQos to 1 then: Consumer gets 1 message → processes → ACK → gets next
            channel.basicQos(1);

            System.out.println("Waiting for messages...");

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody());
                System.out.println(" Received: " + message);

                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            };

            channel.basicConsume(QUEUE_NAME, false, deliverCallback, consumerTag -> {
            });
            /*
            * queue name
            * autoAck
            * deliverCallback(What to do when message arrives)
            * consumerTag->{}(It is called if consumer is cancelled)*/
        }catch (Exception e){
            System.out.println("Exception: "+e.getMessage());
        }
    }
}
