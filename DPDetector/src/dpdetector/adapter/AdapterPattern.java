package dpdetector.adapter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import dpdetector.util.PatternXmlHelper;

import recoder.abstraction.ClassType;

public class AdapterPattern {
	private ClassType adapter, adaptee, target;
	private List<ClassType> clients = new ArrayList<ClassType>();
	
	/**
	 * 
	 * @param adapter
	 * @param adaptee
	 * @param target
	 */
	public AdapterPattern(ClassType adapter, ClassType adaptee, ClassType target){
		this.adapter = adapter;
		this.adaptee = adaptee;
		this.target = target;
	}
	
	public ClassType getTarget(){
		return target;
	}
	public ClassType getAdaptee(){
		return adaptee;
	}
	
	public ClassType getAdapter(){
		return adapter;
	}
	
	public void addClient(ClassType client){
		clients.add(client);
	}

	public String getTargetFullPkgName(){
		return target.getPackage().getFullName();
	}
	public String getTargetFullName(){
		return target.getFullName();
	}
	public boolean equalsTarget(ClassType c){
		if(c.equals(target)){
			return true;
		}
		return false;
	}
	public boolean equalsAdapter(ClassType c){
		if(c.equals(adapter)){
			return true;
		}
		return false;
	}
	
	public boolean hasClient(){
		return !clients.isEmpty();
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((adaptee == null) ? 0 : adaptee.hashCode());
		result = prime * result + ((adapter == null) ? 0 : adapter.hashCode());
		result = prime * result + ((target == null) ? 0 : target.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AdapterPattern other = (AdapterPattern) obj;
		if (adaptee == null) {
			if (other.adaptee != null)
				return false;
		} else if (!adaptee.equals(other.adaptee))
			return false;
		if (adapter == null) {
			if (other.adapter != null)
				return false;
		} else if (!adapter.equals(other.adapter))
			return false;
		if (target == null) {
			if (other.target != null)
				return false;
		} else if (!target.equals(other.target))
			return false;
		return true;
	}
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("Adapter: " + adapter.getFullName() +"\n" +
				"Adaptee: " + adaptee.getFullName() + "\n" +
				"Target: " + target.getFullName() + "\n");
//		sb.append("Client(s):\n");
//		for(ClassType client : clients){
//			sb.append("\t"+client.getFullName()+"\n");
//		}
		return sb.toString();
				
	}
	
	public Element toXML(){
		Element rootElement =PatternXmlHelper.createPattern("Adapter");		
		Element targetElement = PatternXmlHelper.createRole("Target");
		Element adapterElement = PatternXmlHelper.createRole("Adapter");
		Element adapteeElement = PatternXmlHelper.createRole("Adaptee");
		
		targetElement.add(PatternXmlHelper.createClass(target.getFullName()));
		adapterElement.add(PatternXmlHelper.createClass(adapter.getFullName()));
		adapteeElement.add(PatternXmlHelper.createClass(adaptee.getFullName()));

		rootElement.add(targetElement);
		rootElement.add(adapterElement);
		rootElement.add(adapteeElement);
		
		return rootElement;
	
	}
	
}
