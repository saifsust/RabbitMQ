package org.woadec.connections;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class ConnectionEstablisher {
	private static final Logger log = (Logger) LoggerFactory.getLogger(ConnectionEstablisher.class);

	private ConnectionFactory factory;
	private Connection connection;
	private Channel channel;
	private static ConnectionEstablisher instance;

	private Scanner scanner;

	private ConnectionEstablisher() {

		try {
			factory = new ConnectionFactory();
			factory.setHost(Establisher.URL_HOST);
			connection = factory.newConnection();
			channel = connection.createChannel();

			scanner = new Scanner(System.in);
			log.info("connection " + Establisher.SUCCESS);

		} catch (Exception ex) {
			log.error("connection error : " + ex.getMessage());
		}
	}

	/**
	 * return instance of ConnectionEstablisher class.
	 * 
	 * @return instance
	 */

	public static final ConnectionEstablisher getInstance() {
		if (instance == null)
			instance = new ConnectionEstablisher();
		return instance;
	}

	/**
	 * get channel fron instance;
	 * 
	 * @return channel
	 */

	public final Channel getChannel() {
		return channel;
	}

	/**
	 * close all connection which are established for communications
	 * 
	 */

	public final void close() {

		try {
			channel.close();
			connection.close();
		} catch (IOException | TimeoutException e) {
			log.error("close  " + Establisher.CAUSE + e.getCause());
			log.error("close " + Establisher.EXCEPTION_MESSAGE + e.getMessage());
		}
	}

	/**
	 * read colsole input data
	 * 
	 * @return scanner
	 */
	public Scanner getScanner() {
		return scanner;
	}

}
