package com.activemq.test.test_client;

import java.io.Console;
import java.io.FileInputStream;
import java.util.Properties;

import javax.jms.Connection;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import org.apache.activemq.ActiveMQConnectionFactory;

public class Publisher {

	private ActiveMQConnectionFactory connectionFactory;
	private String def;
	private Properties config;
	private String userName;
	private String password;
	private String broker;
		
	public Publisher(String path) {
		def = "Default Message";
		config = new Properties();
		try {
			
			//FileInputStream fis = new FileInputStream("C:\\Users\\Timothy Sam\\eclipse-workspace\\test-client\\src\\test\\resources\\config.properties");
			FileInputStream fis = new FileInputStream(path);
			config.load(fis);
			userName = config.getProperty("user");
			password = config.getProperty("password");
			broker = config.getProperty("brokerURL");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void submit() throws Exception {
		submit(def);
	}
	public void submit(String message) throws Exception {
		
		//connectionFactory.setBrokerURL("tcp://10.32.0.9:61616");
		System.out.println("USER: " + userName);
		System.out.println("PASSWORD: " + password);
		System.out.println("BROKER: " + broker);
		connectionFactory = new ActiveMQConnectionFactory(userName, password, broker);
		Connection connection = connectionFactory.createConnection();
		connection.start();
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Topic topic = session.createTopic("World");
		MessageProducer producer = session.createProducer(topic);
		
		//User Text
		Console console = System.console();
		while (!message.equals("n") || !message.equals("N")) {
			System.out.print("MESSAGE? (n/N to quit): ");
			message = console.readLine();
			TextMessage textMessage = session.createTextMessage(message);
			producer.send(textMessage);
		}
		connection.stop();
		
	}
}
