package dpdetector.adapter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import dpdetector.AbstractDetector;

import recoder.abstraction.ClassType;
import recoder.abstraction.Constructor;
import recoder.abstraction.Field;
import recoder.abstraction.Method;
import recoder.abstraction.Type;
import recoder.convenience.ForestWalker;
import recoder.convenience.TreeWalker;
import recoder.java.CompilationUnit;
import recoder.java.Import;
import recoder.java.ProgramElement;
import recoder.java.declaration.ClassDeclaration;
import recoder.java.declaration.ConstructorDeclaration;
import recoder.java.declaration.Extends;
import recoder.java.declaration.Implements;
import recoder.java.declaration.MethodDeclaration;
import recoder.java.declaration.ParameterDeclaration;
import recoder.java.declaration.TypeDeclaration;
import recoder.java.declaration.VariableDeclaration;
import recoder.java.expression.operator.New;
import recoder.java.reference.MethodReference;
import recoder.java.reference.PackageReference;
import recoder.java.reference.TypeReference;
import recoder.java.reference.VariableReference;
import recoder.service.SourceInfo;

public class AdapterDetector extends AbstractDetector {
	private AdapterPatternSet dpSet = new AdapterPatternSet();
	
	public AdapterDetector(List<CompilationUnit> cul, SourceInfo si) {
		super("Adapter",cul, si);
	}

	private boolean hasInheritRelationship(ClassType a, ClassType b) {
		if (a.getAllSupertypes().contains(b)
				|| b.getAllSupertypes().contains(a)) {
			return true;
		}
		return false;
	}

	private boolean hasRefToTarget(CompilationUnit cu, AdapterPattern ap) {
		List<Import> imports = cu.getImports();
		for (Import impo : imports) {
			ProgramElement pe = impo.getChildAt(0);
			if (pe != null) {
				if (pe instanceof TypeReference) {
					if (si.getType(pe).getFullName()
							.equals(ap.getTargetFullName())) {
						return true;
					}
				} else if (pe instanceof PackageReference) {
					if (si.getPackage((PackageReference) pe).getFullName()
							.equals(ap.getTargetFullPkgName())) {
						return true;
					}
				}

			}
		}
		return false;

	}

	private boolean isClient(AdapterPattern ap, ClassDeclaration candiClient) {
		TreeWalker tw = new TreeWalker(candiClient);
		while (tw.next()) {
			ProgramElement pe = tw.getProgramElement();
			if (pe instanceof VariableDeclaration) {
				TreeWalker subTw = new TreeWalker(pe);
				while (subTw.next()) {
					ProgramElement subPe = subTw.getProgramElement();
					if (subPe instanceof New) {

						Type target = si.getType(((VariableDeclaration) pe)
								.getTypeReference());
						Type adapter = si.getType(((New) subPe)
								.getTypeReference());
						if (target instanceof ClassType
								&& adapter instanceof ClassType) {
							// find out the adapter, adaptee, target in
							// client
							if (ap.equalsTarget((ClassType) target)
									&& ap.equalsAdapter((ClassType) adapter)) {
//								System.out.println(target.getFullName());
//								System.out.println(adapter.getFullName());

								return true;
							}
						}

					}
				}
			}
		}
		return false;
	}

	public void detect() {
		long startTime;
		long endTime;
		
		startTime = Calendar.getInstance().getTimeInMillis();
		
    	ForestWalker fw = new ForestWalker(cul);
    	while(fw.next()){
    		ClassType adapter, adaptee, target;
    		ProgramElement pe = fw.getProgramElement();
    		if(pe instanceof ClassDeclaration){
    			adapter = (ClassType) pe;
    			List<ClassType> superTypes = relationRetrivalUtil.getSuperTypesWithoutObject((ClassType) pe);
    			List<Method> mdl = adapter.getMethods();
    			for(Method m : mdl){
    				for(ClassType sup : superTypes){
    					target = sup;
    					if(relationJudgeUtil.isOverrideMethod(sup, m)){
    						List<ClassType> delegateeClasses = relationRetrivalUtil.getDelegateeClasses(m);
    						//it is important
    						delegateeClasses.remove(sup);
    						if(delegateeClasses.size()>0){
    							for(ClassType ct : delegateeClasses){
    								if(!relationJudgeUtil.isParameter(ct, m)){
    									adaptee = ct;
        								
        								 if(relationJudgeUtil.isSubClass(adapter, adaptee) || (relationJudgeUtil.isSingleComposition(adapter, adaptee) && relationJudgeUtil.hasCopyConstructor(adapter,adaptee)) ){
        									 dpSet.addInstance(adapter, adaptee, target);
        								 }
        								
        							}
        							
    								}
    				
    						}
    						
    					}
    				}
    			}	
    		}
    	}
    	
//    	//try another way
//    	fw = new ForestWalker(cul);
//    	while(fw.next()){
//    		ClassType adapter, adaptee, target;
//    		ProgramElement pe = fw.getProgramElement();
//    		if(pe instanceof ClassDeclaration){
//    			//first - object adapter
//    			adapter = (ClassType) pe;
//    			List<ClassType> containedClasses = relationRetrivalUtil.getContainedClasses(adapter);
//    			List<ClassType> superTypes = relationRetrivalUtil.getSuperTypesWithoutObject((ClassType) pe);
//    			List<Method> mdl = adapter.getMethods();
//    			for(ClassType sup : superTypes){
//					target = sup;
//					for(Method m : mdl){
//	    				for(ClassType c : containedClasses){
//	    					if(relationJudgeUtil.isOverrideMethod(sup, m) && relationJudgeUtil.isDelegationMethod(m,c) && !superTypes.contains(c)){
//	    						adaptee = c;
//	    						 objectAdapterSet.addInstance(adapter, adaptee, target);
//	    					}
//	    				}
//	    				
//	    			}
//    			}
//    			
//    		}
//    	}
//    	//end of try
    	
		// add another phase : to detect client role
		for (CompilationUnit cu : cul) {
			// check import first, to reduce the search space
			for (Iterator it = dpSet.iterator(); it.hasNext();) {
				AdapterPattern apCurrent = (AdapterPattern) it.next();
				if (hasRefToTarget(cu, apCurrent)) {
					TreeWalker tw = new TreeWalker(cu);
					while (tw.next()) {
						ProgramElement pe = tw.getProgramElement();
						if (pe instanceof ClassDeclaration
								&& ((ClassDeclaration) pe).isPublic()
								&& !((ClassDeclaration) pe).isInner()) {
							if (isClient(apCurrent, (ClassDeclaration) pe)) {
								apCurrent.addClient((ClassType) pe);
							}
						}
					}
				}
			}

		}
		
		endTime = Calendar.getInstance().getTimeInMillis();
		detectTime = (int) (endTime - startTime);

	}

