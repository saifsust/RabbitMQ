package org.woadec.topices;

import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.woadec.connections.ConnectionEstablisher;
import org.woadec.connections.Establisher;

import com.rabbitmq.client.Channel;

public class TopicProducer {

	private static final Logger log = (Logger) LoggerFactory.getLogger(TopicProducer.class);

	public static void main(String[] args) {

		try {

			System.out.println("Topic Producer");

			Channel channel = ConnectionEstablisher.getInstance().getChannel();

			Scanner read = ConnectionEstablisher.getInstance().getScanner();

			channel.exchangeDeclare(Establisher.TOPIC_EXCHANGE_NAME, Establisher.TOPIC_EXCHANGE_TYPE);

			String sms, routingKey;
			System.out.println("Enter text and Routing  Key ");
			while (read.hasNext()) {

				sms = read.nextLine();

				if (sms.equals("exit"))
					break;

				routingKey = read.next();

				channel.basicPublish(Establisher.TOPIC_EXCHANGE_NAME, routingKey, null, sms.getBytes());
			}

			log.info(Establisher.SUCCESS);

		

		} catch (Exception ex) {

			log.info(Establisher.CAUSE + ex.getCause());
			log.info(Establisher.EXCEPTION_MESSAGE + ex.getMessage());

		}

	}

}
