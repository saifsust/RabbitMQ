package org.woadec.consumers;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.woadec.applauncher.Producer;
import org.woadec.connections.ConnectionEstablisher;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

public class ConsumerTwo {
	private static final Logger log = (Logger) LoggerFactory.getLogger(ConsumerTwo.class);

	public static void main(String[] args) {

		try {

			Channel channel = ConnectionEstablisher.getInstance().getChannel();

			channel.queueDeclare(Producer.QUEUE_NAME, Producer.IS_DURABLE, false, false, null);
			System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
			channel.basicQos(1);

			DeliverCallback deliverCallback = new DeliverCallback() {

				@Override
				public void handle(String consumerTag, Delivery message) throws IOException {

					// System.out.println(consumerTag);

					String sms = new String(message.getBody());

					System.out.println("consumer two : " + sms);

					try {
						doWork(sms);
					} catch (Exception ex) {
						log.error("Thread sleep Exception : " + ex.getMessage());
					} finally {
						System.out.println(" [x] Done");
						channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
					}

				}
			};

			boolean autoAck = false;

			channel.basicConsume(Producer.QUEUE_NAME, autoAck, deliverCallback, Consumer -> {
			});

			// conn.close();

		} catch (Exception ex) {
			log.error("consumer Two error : " + ex.getCause());
		}

	}

	private static void doWork(String task) throws Exception {

		for (char chr : task.toCharArray()) {

			// System.out.println(chr);

			if (chr == '.') {
				Thread.sleep(2000);
			}
		}

	}

}
