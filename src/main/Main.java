package main;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

public class Main {

	public static void main(String args[])
			throws ParserConfigurationException, TransformerException, SAXException, IOException {
		int N = 0;
		String address = "";

		try (Scanner sc = new Scanner(System.in)) {
			System.out.println("Enter path to DB and N (example: \n testbase.s3db \n 10 \n)");
			address = sc.next();
			N = sc.nextInt();
		}
		long startTime = System.currentTimeMillis();
		DBHandler dataBaseWorker = new DBHandler();
		dataBaseWorker.setAdress(address);
		dataBaseWorker.setN(N);
		dataBaseWorker.deleteFromDB();
		dataBaseWorker.writeInDB();
		List<Integer> numbers = dataBaseWorker.readFromDB();
		dataBaseWorker.closeDB();
		XMLHandler xmlWorker = new XMLHandler();
		xmlWorker.writeInXML(numbers);
		xmlWorker.transformIntoSecondXML();
		System.out.println(xmlWorker.parseFromXML());
		System.out.println("Time elapsed: " + (System.currentTimeMillis() - startTime) + "ms");

	}

}