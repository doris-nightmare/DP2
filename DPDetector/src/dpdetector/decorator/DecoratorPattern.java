package dpdetector.decorator;

import recoder.abstraction.ClassType;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.Iterator;

import org.dom4j.Element;

import dpdetector.util.PatternXmlHelper;

public class DecoratorPattern {
	public ClassType component,decorator;
	public Set<ClassType> concreteComponents, concreteDecorators;
	
	public DecoratorPattern(ClassType component, ClassType decorator, ClassType concreteDecorator){
		concreteComponents = new HashSet<ClassType>();
		concreteDecorators = new HashSet<ClassType>();
		
		this.component = component;
		this.decorator = decorator;
		concreteDecorators.add(concreteDecorator);
	}
	

	
	

	public ClassType getComponent() {
		return component;
	}

	public void setComponent(ClassType component) {
		this.component = component;
	}

	public ClassType getDecorator() {
		return decorator;
	}

	public void setDecorator(ClassType decorator) {
		this.decorator = decorator;
	}

	public void addConcreteDecorator(ClassType ct){
		concreteDecorators.add(ct);
	}
	
	public void addConcreteComponent(ClassType ct){
		concreteComponents.add(ct);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((component == null) ? 0 : component.hashCode());
		result = prime * result
				+ ((decorator == null) ? 0 : decorator.hashCode());
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
		DecoratorPattern other = (DecoratorPattern) obj;
		if (component == null) {
			if (other.component != null)
				return false;
		} else if (!component.equals(other.component))
			return false;
		if (decorator == null) {
			if (other.decorator != null)
				return false;
		} else if (!decorator.equals(other.decorator))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("Component: \n\t" + component.getFullName() +"\n");
		sb.append("Concrete Component(s): \n");
		for(Iterator it = concreteComponents.iterator();it.hasNext();){
			sb.append("	"+((ClassType)it.next()).getFullName() + "\n");
		}
		sb.append("Decorator: \n\t" + decorator.getFullName() +"\n");
		sb.append("Concrete Decorator(s): \n");
		for(Iterator it = concreteDecorators.iterator();it.hasNext();){
			sb.append("	"+((ClassType)it.next()).getFullName() + "\n");
		}
		
		return sb.toString();
	}
	
	public Element toXML(){
		Element rootElement =PatternXmlHelper.createPattern("Decorator");	
		
		Element componentElement = PatternXmlHelper.createRole("Component");
		Element decoratorElement = PatternXmlHelper.createRole("Decorator");
		Element concreteComponentElement  = PatternXmlHelper.createRole("ConcreteComponent");
		Element concreteDecoratorElement = PatternXmlHelper.createRole("ConcreteDecoarator");
		
		
		componentElement.add(PatternXmlHelper.createClass(component.getFullName()));
		decoratorElement.add(PatternXmlHelper.createClass(decorator.getFullName()));
		for(ClassType c : concreteComponents){
			concreteComponentElement.add(PatternXmlHelper.createClass(c.getFullName()));
		}
		for(ClassType c : concreteDecorators){
			concreteDecoratorElement.add(PatternXmlHelper.createClass(c.getFullName()));
		}
		

		rootElement.add(componentElement);
		rootElement.add(decoratorElement);
		rootElement.add(concreteComponentElement);
		rootElement.add(concreteDecoratorElement);
		
		return rootElement;
	
	}





	public List<ClassType> getConcreteComponents() {
		List<ClassType> retList = new ArrayList<ClassType>();
		retList.addAll(concreteComponents);
		return retList;
	}





	public List<ClassType> getConcreteDecorators() {
		List<ClassType> retList = new ArrayList<ClassType>();
		retList.addAll(concreteDecorators);
		return retList;
	}





	public void removeConcreteComponent(ClassType concreteComponent) {
		concreteComponents.remove(concreteComponent);
		
	}
	
	public void removeConcreteDecorator(ClassType concreteDecorator){
		concreteDecorators.remove(concreteDecorator);
	}





	public boolean hasConcreteDecorator() {
		if(concreteDecorators.size()>0){
			return true;
		}
		return false;
	}





	public boolean hasConcreteComponent() {
		if(concreteComponents.size()>0){
			return true;
		}
		return false;
	}
	
	
}
