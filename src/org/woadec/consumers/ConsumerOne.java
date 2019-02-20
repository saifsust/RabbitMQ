package org.woadec.consumers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.woadec.applauncher.Producer;
import org.woadec.connections.ConnectionEstablisher;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

public class ConsumerOne {

	private static final Logger log = (Logger) LoggerFactory.getLogger(ConsumerOne.class);

	public static void main(String[] args) {

		try {
			Channel channel = ConnectionEstablisher.getInstance().getChannel();

			channel.queueDeclare(Producer.QUEUE_NAME, Producer.IS_DURABLE, false, false, null);

			DeliverCallback deliverCallback = (consumerTag, delivery) -> {
				System.out.println("Hello world");
				String message = new String(delivery.getBody());
				System.out.println("consumer one : " + message);
			};

			channel.basicConsume(Producer.QUEUE_NAME, true, deliverCallback, consumerTag -> {
			});

			log.info("receive successful");
			// conn.close();
		} catch (Exception ex) {
			log.error("error : " + ex.getMessage());
		}

	}

}
