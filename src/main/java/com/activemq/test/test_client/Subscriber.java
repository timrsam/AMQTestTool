package com.activemq.test.test_client;

//import java.io.FileInputStream;
//import java.util.Properties;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.apache.activemq.ActiveMQConnectionFactory;

public class Subscriber implements Runnable, MessageListener {

	ActiveMQConnectionFactory connFactory;
	String rcvMsg;
	//private Properties config;
	private String userName;
	private String password;
	private String broker;
	private String topicName;
	public boolean stopThread;
	Connection connection;
	
	public Subscriber(String brok, String top) {
		//config = new Properties();
		try {
			
			//FileInputStream fis = new FileInputStream("C:\\Users\\Timothy Sam\\eclipse-workspace\\test-client\\src\\test\\resources\\config.properties");
			//FileInputStream fis = new FileInputStream(path);
			//config.load(fis);
			userName = Handler.mode.getProperty("user");
			password = Handler.mode.getProperty("password");
			//broker = config.getProperty("brokerURL");
			broker = "tcp://" + brok + ":61616";
			topicName = top;
			stopThread = false;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void recieve() throws Exception {
		System.out.println("USER: " + userName);
		System.out.println("PASSWORD: " + password);
		System.out.println("BROKER: " + broker);
		connFactory = new ActiveMQConnectionFactory(userName, password, broker);
		connection = connFactory.createConnection();
		connection.start();
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Destination topic = session.createTopic(topicName);
		MessageConsumer consumer = session.createConsumer(topic);
		//TODO Need to fix listner and onMessage method.
		consumer.setMessageListener(this);
	}
	
	public void close() throws Exception {
		connection.close();
	}
	
	//Allows multi-threading of listener
	public void run() {
		
		try {
			while(!stopThread) {
				//Do nothing until calling thread says stop.
			}
			this.close();
		} catch (Exception e) {
			
		}
	}
	
	//Implements the onMessage method of the listener
	public void onMessage(Message message) {
		try {
			if (message instanceof TextMessage) {
				TextMessage textMessage = (TextMessage) message;
				System.out.println("BROKER: " + broker + textMessage.getText());
			}
		} catch (JMSException e) {
			System.out.println("Caught:" + e);
		}
		}
	
}
