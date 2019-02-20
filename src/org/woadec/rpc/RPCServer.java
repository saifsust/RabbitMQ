package org.woadec.rpc;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.woadec.connections.ConnectionEstablisher;
import org.woadec.connections.Establisher;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

public class RPCServer {

	private static final int SIZE = 1000;
	private static int[] dp = new int[SIZE + 1];
	private static final Logger log = (Logger) LoggerFactory.getLogger(RPCServer.class);

	static {

		dp[1] = 1;
		for (int i = 2; i <= SIZE; ++i) {
			dp[i] = dp[i - 1] + dp[i - 2];
		}

	}

	public static void main(String[] args) {

		try {

			Channel channel = ConnectionEstablisher.getInstance().getChannel();

			channel.queueDeclare(Establisher.RPC_QUEUE_NAME, !Establisher.IS_DURABLE, false, false, null);

			channel.queuePurge(Establisher.RPC_QUEUE_NAME);

			channel.basicQos(1);
			System.out.println(" [x] Awaiting RPC requests");

			Object monitor = new Object();

			DeliverCallback callback = new DeliverCallback() {

				@Override
				public void handle(String consumerTag, Delivery message) {

					try {
						String response = "";
						AMQP.BasicProperties props = new AMQP.BasicProperties.Builder()
								.correlationId(message.getProperties().getCorrelationId()).build();
						try {

							String sms = new String(message.getBody());

							int value = Integer.parseInt(sms);

							System.out.println("Request SMS : " + value);

							response += dp[value];

							Thread.sleep(10);

							// fib(value);
							System.out.println("Response SMS : " + response);

						} catch (Exception e) {
							// TODO: handle exception
							log.error(Establisher.CAUSE + e.getCause());
							log.error(Establisher.EXCEPTION_MESSAGE + e.getMessage());
						} finally {

							channel.basicPublish("", message.getProperties().getReplyTo(), props, response.getBytes());
							channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
						}
						synchronized (monitor) {
							monitor.notify();
						}

					}

					catch (Exception ex) {
						log.error(Establisher.CAUSE + ex.getCause());

					}
				}

			};

			channel.basicConsume(Establisher.RPC_QUEUE_NAME, false, callback, com -> {
			});
			log.info(Establisher.SUCCESS);

			while (true) {
				synchronized (monitor) {
					try {
						monitor.wait();
					} catch (Exception e) {
						// TODO: handle exception

						log.error(Establisher.CAUSE + e.getCause());
					}

				}

			}

		} catch (Exception e) {
			// TODO: handle exception

			log.error(Establisher.CAUSE + e.getCause());
			log.error(Establisher.EXCEPTION_MESSAGE + e.getMessage());
		}

	}

	private static int fib(int n) {
		if (n == 0)
			return 0;
		if (n == 1)
			return 1;
		return fib(n - 1) + fib(n - 2);
	}

	/*
	 * private static int fib(int n) {
	 * 
	 * if (n == 0) return 0; if (n == 1) return 1; if (dp[n] != 0) return dp[n];
	 * return dp[n] = fib(n - 1) + fib(n - 2); }
	 */

}
