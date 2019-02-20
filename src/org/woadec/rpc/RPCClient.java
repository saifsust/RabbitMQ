package org.woadec.rpc;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.woadec.connections.ConnectionEstablisher;
import org.woadec.connections.Establisher;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

public class RPCClient implements AutoCloseable {

	private static final Logger log = (Logger) LoggerFactory.getLogger(RPCClient.class);

	public static void main(String[] args) {
		try {

			Channel channel = ConnectionEstablisher.getInstance().getChannel();

			/*
			 * for (int i = 0; i <= 32; ++i) { String num = Integer.toString(i);
			 * System.out.println("Request : " + num);
			 * 
			 * String response = call(num);
			 * 
			 * System.out.println("Response : " + response);
			 * 
			 * }
			 */

			System.out.println(call(Integer.toString(5)));

			log.info(Establisher.SUCCESS);

		} catch (Exception e) {
			// TODO: handle exception
			log.error(Establisher.CAUSE + e.getCause());
			log.error(Establisher.EXCEPTION_MESSAGE + e.getMessage());
		}

	}

	private static String call(String message) throws Exception {

		Channel channel = ConnectionEstablisher.getInstance().getChannel();

		String reply = channel.queueDeclare().getQueue();

		final String corrId = UUID.randomUUID().toString();

		AMQP.BasicProperties props = new AMQP.BasicProperties.Builder().correlationId(corrId).replyTo(reply).build();

		channel.basicPublish("", reply, props, message.getBytes());

		final BlockingQueue<String> response = new ArrayBlockingQueue<>(1);

		DeliverCallback callback = new DeliverCallback() {

			@Override
			public void handle(String consumerTag, Delivery message) throws IOException {
				// TODO Auto-generated method stub

				if (message.getProperties().getCorrelationId().equals(corrId)) {
					response.offer(new String(message.getBody()));
				}

			}
		};

		String ctag = channel.basicConsume(reply, Establisher.IS_AUTO_ACK, callback, con -> {
		});

		String result = response.take();
		channel.basicCancel(ctag);

		return result;

	}

	@Override
	public void close() throws Exception {
		ConnectionEstablisher.getInstance().close();

	}

}
