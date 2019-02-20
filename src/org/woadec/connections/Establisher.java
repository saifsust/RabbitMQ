package org.woadec.connections;

public class Establisher {
	protected static final String URL_HOST = "localhost";

	public static final boolean IS_AUTO_ACK = true;

	public static final String QUEUE_NAME = "test_queue";
	public static final boolean IS_DURABLE = true;
	public static final String SUCCESS = "ALL are Successful '.' ! ";
	/**
	 * 
	 */

	public static final String CAUSE = " ERROR CAUSES : ";
	public static final String EXCEPTION_MESSAGE = "ERROR MESSAGE : ";

	/**
	 * 
	 */
	public static final String FANOUT_EXCHANGE_TYPE = "fanout";
	public static final String FANOUT_EXCHANGE_NAME = "logs";
	/**
	 * direct
	 * 
	 */
	public static final String DIRECT_EXCHANGE_TYPE = "direct";
	public static final String DIRECT_EXCHANGE_NAME = "direct_logs";
	/**
	 * topic
	 * 
	 */

	public static final String TOPIC_EXCHANGE_TYPE = "topic";
	public static final String TOPIC_EXCHANGE_NAME = "topic_logs";

	/**
	 * 
	 * RPC
	 * 
	 */

	public static final String RPC_QUEUE_NAME = "rpc_queue";
}
