package dpdetector.adapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import dpdetector.util.PatternXmlHelper;

import recoder.abstraction.ClassType;

public class AdapterPatternSet implements Iterable {
	private Set<AdapterPattern> adapterPatternSet = new HashSet<AdapterPattern>();
	public void addInstance(ClassType adapter, ClassType adaptee, ClassType target){
		if(!adapterPatternSet.contains(new AdapterPattern(adapter,adaptee,target))){
			adapterPatternSet.add(new AdapterPattern(adapter,adaptee,target));
		}
	}
	
	public void addClient(AdapterPattern ap, ClassType client){
		for(Iterator it = adapterPatternSet.iterator(); it.hasNext();){
			AdapterPattern apCurrent = (AdapterPattern) it.next();
			if(apCurrent.equals(ap)){
				apCurrent.addClient(client);
			}
		}
	}
	
	public int size(){
		return adapterPatternSet.size();
	}
	
	
	public boolean containsTarget(ClassType potentialTarget){
		for(Iterator it = adapterPatternSet.iterator(); it.hasNext();){
			AdapterPattern apCurrent = (AdapterPattern) it.next();
			if(apCurrent.equalsTarget(potentialTarget)){
				return true;
			}
		}
		return false;
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append(size()+" Adapter Pattern(s) has been detected: \n\n");
		for(Iterator<AdapterPattern> it = adapterPatternSet.iterator();it.hasNext();){
			sb.append(it.next().toString());
			sb.append("\n");
		}
		return sb.toString();
	}
	
	public Element toXML(){
		Element patterns = PatternXmlHelper.createPatternRootElement(size());
		for(Iterator<AdapterPattern> it = adapterPatternSet.iterator();it.hasNext();){
			patterns.add(it.next().toXML());

		}
		return patterns;
	}


	@Override
	public Iterator<AdapterPattern> iterator() {
		return adapterPatternSet.iterator();
	}
	
	public void remove(AdapterPattern ap){
		adapterPatternSet.remove(ap);
	}

	public void add(AdapterPattern ap) {
		adapterPatternSet.add(ap);
		
	}
}
