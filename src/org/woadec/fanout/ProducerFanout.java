package org.woadec.fanout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.woadec.connections.ConnectionEstablisher;
import org.woadec.connections.Establisher;

import com.rabbitmq.client.Channel;

public class ProducerFanout {

	private static final Logger log = (Logger) LoggerFactory.getLogger(ProducerFanout.class);

	public static void main(String[] args) {

		try {
			Channel channel = ConnectionEstablisher.getInstance().getChannel();

			channel.exchangeDeclare(Establisher.FANOUT_EXCHANGE_NAME, Establisher.FANOUT_EXCHANGE_TYPE);

			String sms = "I am fanout exchanger ";

			channel.basicPublish(Establisher.FANOUT_EXCHANGE_NAME, "", null, sms.getBytes());

			log.info(Establisher.SUCCESS);

		} catch (Exception e) {
			// TODO: handle exception
			log.error("ProducerFanout " + Establisher.CAUSE + e.getCause());
			log.error("ProducerFanout " + Establisher.EXCEPTION_MESSAGE + e.getMessage());
		}

	}

}
