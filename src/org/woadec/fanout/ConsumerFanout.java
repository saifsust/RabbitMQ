package org.woadec.fanout;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.woadec.connections.ConnectionEstablisher;
import org.woadec.connections.Establisher;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

public class ConsumerFanout {

	private static final Logger log = (Logger) LoggerFactory.getLogger(ConsumerFanout.class);

	public static void main(String[] args) {

		try {

			Channel channel = ConnectionEstablisher.getInstance().getChannel();

			channel.exchangeDeclare(Establisher.FANOUT_EXCHANGE_NAME, Establisher.FANOUT_EXCHANGE_TYPE);

			String queue = channel.queueDeclare().getQueue();
			channel.queueBind(queue, Establisher.FANOUT_EXCHANGE_NAME, "");

			DeliverCallback callback = new DeliverCallback() {
				@Override
				public void handle(String consumerTag, Delivery message) throws IOException {
					System.out.println(new String(message.getBody()));
				}
			};

			channel.basicConsume(queue, true, callback, ConsumerTag -> {
			});

			log.info(Establisher.SUCCESS);

		} catch (Exception e) {
			// TODO: handle exception
			log.error("consumer fanout : " + e.getCause());
		}

	}

}
