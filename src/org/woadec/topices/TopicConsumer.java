package org.woadec.topices;

import java.io.IOException;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.woadec.connections.ConnectionEstablisher;
import org.woadec.connections.Establisher;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

public class TopicConsumer {

	private static final Logger log = (Logger) LoggerFactory.getLogger(TopicConsumer.class);

	public static void main(String[] args) {

		try {

			System.out.println("Topic Consumer ");

			Channel channel = ConnectionEstablisher.getInstance().getChannel();

			Scanner read = ConnectionEstablisher.getInstance().getScanner();

			channel.exchangeDeclare(Establisher.TOPIC_EXCHANGE_NAME, Establisher.TOPIC_EXCHANGE_TYPE);

			channel.exchangeDeclare(Establisher.TOPIC_EXCHANGE_NAME, Establisher.TOPIC_EXCHANGE_TYPE);
			String queue = channel.queueDeclare().getQueue();
			System.out.println("Enter Routing Key");
			while (read.hasNext()) {
				
				String routingKey = read.next();

				if (routingKey.equals("exit"))
					break;
				channel.queueBind(queue, Establisher.TOPIC_EXCHANGE_NAME, routingKey);

			}

			DeliverCallback callback = new DeliverCallback() {

				@Override
				public void handle(String consumerTag, Delivery message) throws IOException {
					// TODO Auto-generated method stub

					System.out.println(
							new String(message.getBody()) + " routinh key :  " + message.getEnvelope().getRoutingKey());

				}
			};

			channel.basicConsume(queue, Establisher.IS_AUTO_ACK, callback, consumerTag -> {
			});

			log.info(Establisher.SUCCESS);
			

		} catch (Exception ex) {
			log.error(Establisher.CAUSE + ex.getCause());
			log.error(Establisher.EXCEPTION_MESSAGE + ex.getMessage());

		}
	}

}
