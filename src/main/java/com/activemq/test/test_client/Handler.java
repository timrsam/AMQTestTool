package com.activemq.test.test_client;

import java.io.FileInputStream;
import java.util.Properties;

public class Handler {
	  public static int mainTester(boolean choice) throws Exception {
		  
		  String OS = System.getProperty("os.name").toLowerCase();
		  String path;
		  if (OS.indexOf("win") >= 0) {
			  path = "C:\\Users\\Timothy Sam\\eclipse-workspace\\test-client\\src\\test\\resources\\config.properties";
		  } else {
			  path = "..//src//test//resources//config.properties";
		  }
		  
		  //String msg = "Hello World";
		  Properties config;
		  config = new Properties();
		  FileInputStream fis = new FileInputStream(path);
		  config.load(fis);
		  String msg = config.getProperty("message");
		  
		  System.out.println(msg);
		  try {
			  if (choice) {
				  Publisher pub = new Publisher(path);
				  pub.submit(msg);
			  } else {
				  Subscriber sub = new Subscriber(path);
				  sub.recieve();
			  }
			  
  
		  }
		  catch (Exception e) {
			  System.out.println(e);
			  return 1;
		  }
		 		  
		  return 0;
	  }
	  public static void main(String[] args) {
		  try {

			  
			  if (args[0].toLowerCase().equals("pub")) {
				  int val = mainTester(true);
				  System.out.println(val);
			  } else if (args[0].toLowerCase().equals("sub")) {
				  int val = mainTester(false);
				  System.out.println(val);
			  } else {
				  System.out.println("Invalid Input! Exiting.");
			  }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.exit(0);
	  }
}
