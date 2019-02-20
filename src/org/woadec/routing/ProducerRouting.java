package org.woadec.routing;

import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.woadec.connections.ConnectionEstablisher;
import org.woadec.connections.Establisher;

import com.rabbitmq.client.Channel;

public class ProducerRouting {

	private static final Logger log = (Logger) LoggerFactory.getLogger(ProducerRouting.class);

	public static void main(String[] args) {

		try {

			Channel channel = ConnectionEstablisher.getInstance().getChannel();

			Scanner read = ConnectionEstablisher.getInstance().getScanner();

			System.out.println("Hi I am a Producer\n");

			while (read.hasNext()) {
				String sms = read.nextLine();
				if (sms.equals("exit"))
					break;
				String routingKey = read.next();

				channel.basicPublish(Establisher.DIRECT_EXCHANGE_NAME, routingKey, null, sms.getBytes());
			}
			/*
			 * String routingKey = read.next();
			 * 
			 * channel.basicPublish(Establisher.DIRECT_EXCHANGE_NAME, routingKey, null,
			 * sms.getBytes());
			 */

			log.info(Establisher.SUCCESS);

		} catch (Exception e) {
			log.error(Establisher.CAUSE + e.getCause());
			log.error(Establisher.EXCEPTION_MESSAGE + e.getMessage());
		}

	}

}
