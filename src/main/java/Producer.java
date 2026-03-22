import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Producer {
    public static String EXCHANGE_NAME="test_exchange";
    public static String QUEUE_NAME="test_queue";
    public static String ROUTING_KEY="test_key";

    public static void main(String[] args) {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");

            Connection connection = factory.newConnection();
            Channel channel=connection.createChannel();

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

            String message = "Hello via Exchange";
            channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY,null, message.getBytes());
            /*
            * exchange("" means default exchange will be used i.e. direct exchange),
            * routing key,
            * props(message meta data)
            * message.getBytes()(Actual message payload)*/

            System.out.println("Sent: "+message);
            channel.close();
            connection.close();
        }catch(Exception e){
            System.out.println("Error: "+e.getMessage());
        }

    }
}
