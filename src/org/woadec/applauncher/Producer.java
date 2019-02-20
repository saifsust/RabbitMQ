package org.woadec.applauncher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.woadec.connections.ConnectionEstablisher;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

public class Producer {

	private static final Logger log = (Logger) LoggerFactory.getLogger(Producer.class);

	public static final String HOST = "localhost";
	public static final String QUEUE_NAME = "TEST1";
	public static final boolean IS_DURABLE = true;

	public static void main(String[] args) {

		try {

			Channel channel = ConnectionEstablisher.getInstance().getChannel();

			channel.queueDeclare(QUEUE_NAME, IS_DURABLE, false, false, null);
			String massage = "I am ... Saiful Islam liton";

			channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, massage.getBytes());

			log.info("successful");
			// conn.close();

		} catch (Exception e) { // TODO: handle exception

			log.error(" error : " + e.getMessage());

		}

	}

}
