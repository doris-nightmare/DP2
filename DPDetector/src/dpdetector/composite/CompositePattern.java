package dpdetector.composite;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.dom4j.Element;

import dpdetector.util.PatternXmlHelper;

import recoder.abstraction.ClassType;
import recoder.abstraction.Method;

public class CompositePattern {

	public ClassType composite, component;
	private Set<ClassType> leafs = new HashSet<ClassType>();
	private Set<String> compositeOperNames = new HashSet<String>();
	public CompositePattern(ClassType composite, ClassType component, String compositeOperName){
		this.component = component;
		this.composite = composite;
		compositeOperNames.add(compositeOperName);
	}
	public CompositePattern(ClassType composite, ClassType component){
		this.component = component;
		this.composite = composite;
	}
	public void addLeaf(ClassType c){
		if(!leafs.contains(c)){
			leafs.add(c);
		}		
	}
	public boolean hasLeaf(){
		return !leafs.isEmpty();
	}

	public void addCompositeOperName(String compositeOperName){
		compositeOperNames.add(compositeOperName);
	}
	public boolean isCompositeOperMatch(ClassType candidateLeaf){
		//check the candidate leaf class contains all methods of composite operation
		Set<String> leafMethodNames = new HashSet<String>();
		for(Method md:candidateLeaf.getMethods()){
			leafMethodNames.add(md.getName());
		}
		if(leafMethodNames.containsAll(compositeOperNames)){
			return true;
		}
		return false;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((component == null) ? 0 : component.hashCode());
		result = prime * result
				+ ((composite == null) ? 0 : composite.hashCode());
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
		CompositePattern other = (CompositePattern) obj;
		if (component == null) {
			if (other.component != null)
				return false;
		} else if (!component.equals(other.component))
			return false;
		if (composite == null) {
			if (other.composite != null)
				return false;
		} else if (!composite.equals(other.composite))
			return false;
		return true;
	}
	@Override 
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("component: \n");
		sb.append(component.getFullName());
		sb.append("\n");
		sb.append("composite: \n");
		sb.append(composite.getFullName());
		sb.append("\n");
		sb.append("leafs: ");
		if(leafs.size()>0){
			Iterator<ClassType> it = leafs.iterator();
			sb.append("\n");
			while(it.hasNext()){
				sb.append(it.next().getFullName()+"\n");
			}
		}
//		sb.append("composite operation:\n");
//		for(Iterator itOper = compositeOperNames.iterator();itOper.hasNext();){
//			sb.append("\t"+itOper.next()+"\n");
//		}
		sb.append("\n");
		return sb.toString();
	}
	
	public Element toXML(){
		Element rootElement =PatternXmlHelper.createPattern("Composite");		
		Element componentElement = PatternXmlHelper.createRole("Component");
		Element compositeElement = PatternXmlHelper.createRole("Composite");
		Element leafElement = PatternXmlHelper.createRole("Leaf");
		
		componentElement.add(PatternXmlHelper.createClass(component.getFullName()));
		compositeElement.add(PatternXmlHelper.createClass(composite.getFullName()));
		for(ClassType c : leafs){
			leafElement.add(PatternXmlHelper.createClass(c.getFullName()));
		}
		

		rootElement.add(componentElement);
		rootElement.add(compositeElement);
		rootElement.add(leafElement);
		
		return rootElement;
	
	}
	public ClassType getComponent() {
		return component;
	}
	public ClassType getComposite(){
		return composite;
	}
	public List<ClassType> getLeafs() {
		List<ClassType> retList = new ArrayList<ClassType>();
		retList.addAll(leafs);
		return retList;
	}
	public void removeLeaf(ClassType leaf) {
		leafs.remove(leaf);
		
	}
}
