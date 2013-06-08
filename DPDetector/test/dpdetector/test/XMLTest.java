package dpdetector.test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import dpdetector.util.PatternXmlHelper;

import junit.framework.TestCase;

public class XMLTest extends TestCase {
	public void testXML(){
		//Document document = DocumentHelper.createDocument();
		Element root =PatternXmlHelper.createPattern("Adapter");		
		Element target = PatternXmlHelper.createRole("Target");
		Element targetClass = PatternXmlHelper.createClass("org.test.ClassA");
		target.add(targetClass);
		Element adapter = PatternXmlHelper.createRole("Adapter");
		Element adaptee = PatternXmlHelper.createRole("Adaptee");

		root.add(target);
		root.add(adapter);
		root.add(adaptee);
		
		try {
			OutputFormat format = OutputFormat.createPrettyPrint();
			XMLWriter writer = new XMLWriter(System.out,format);
			writer.write(root);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
}
