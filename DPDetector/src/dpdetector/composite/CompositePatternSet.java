package dpdetector.composite;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.dom4j.Element;

import dpdetector.adapter.AdapterPattern;
import dpdetector.util.PatternXmlHelper;

import recoder.abstraction.ClassType;

public class CompositePatternSet implements Iterable<CompositePattern> {
	private Set<CompositePattern> compositePatternSet = new HashSet<CompositePattern>();
	
	public void add(ClassType composite, ClassType component, String compositeOperName){
		if(!compositePatternSet.contains(new CompositePattern(composite,component))){
			compositePatternSet.add(new CompositePattern(composite,component,compositeOperName));
		}else{
			for(Iterator it = compositePatternSet.iterator();it.hasNext();){
				CompositePattern current = (CompositePattern) it.next();
				if(current.equals(new CompositePattern(composite,component))){
					current.addCompositeOperName(compositeOperName);
				}
			}
		}
	}
	
	
	
//	public void filterQualifiedPattern(){
//		kickCandidateWithNoLeaf();
//	}
//	
//	private void kickCandidateWithNoLeaf(){
//		Set<CompositePattern> anotherCandiSet = new HashSet<CompositePattern>();
//		for(Iterator it = compositePatternSet.iterator();it.hasNext();){
//			CompositePattern temp = (CompositePattern) it.next();
//			if(temp.hasLeaf()){
//				anotherCandiSet.add(temp);
//			}
//		}
//		compositePatternSet = anotherCandiSet;
//	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append(size()+" Composite Pattern(s) has been detected: \n");
		for(Iterator<CompositePattern> it = compositePatternSet.iterator();it.hasNext();){
			sb.append(it.next().toString());
			
		}
		return sb.toString();
	}
	
	public Element toXML(){
		Element patterns = PatternXmlHelper.createPatternRootElement(size());
		for(Iterator<CompositePattern> it = compositePatternSet.iterator();it.hasNext();){
			patterns.add(it.next().toXML());

		}
		return patterns;
	}
	

	@Override
	public Iterator<CompositePattern> iterator(){
		return compositePatternSet.iterator();
	}
	
	public int size(){
		return compositePatternSet.size();
	}



	public void addInstance(CompositePattern cp) {
		compositePatternSet.add(cp);
		
	}
}
