package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.filter.ElementFilter;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;

public class XMLHandler {
	private static Logger log = Logger.getLogger(DBHandler.class.getName());
	private static final String PATH_TO_PROPERTIES = "src/main/resources/config.properties";
	private static Properties prop = null;
	private static String firstXmlName;
	private static String secondXmlName;
	private static String transformerName;

	public XMLHandler() {
		this.setProperties();
	}

	private void setProperties() {
		prop = new Properties();

		try {
			FileInputStream stream = new FileInputStream(PATH_TO_PROPERTIES);
			prop.load(stream);
			firstXmlName = prop.getProperty("firstXmlName");
			secondXmlName = prop.getProperty("secondXmlName");
			transformerName = prop.getProperty("transformerName");
			log.log(Level.INFO, "Successfully read out properties");
		} catch (IOException e) {
			log.log(Level.SEVERE, "File " + PATH_TO_PROPERTIES + " not found!", e);
		}
	}

	public void writeInXML(List<Integer> numbers) {
		if (prop == null)
			this.setProperties();
		try {
			log.addHandler(new StreamHandler(new FileOutputStream("Application.log", true), new SimpleFormatter()));
		} catch (SecurityException e) {
			log.log(Level.SEVERE, "Can't create log file. Security error.", e);
		} catch (IOException e) {
			log.log(Level.SEVERE, "Can't create log file. Input-output error.", e);
		}
		Document XMLDocument = new Document();
		Element root = new Element("entries");
		XMLDocument.setRootElement(root);
		for (int number : numbers) {
			root.addContent(new Element("entry").addContent(new Element("field").addContent(String.valueOf(number))));
		}
		XMLOutputter serializer = new XMLOutputter();
		try {
			serializer.output(XMLDocument, new FileOutputStream(new File(firstXmlName)));
			log.log(Level.INFO, "Successfully written data in " + firstXmlName);
		} catch (FileNotFoundException e) {
			log.log(Level.SEVERE, "File not found!", e);
		} catch (IOException e) {
			log.log(Level.SEVERE, "Can't write data in file!", e);
		}
	}

	public void transformIntoSecondXML() {
		if (prop == null)
			this.setProperties();
		try {
			FileInputStream stream = new FileInputStream(PATH_TO_PROPERTIES);
			prop.load(stream);
			firstXmlName = prop.getProperty("firstXmlName");
			secondXmlName = prop.getProperty("secondXmlName");
			transformerName = prop.getProperty("transformerName");
			log.log(Level.INFO, "Successfully read out properties");
		} catch (IOException e) {
			log.log(Level.SEVERE, "File " + PATH_TO_PROPERTIES + " not found!", e);
		}

		try {
			log.addHandler(new StreamHandler(new FileOutputStream("Application.log", true), new SimpleFormatter()));

		} catch (SecurityException e) {
			log.log(Level.SEVERE, "Can't create log file. Security error.", e);
		} catch (IOException e) {
			log.log(Level.SEVERE, "Can't create log file. Input-output error.", e);
		}

		try {
			TransformerFactory factory = TransformerFactory.newInstance();
			Templates template = factory.newTemplates(new StreamSource(new FileInputStream(transformerName)));
			Transformer xformer = template.newTransformer();
			Source sourceFile = new StreamSource(new FileInputStream(firstXmlName));
			Result resultFile = new StreamResult(new FileOutputStream(secondXmlName));
			xformer.transform(sourceFile, resultFile);
			log.log(Level.INFO, "Successfully transformed " + firstXmlName + " into " + secondXmlName);
		} catch (FileNotFoundException e) {
			log.log(Level.SEVERE, "File not found!", e);
		} catch (TransformerConfigurationException e) {
			log.log(Level.SEVERE, "Wrong transformer configuration!", e);
		} catch (TransformerException e) {
			log.log(Level.SEVERE, "Can't transform XML!", e);
		}
	}

	public int parseFromXML() {
		if (prop == null)
			this.setProperties();
		try {
			FileInputStream stream = new FileInputStream(PATH_TO_PROPERTIES);
			prop.load(stream);
			firstXmlName = prop.getProperty("firstXmlName");
			secondXmlName = prop.getProperty("secondXmlName");
			transformerName = prop.getProperty("transformerName");
			log.log(Level.INFO, "Successfully read out properties");
		} catch (IOException e) {
			log.log(Level.SEVERE, "File " + PATH_TO_PROPERTIES + " not found!", e);
		}

		try {
			log.addHandler(new StreamHandler(new FileOutputStream("Application.log", true), new SimpleFormatter()));

		} catch (SecurityException e) {
			log.log(Level.SEVERE, "Can't create log file. Security error.", e);
		} catch (IOException e) {
			log.log(Level.SEVERE, "Can't create log file. Input-output error.", e);
		}
		SAXBuilder parser = new SAXBuilder();
		Document XMLDocument;
		int sum = 0;
		try {
			XMLDocument = parser.build(new File(secondXmlName));

			List<Element> numbers = XMLDocument.getRootElement().getContent(new ElementFilter("entry"));

			Iterator<Element> iterator = numbers.iterator();
			while (iterator.hasNext()) {
				Element head = (Element) iterator.next();
				sum += Integer.parseInt(head.getAttributeValue("field"));
			}
			log.log(Level.INFO, "Successfully read out data from " + secondXmlName);
		} catch (JDOMException e) {
			log.log(Level.SEVERE, "Can't parse " + secondXmlName, e);
		} catch (IOException e) {
			log.log(Level.SEVERE, "Can't read file!", e);
		}
		return sum;
	}
}
