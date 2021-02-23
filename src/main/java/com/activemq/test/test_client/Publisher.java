package com.activemq.test.test_client;



//import java.io.Console;
//import java.io.FileInputStream;
//import java.util.Properties;

import javax.jms.Connection;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQSslConnectionFactory;

public class Publisher implements Runnable{

	private ActiveMQConnectionFactory connectionFactory;
	private String def;
	//private Properties config;
	private String userName;
	private String password;
	private String broker;
	private String topicName;
	private Connection connection;
	private Session session;
	private MessageProducer producer;
		
	public Publisher(String brok, String top) {
		//config = new Properties();
		try {
			
			//FileInputStream fis = new FileInputStream("C:\\Users\\Timothy Sam\\eclipse-workspace\\test-client\\src\\test\\resources\\config.properties");
			//FileInputStream fis = new FileInputStream(path);
			//config.load(fis);
			userName = Handler.mode.getProperty("user");
			password = Handler.mode.getProperty("password");
			def = Handler.mode.getProperty("defaultmsg");
			//broker = config.getProperty("brokerURL");
			//broker = "tcp://" + brok + ":61616";
			broker = "ssl://" + brok + ":61616";
			topicName = top;
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//Initiate Connection
	public void connect() throws Exception {
		//connectionFactory.setBrokerURL("tcp://10.32.0.9:61616");
		TrustManager[] trustAllCerts = new TrustManager[]{
				new X509TrustManager() {
					public java.security.cert.X509Certificate[] getAcceptedIssuers() {
						return null;
					}
					public void checkClientTrusted(
							java.security.cert.X509Certificate[] certs, String authType) {
					}
					public void checkServerTrusted(
							java.security.cert.X509Certificate[] certs, String authType) {
					}
				}
		};

		try {
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			//TODO
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (Exception e) {
		}
		
		System.out.println("USER: " + userName);
		System.out.println("PASSWORD: " + password);
		System.out.println("BROKER: " + broker);
		//connectionFactory = new ActiveMQConnectionFactory(userName, password, broker);
		connectionFactory = new ActiveMQSslConnectionFactory(broker);
		connectionFactory.setUserName(userName);
		connectionFactory.setPassword(password);
		
		connection = connectionFactory.createConnection();
		connection.start();
		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Topic topic = session.createTopic(topicName);
		producer = session.createProducer(topic);
		
	}
	
	//Disconnect connection
	public void disconnect() throws Exception {
		connection.stop();
	}
	
	//Submit default message to the topic	
	public void submit() throws Exception {
		System.out.println("MESSAGE BEING SENT: " + def);
		submit(def);
	}
	
	//Submit message to the topic	
	public void submit(String message) throws Exception {
		TextMessage textMessage = session.createTextMessage(message);
		producer.send(textMessage);
				
	}
	
	public void run() {
		String message;
		
		try {
			//Connect to the broker
			this.connect();
			System.out.print("MESSAGE? (n/N to quit): ");
			
			do {
				//Capture and submit Message
				message = System.console().readLine();
				this.submit(message);
	
				//Convert to lower case and check for exit case.
				message = message.toLowerCase();
			} while (!message.equals("n"));
			
			//Close connection.
			this.disconnect();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
