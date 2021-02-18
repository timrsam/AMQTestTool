package com.activemq.test.test_client;

import java.io.FileInputStream;
import java.util.Properties;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.apache.activemq.ActiveMQConnectionFactory;

public class Subscriber {

	ActiveMQConnectionFactory connFactory;
	String rcvMsg;
	private Properties config;
	private String userName;
	private String password;
	private String broker;
	
	public Subscriber() {
		config = new Properties();
		try {
			
			FileInputStream fis = new FileInputStream("C:\\Users\\Timothy Sam\\eclipse-workspace\\test-client\\src\\test\\resources\\config.properties");
			config.load(fis);
			userName = config.getProperty("user");
			password = config.getProperty("password");
			broker = config.getProperty("brokerURL");
			
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
		Connection connection = connFactory.createConnection();
		connection.start();
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Destination topic = session.createTopic("World");
		MessageConsumer consumer = session.createConsumer(topic);
		MessageListener listener = new MessageListener() {
			public void onMessage(Message message) {
				try {
					if (message instanceof TextMessage) {
						TextMessage textMessage = (TextMessage) message;
						System.out.println("RECIEVED: " + textMessage.getText());
					}
				} catch (JMSException e) {
					System.out.println("Caught:" + e);
				}
			}
		};
		
		consumer.setMessageListener(listener);
		try {
			System.in.read();
		} catch (Exception e) {
			System.out.println(e);
		}
		
		connection.close();
	}
	
}
