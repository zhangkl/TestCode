package rabbitMQ;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import static oracle.net.aso.C11.k;

public class TestMQ {

    public TestMQ() throws Exception{
		
		QueueConsumer consumer = new QueueConsumer("queue");
		Thread consumerThread = new Thread(consumer);
		consumerThread.start();
		
		Producer producer = new Producer("queue");
		
		for (int i = 0; i < 6; i++) {
			HashMap message = new HashMap();
			message.put("message number", i);
			producer.sendMessage(message);
			System.out.println("Message Number "+ i +" sent.");
		}
	}
	
	/**
	 * @param args
	 * @throws SQLException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws Exception{
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY,9);
		cal.set(Calendar.MINUTE,30);
		cal.set(Calendar.SECOND,00);
		DateFormat df = new SimpleDateFormat("hhmmss");
		System.out.println(df.format(cal.getTime()));
		cal.add(Calendar.MINUTE,5);
		System.out.println(cal.getTime());
	}
}