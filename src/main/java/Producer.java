import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Producer {
    public static void main(String[] args) {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");

            Connection connection = factory.newConnection();
            Channel channel=connection.createChannel();

            String queueName = "test_queue";
            channel.queueDeclare(queueName, false, false, false, null);

            String message = "Hello RabbitMQ";
            channel.basicPublish("", queueName,null, message.getBytes());

            System.out.println("Sent: "+message);
            channel.close();
            connection.close();
        }catch(Exception e){
            System.out.println("Error: "+e.getMessage());
        }

    }
}
