package com.activemq.test.test_client;

public class Handler {
	  public static int mainTester(boolean choice) throws Exception {
		  
		  String msg = "Hello World";
		  
		  System.out.println(msg);
		  try {
			  if (choice) {
				  Publisher pub = new Publisher();
				  pub.submit(msg);
			  } else {
				  Subscriber sub = new Subscriber();
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
