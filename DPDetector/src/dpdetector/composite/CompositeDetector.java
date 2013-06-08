package dpdetector.composite;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
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
import recoder.abstraction.Method;
import recoder.abstraction.Type;
import recoder.convenience.ForestWalker;
import recoder.convenience.TreeWalker;
import recoder.java.CompilationUnit;
import recoder.java.ProgramElement;
import recoder.java.declaration.ClassDeclaration;
import recoder.java.declaration.ConstructorDeclaration;
import recoder.java.declaration.MethodDeclaration;
import recoder.java.declaration.ParameterDeclaration;
import recoder.java.reference.MethodReference;
import recoder.java.reference.TypeReference;
import recoder.service.SourceInfo;

public class CompositeDetector extends AbstractDetector {

	private CompositePatternSet dpSet = new CompositePatternSet();
	
	public CompositeDetector(List<CompilationUnit> cul, SourceInfo si) {
		super("Composite",cul, si);
	}

	boolean isLeaf(CompositePattern cp, ClassType candidateLeaf){
		//it seems relationUtil's method does not work . the hasAddComponentOper is useful
		if(cp.isCompositeOperMatch(candidateLeaf) && !relationJudgeUtil.isComposition(candidateLeaf, cp.getComponent())){
			if(!hasAddComponentOper(candidateLeaf, cp.getComponent())){
				return true;
			}
			
		}
		return false;
	}
	
	boolean isCompositeOperation(Method m1, Method m2){
		//m1 in composite, m1 has delegation to m2
		//m2 in component
		if(m1.getName().equals(m2.getName())){
			MethodDeclaration md = si.getMethodDeclaration(m1);
			ClassType m2C = m2.getContainingClassType();
			
			//check m1's parameter list do not contain component
			if(md.getParameters().size()>0){
				List<ParameterDeclaration> pdl = md.getParameters();
				for(ParameterDeclaration pd : pdl){
					if(pd.getChildAt(0) instanceof TypeReference){
						if(si.getType(pd.getChildAt(0)).equals(m2C)){
							return false;
						}
					}
				}
				
				
			}
			//traverse the statement block of method????
			for(int i=0;i<md.getChildCount();i++){
				TreeWalker tw = new TreeWalker(md.getChildAt(i));
				while(tw.next()){
					ProgramElement pe = tw.getProgramElement();
					//System.out.println(pe.getClass().getName());
					if(pe instanceof MethodReference){
						//System.out.println(((MethodReference) pe).getName());
						Method m = si.getMethod((MethodReference) pe);
						ClassType ct = m.getContainingClassType();
						
						if(ct.equals(m2C)){
							return true;
						}
						
					}
				}
			}
			
		}	
		return false;
	}
	
	public boolean hasAddComponentOper(ClassType composite, ClassType component){
		for(Method m : composite.getMethods()){
			MethodDeclaration md = si.getMethodDeclaration(m);
			if(md.getName().startsWith("add")){
				for(ParameterDeclaration pd:md.getParameters()){
					if(pd.getChildAt(0) instanceof TypeReference){
						if(si.getType(pd.getChildAt(0)).equals(component)){
							return true;
						}
					}
				}
			}
			
		}
		return false;
	}
	
