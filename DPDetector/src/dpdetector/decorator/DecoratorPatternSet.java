package dpdetector.decorator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.dom4j.Element;

import dpdetector.composite.CompositePattern;
import dpdetector.util.PatternXmlHelper;

import recoder.abstraction.ClassType;

public class DecoratorPatternSet implements Iterable<DecoratorPattern> {
	private Set<DecoratorPattern> decoratorPatternSet = new HashSet<DecoratorPattern>();
	private Set<ClassType> components = new HashSet<ClassType>();
	
	public void add(ClassType component, ClassType decorator, ClassType concreteDecorator){
		if(!components.contains(component)){
			components.add(component);
		}
		for(Iterator it = decoratorPatternSet.iterator();it.hasNext();){
			DecoratorPattern dp = (DecoratorPattern) it.next();
			if(dp.equals(new DecoratorPattern(component,decorator,null))){
				dp.addConcreteDecorator(concreteDecorator);
								
				return;
			}
		}
		decoratorPatternSet.add(new DecoratorPattern(component,decorator,concreteDecorator));
	}
	
	public boolean containsComponent(ClassType component){
		return components.contains(component);
	}
	public DecoratorPattern getPatternByComponent(ClassType component){
		for(Iterator it = decoratorPatternSet.iterator();it.hasNext();){
			DecoratorPattern dp = (DecoratorPattern) it.next();
			if(dp.getComponent().equals(component)){
				return dp;
			}
		}
		return null;
	}
	
	public void addConcreteComponent(ClassType component, ClassType concreteComponent){
		for(Iterator it = decoratorPatternSet.iterator();it.hasNext();){
			DecoratorPattern dp = (DecoratorPattern) it.next();
			if(dp.getComponent().equals(component)){
				dp.addConcreteComponent(concreteComponent);
			}
		}
	}
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append(size()+" Decorator Pattern(s) has been detected: \n\n");
		for(Iterator it = decoratorPatternSet.iterator();it.hasNext();){
			DecoratorPattern dp = (DecoratorPattern) it.next();
			sb.append(dp.toString());
			sb.append("\n");
			
		}
		return sb.toString();
	}
	
	public Element toXML(){
		Element patterns = PatternXmlHelper.createPatternRootElement(size());
		for(Iterator<DecoratorPattern> it = decoratorPatternSet.iterator();it.hasNext();){
			patterns.add(it.next().toXML());

		}
		return patterns;
	}

	public int size() {
		return decoratorPatternSet.size();
	}

	@Override
	public Iterator<DecoratorPattern> iterator() {
		return decoratorPatternSet.iterator();
	}

	public void addInstance(DecoratorPattern dp) {
		decoratorPatternSet.add(dp);
		
	}
}
