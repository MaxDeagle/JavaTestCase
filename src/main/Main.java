package main;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

public class Main {
	
	
	public static void main(String args[]) throws ParserConfigurationException, TransformerException, SAXException, IOException
	{
		int N = 0;
		String adress = "";
		
	    try (Scanner sc = new Scanner(System.in))
	    {
	    System.out.println("Enter path to DB and N (example: \n testbase.s3db \n 10 \n)");
	    adress = sc.next();
	    N = sc.nextInt();	    
	    }
	    
	    long startTime = System.currentTimeMillis();
	    
		DBHandler DataBaseWorker = new DBHandler();
		DataBaseWorker.setAdress(adress);
		DataBaseWorker.setN(N);
     	
		List<Integer> numbers;	
		
		DataBaseWorker.deleteFromDB();
					
		DataBaseWorker.writeInDB();
			
		numbers = DataBaseWorker.readFromDB();
			
		XMLHandler.writeInXML(numbers);
			
		XMLHandler.transformIntoSecondXML();
			
		System.out.println(XMLHandler.parseFromXML());
		
		System.out.println("Time elapsed: " + (System.currentTimeMillis() - startTime) + "ms");
	
	}
	
}