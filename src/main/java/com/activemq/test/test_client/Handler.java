package com.activemq.test.test_client;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Properties;
import java.util.Scanner;

public class Handler {

	//Global Variables
	public static Properties mode;
	public static Properties test;


	//Initialization
	private static void initializer() {
		
		//Determine OS platform (Windows vs. Linux)
		//Obtain path base path to project folder
		String OS = System.getProperty("os.name").toLowerCase();
		String localDir = System.getProperty("user.dir");
		String temp;
		String path;
		
		//Strip "target" folder off of base path
		temp = localDir.substring(0, localDir.length() - 7);
		
		//Assign resource location based on OS
		if (OS.indexOf("win") >= 0) {
			//Path in Windows Form
			path = temp + "\\resources\\";
		} else {
			//Path in Linux Form
			path = temp + "//resources//";
		}

		//Load properties files
		mode = new Properties();
		test = new Properties();
		FileInputStream fis;
		try {
			System.out.println("Loading mode.properties");
			fis = new FileInputStream(path + "mode.properties");
			mode.load(fis);
			System.out.println("Loading test.properties");
			fis = new FileInputStream(path + "test.properties");
			test.load(fis);
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//MAIN
	public static void main(String[] args) {
		
		String appType;
		
		try {
			//Initialize and load configs
			System.out.println("Initializing...");
			initializer();
			
			String[] brokers = mode.getProperty("brokers").split(",");
			String[] topics = mode.getProperty("topics").split(",");
			String brokURL;
			String topicName;
			Scanner myInput = new Scanner( System.in );
			
			if (args.length == 0) {
				//If no arguments specified:
				appType = mode.getProperty("mode");
				//User confirmation of mode
				String input = System.console().readLine("Using in " + appType + " Mode. Acceptable? (y/n): ");
				if (input.equals("y") || input.equals("Y")) {
					switch (appType) {
					case "man":						
						System.out.println("Starting in MANUAL testing mode...");
						manMode();
						break;
					case "auto":
						System.out.println("Starting in AUTOMATIC testing mode...");
						autoMode();
						break;
					case "IM":
						System.out.println("Starting in BONUS FEATURE mode...");
						//Choose Server
						System.out.println("Choose Server (0/1/2/x): ");
						for (int i = 0; i <= brokers.length - 1; i++) {
							System.out.println("(" + i +"): " + brokers[i]);
						}
						int serv = myInput.nextInt();
						brokURL = brokers[serv];
						
						//Choose Topic Subscription
						System.out.println("Choose Topic (0/1/2/x): ");
						for (int i = 0; i <= topics.length - 1; i++) {
							System.out.println("(" + i +"): " + topics[i]);
						}
						int top = myInput.nextInt();
						topicName = topics[top];
						
						easterEggMode(brokURL, topicName);
						break;
					default:
						System.out.println(appType + " is Invalid. Please input one of the following in mode.properties:");
						System.out.println("man/auto/IM");
					}
				} else if (input.equals("n") || input.equals("N")) {
					//Run Interactive Configuration
					//TODO need to build interactive configurator
					String[] i = interactive();
				}

			} else if (args[0].toLowerCase().equals("pub")) {
				int val = amqSimple(true);
				System.out.println(val);
			} else if (args[0].toLowerCase().equals("sub")) {
				int val = amqSimple(false);
				System.out.println(val);
			} else {
				System.out.println("Invalid Arguments! Starting Interactive Configurator.");
				//TODO invoke interactive();
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.exit(0);
	}


	private static int amqSimple(boolean choice) throws Exception {
		//If pub/sub arguments are presented.
		//Simply iterate through broker list once and pub/sub
		
		//Get broker and topic list
		String[] brokers = mode.getProperty("brokers").split(",");
		String[] topics = mode.getProperty("topics").split(",");
		
		try {
			if (choice) {
				//Load Default messsage
				//String msg = mode.getProperty("defaultmsg");
				//System.out.println("MESSAGE BEING SENT: " + msg);
				
				//Iterate through broker list and publish default message.
				for (String brok : brokers) {
					for (String top : topics) {
						Publisher pub = new Publisher(brok, top);
						pub.connect();
						pub.submit();
						pub.disconnect();
					}
				}
			} else {
				Subscriber[] subList = new Subscriber[brokers.length * topics.length];
				int ctr = 0;
				for (String brok : brokers) {
					for (String top : topics) {
						subList[ctr] = new Subscriber(brok, top);
						Thread rcvThd = new Thread(subList[ctr]);
						subList[ctr].format(true);
						rcvThd.start();
						ctr ++;
					}
				}
				System.out.print(ctr + " Listeners Started. Press Enter to stop listening");
				System.in.read();
				for (int i=0; i <= subList.length - 1; i++) {
					subList[i].stopThread = true;
					System.out.println(subList[i].ID + " stopped.");
				}
			}
		}
		catch (Exception e) {
			System.out.println(e);
			//Exit function with error
			return 1;
		}
		
		//exit status 0
		return 0;
	}
	
	private static void manMode() {
		//TODO build out manual testing mode
	}
	
	private static void autoMode() {
		//TODO build out Automatic testing mode
	}

	private static void easterEggMode(String url, String topic) {
		//TODO build out simple instant messaging app mode
		
		//Initialize send and receive components
		Subscriber rcv = new Subscriber(url, topic);
		Publisher send = new Publisher(url, topic);
		Thread rcvThread = new Thread(rcv);
		Thread sendThread = new Thread(send);
		
		
		//Start the components
			rcvThread.start();
			sendThread.start();
			while(sendThread.isAlive()) {
				//Do nothing while sender is alive
			}
			
			//After sender has exited, close reciever
			rcv.stopThread = true;
			System.out.println("Quitting...");
	}
	
	private static String[] interactive() {
		String[] configs = new String[3];

		System.out.println("No config arguments specified. Entering interactive mode.");
		configs[0] = System.console().readLine("Select App Mode - IM, Test Harness (1/2): ");

		if (configs[0].equals("1")) {
			//System.out.print("Run as publisher or subscriber? (pub/sub): ");
			configs[1] = System.console().readLine("Run as the publisher or subscriber? (pub/sub): ");

		} else if (configs[0].equals("2")) {

		}
		return configs;
	}
}