	boolean isAddComponent(Method m1){
		String name = m1.getName();
		//System.out.println(name);
		if(name.startsWith("add")){
			return true;
		}
//		if(m1.getTypeParameters()!=null){
//			if(m1.getTypeParameters().get(0) instanceof ClassType){
//				return true;
//			}
//		}
		
		return false;
	}

	
	public void detect(){
		long startTime;
		long endTime;
		
		startTime = Calendar.getInstance().getTimeInMillis();
		
		for(CompilationUnit cu : cul){
    		TreeWalker tw = new TreeWalker(cu);  		
    		while(tw.next()){
    			 ProgramElement pe = tw.getProgramElement();
    			 if(pe instanceof MethodDeclaration && !(pe instanceof ConstructorDeclaration)){
    				 ClassType currentClass =si.getContainingClassType(pe);
    				//System.out.println(((MethodDeclaration) pe).getFullName());
    				 List<ParameterDeclaration> params = ((MethodDeclaration)pe).getParameters();
    				 if(params != null){
    					 for(ParameterDeclaration pa : params){
        					 ProgramElement tmpPe = pa.getChildAt(0);
        					 if(tmpPe instanceof TypeReference){
        						 Type tp = si.getType(tmpPe);
        						 if(tp instanceof ClassType ){							
        							 if(relationJudgeUtil.isSubClass(currentClass,(ClassType) tp)){    								 
        								//currentClass is composite, tp is component
        								 for(Method m1:currentClass.getMethods()){
        									 for(Method m2 : ((ClassType) tp).getMethods()){        										
        										 if(isCompositeOperation(m1,m2)){
        											 if(hasAddComponentOper(currentClass, (ClassType)tp)){
        												 dpSet.add(currentClass,(ClassType) tp,m1.getName());
        											 }
        											 
        										 }
        									 }
        								 }
        							 }
        							 
        							 
        						 }
        					 }
        				 }
    				 }
    				 
    			 } 						
    		}		 
		}
		//end of phase one
		
		//try another detect algorithm
		for(CompilationUnit cu : cul){
			TreeWalker tw  = new TreeWalker(cu);
			ClassType component, composite;
			while(tw.next()){
				ProgramElement pe = tw.getProgramElement();
				if(pe instanceof ClassDeclaration){
					composite = (ClassType) pe;
					List<ClassType> supCl = ((ClassDeclaration) pe).getSupertypes();
					for(ClassType supC : supCl){
						if(hasAddComponentOper(composite,supC)){
							for(Method m1 : composite.getMethods()){
								for(Method m2 : supC.getMethods()){
									 if(isCompositeOperation(m1,m2)){
										// dpSet.add(composite,supC,m1.getName());									 
										 
									 }
								}
							}
						
						}
					}
				}
			}
		}
		// end of another algorithm
		 
		
		 
		 
//		 Iterator<CompositePhaseTwoTuple> itFinal = phaseTwoList.iterator();
//	
//		 while(itFinal.hasNext()){
//			 CompositePhaseTwoTuple finalTuple = itFinal.next();
//			 System.out.println(finalTuple);
//		 }
		 
		//another phase two --- for new algorithm
		 
		ForestWalker fw = new ForestWalker(cul);
		while(fw.next()){
			ProgramElement pe2 = fw.getProgramElement();
			if(pe2 instanceof ClassDeclaration){
				ClassType ctp =  (ClassType) si.getType(pe2);
				if(relationJudgeUtil.isSubClass(ctp)){
					Iterator<CompositePattern> itCan = dpSet.iterator();
					 while(itCan.hasNext()){
						CompositePattern tempcan = itCan.next(); 
						if(relationJudgeUtil.isSubClass(ctp,tempcan.component) && (!ctp.isAbstract()) && (!ctp.isInterface()) && (!ctp.equals(tempcan.composite)) && (!relationJudgeUtil.isSubClass(ctp,tempcan.composite)) &&(!relationJudgeUtil.isSubClass(tempcan.composite,ctp)) ){
							if(isLeaf(tempcan,ctp)){
								tempcan.addLeaf(ctp);
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
		
		CompositePatternSet anotherSet = new CompositePatternSet();
		for(CompositePattern cp : dpSet){
			ClassType component = cp.getComponent();
			ClassType composite = cp.getComposite();
			List<ClassType> leafs = cp.getLeafs();
			if(leafs.size()>0){
				if(component.isPublic() && composite.isPublic() ){
					for(ClassType leaf : leafs){
						if(!leaf.isPublic()  ){
							cp.removeLeaf(leaf);
						}
					}
					if(cp.hasLeaf()){
						anotherSet.addInstance(cp);
					}
					
				}
			}
			
		}
		dpSet = anotherSet;
		
		endTime = Calendar.getInstance().getTimeInMillis();
		eliminateTime = (int) (endTime - startTime);
		
	}


	public void output() {
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
