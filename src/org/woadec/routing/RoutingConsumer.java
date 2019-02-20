package org.woadec.routing;

import java.io.IOException;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.woadec.connections.ConnectionEstablisher;
import org.woadec.connections.Establisher;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

public class RoutingConsumer {

	private static final Logger log = (Logger) LoggerFactory.getLogger(RoutingConsumer.class);

	public static void main(String[] args) {
		try {

			System.out.println("Hi I am a Consumers \n");

			Channel channel = ConnectionEstablisher.getInstance().getChannel();
			Scanner read = ConnectionEstablisher.getInstance().getScanner();

			channel.exchangeDeclare(Establisher.DIRECT_EXCHANGE_NAME, Establisher.DIRECT_EXCHANGE_TYPE);

			String queue = channel.queueDeclare().getQueue();

			// System.out.println(queue);

			while (read.hasNext()) {
				String routingKey = read.next();

				if (routingKey.equals("exit"))
					break;
				channel.queueBind(queue, Establisher.DIRECT_EXCHANGE_NAME, routingKey);
			}

			/*
			 * String routingKey = read.next();
			 * 
			 * channel.queueBind(queue, Establisher.DIRECT_EXCHANGE_NAME, routingKey);
			 */
			DeliverCallback callback = new DeliverCallback() {

				@Override
				public void handle(String consumerTag, Delivery message) throws IOException {
					System.out.println(
							new String(message.getBody()) + "  routinh key " + message.getEnvelope().getRoutingKey());
				}
			};

			channel.basicConsume(queue, Establisher.IS_AUTO_ACK, callback, consumerTag -> {
			});

			log.info(Establisher.SUCCESS);

		} catch (Exception e) {
			log.error(Establisher.CAUSE + e.getCause());
			log.error(Establisher.EXCEPTION_MESSAGE + e.getMessage());
		}

	}

}