	@Override
	public void eliminate() {
		long startTime;
		long endTime;
		
		startTime = Calendar.getInstance().getTimeInMillis();
		
		
		AdapterPatternSet anotherDpSet = new AdapterPatternSet();
		for(Iterator<AdapterPattern> it = dpSet.iterator();it.hasNext();){
			AdapterPattern ap = it.next();
			//check relation between target &¡¡adaptee
			//check public
			//check composition or inheritance
			if(	ap.getTarget().isPublic() && ap.getAdaptee().isPublic() && ap.getAdapter().isPublic()){
				
				//check have no inheritance relationship
				if(!(relationJudgeUtil.isSubClass(ap.getTarget(), ap.getAdaptee()) ||
					relationJudgeUtil.isSubClass(ap.getAdaptee(),ap.getTarget()) ||
					relationJudgeUtil.isComposition(ap.getTarget(), ap.getAdaptee()) ||
					relationJudgeUtil.isComposition(ap.getAdaptee(), ap.getTarget()) ||
					ap.getAdaptee().equals(ap.getTarget())) 
					){
					
					
					
					if(relationRetrivalUtil.getAllSuperTypesWithoutObject(ap.getAdaptee()).size()>0 ||
							ap.getAdaptee().isAbstract()){
						if(!relationJudgeUtil.isComposition(ap.getAdaptee(), ap.getAdapter().getAllSupertypes())){
						
							anotherDpSet.add(ap);
//							if(!ap.getAdaptee().getName().endsWith("Factory")){
//								
//							}
//							
						}
						
						
					}
					
					
				}				
			}
		}
		
		dpSet = anotherDpSet;
		//normal end........................................................
		
//		AdapterPatternSet anotherObjectDpSet = new AdapterPatternSet();
//		for(Iterator<AdapterPattern> it = objectAdapterSet.iterator();it.hasNext();){
//			AdapterPattern ap = it.next();
//			//check relation between target &¡¡adaptee
//			//check public
//			//check composition or inheritance
//			if(	ap.getTarget().isPublic() && ap.getAdaptee().isPublic() && ap.getAdapter().isPublic()){
//				
//				//check have no inheritance relationship
//				if(!(relationJudgeUtil.isSubClass(ap.getTarget(), ap.getAdaptee()) ||
//					relationJudgeUtil.isSubClass(ap.getAdaptee(),ap.getTarget()) ||
//					ap.getAdaptee().equals(ap.getTarget())) 
//					){
//					
//					
//					
//					if(relationRetrivalUtil.getAllSuperTypesWithoutObject(ap.getAdaptee()).size()>0 ||
//							ap.getAdaptee().isAbstract()){
//						anotherObjectDpSet.add(ap);
//					}
//					
//					
////					if(!(relationJudgeUtil.isComposition(ap.getTarget(), ap.getAdaptee()) ||
////							relationJudgeUtil.isComposition(ap.getAdaptee(), ap.getTarget()) ||
////							relationJudgeUtil.isCompositionSuperTypes(ap.getTarget(), ap.getAdaptee()) ||
////							relationJudgeUtil.isCompositionSuperTypes(ap.getAdaptee(), ap.getTarget()) ) ){
////						
////					}
//					
//					
//					if(ap.getTarget().isAbstract() || ap.getTarget().isInterface()){					
//						if(!ap.getTarget().getName().contains("Listener")){
//							
//						}
//						
//						
//					}
//				}				
//			}
//		}
//		
//		objectAdapterSet = anotherObjectDpSet;
		
		endTime = Calendar.getInstance().getTimeInMillis();
		eliminateTime = (int) (endTime - startTime);
	}
	
	
	public void output(){
//		for(Iterator<AdapterPattern> it = dpSet.iterator();it.hasNext();){
//			AdapterPattern ap = it.next();
//			if(ap.hasClient()){
//				System.out.println(ap);
//			}
//		}
		
		System.out.println(dpSet);
		System.out.println(dpSet.size());
		
		try {
			OutputFormat format = OutputFormat.createPrettyPrint();
			XMLWriter writer = new XMLWriter(System.out,format);
			writer.write(dpSet.toXML());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		
//		System.out.println(objectAdapterSet);
//		System.out.println(objectAdapterSet.size());
	}

	@Override
	public String toString() {
		return dpSet.toString();
	}

	@Override
	public Element toXML() {
		return dpSet.toXML();
	}

	@Override
	public String getPatternName() {
		return name;
		
	}

	@Override
	public int size() {
		return dpSet.size();
	}
}
