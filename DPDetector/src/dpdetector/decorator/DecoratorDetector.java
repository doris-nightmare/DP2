package dpdetector.decorator;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import recoder.abstraction.ClassType;
import recoder.abstraction.Constructor;
import recoder.abstraction.Method;
import recoder.abstraction.Type;
import recoder.convenience.ForestWalker;
import recoder.java.CompilationUnit;
import recoder.java.ProgramElement;
import recoder.java.declaration.ClassDeclaration;
import recoder.java.declaration.ConstructorDeclaration;
import recoder.java.declaration.FieldSpecification;
import recoder.java.declaration.InterfaceDeclaration;
import recoder.java.declaration.MethodDeclaration;
import recoder.java.declaration.ParameterDeclaration;
import recoder.java.reference.TypeReference;
import recoder.service.SourceInfo;
import dpdetector.AbstractDetector;
import dpdetector.composite.CompositePattern;
import dpdetector.composite.CompositePatternSet;
import dpdetector.util.RelationJudgeUtil;

public class DecoratorDetector extends AbstractDetector {
	private DecoratorPatternSet dpSet = new DecoratorPatternSet();

	public DecoratorDetector(List<CompilationUnit> cul, SourceInfo si) {
		super("Decorator",cul, si);
	}

	private boolean hasPublicConstructor(ClassType ct){
		for(Constructor con: ct.getConstructors()){
			if(con.isPublic()){
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void detect() {		
		long startTime;
		long endTime;
		
		startTime = Calendar.getInstance().getTimeInMillis();
		
		ForestWalker fw = new ForestWalker(cul);
		while(fw.next()){
			ClassType component,decorator,concreteDecorator,concreteComponent;
			ProgramElement pe = fw.getProgramElement();
			if(pe instanceof ClassDeclaration){
				//just assume the current class is concreteDecorator
				concreteDecorator = (ClassDeclaration) pe;
		
				// here's the algorithm using constructor
				//get the list of constructors
				List<? extends Constructor> cons = ((ClassDeclaration) pe).getConstructors();
				for(Constructor con : cons){
					MethodDeclaration md = si.getMethodDeclaration(con);
					if(md instanceof ConstructorDeclaration){					
						List<ParameterDeclaration> pdl = md.getParameters();
						if(pdl.size()>0){
							for(ParameterDeclaration pd : pdl){
								Type conParamType = si.getType(pd.getChildAt(0)) ;
								if(conParamType instanceof ClassDeclaration || conParamType instanceof InterfaceDeclaration){
									if(relationRetrivalUtil.getAllSuperTypesWithoutObject(concreteDecorator).contains(conParamType)){
										//you can find structure like this
//										public Mocha(Beverage beverage){
//											this.beverage = beverage;
//										}
										//hope this cast be correct
										component = (ClassType)  conParamType;		
										
										//next step to find the role : decorator
										List<ClassType> ctlSup = ((ClassDeclaration) pe).getSupertypes();
										for(ClassType ct : ctlSup){														
											if(ct instanceof ClassDeclaration || ct instanceof InterfaceDeclaration){															
												if(relationRetrivalUtil.getSuperTypesWithoutObject(ct).contains(component)){
													decorator = (ClassType) ct;
													if(decorator.getName().startsWith("Decorator")){
														System.out.println(decorator.getName());
													}
													//then check delegation 
													//mdl is the method list of concreteDecorator
													
													boolean isDecoratorDelegation = relationJudgeUtil.isDelegation(decorator, component);
													boolean isConcreteDecoratorDelegation = relationJudgeUtil.isDelegation(concreteDecorator, component);
													boolean isDecoratorComposition = relationJudgeUtil.isSingleComposition(decorator, component);
													boolean isConcreteDecoratorComposition = relationJudgeUtil.isSingleComposition(concreteDecorator, component);
													if(isDecoratorDelegation || isConcreteDecoratorDelegation){
														if(isDecoratorComposition || isConcreteDecoratorComposition){
															dpSet.add(component, decorator, concreteDecorator);
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
				//end of the second algorithm
				
//				//begin of third algorithm
//				List<ClassType> containedClasses = relationRetrivalUtil.getContainedClasses(concreteDecorator);
//				for(ClassType c : containedClasses){
//					component = c;
//					
//					//next step to find the role : decorator
//					List<ClassType> ctlSup = concreteDecorator.getSupertypes();
//					for(ClassType c2 : ctlSup){														
//						if(c2 instanceof ClassDeclaration){															
//							if(c2.getAllSupertypes().contains(component) && !c2.equals(component)){
//								decorator = c2;
//								dpSet.add(component, decorator, concreteDecorator);	
//							}
//						}
//					}
//				}
//				// end of the third algorithm
			
			}
				
		}
		
		//end of first while loop
		
		//find the ConcreteComponents
		//this 
		fw = new ForestWalker(cul);
		while(fw.next()){
			//check whether pe2 is concrete component
			ProgramElement pe2 = fw.getProgramElement();
			if(pe2 instanceof ClassDeclaration){
				if(((ClassDeclaration) pe2).isAbstract() == false){
					List<ClassType> supCtl = relationRetrivalUtil.getAllSuperTypesWithoutObject((ClassType) pe2);
					
					for(DecoratorPattern dp : dpSet){
						ClassType component = dp.getComponent();
						ClassType decorator = dp.getDecorator();
						if(supCtl.contains(component) && !supCtl.contains(decorator) && !((ClassType)pe2).equals(decorator)){
							dp.addConcreteComponent((ClassType) pe2);
						}
						
					}
					
//					for(ClassType supCt : supCtl){
//						if(dpSet.containsComponent(supCt)){
//							DecoratorPattern dp = dpSet.getPatternByComponent(supCt);
//							
//							
//							if(!relationJudgeUtil.isDelegation((ClassType) pe2,supCt)  && ((ClassType) pe2).isPublic() && hasPublicConstructor((ClassType) pe2)){
//								dpSet.addConcreteComponent(supCt, (ClassType) pe2);
//							}
//							
//						}						
//					}
				}
			}
		}
		
		//end of second while loop
		
		endTime = Calendar.getInstance().getTimeInMillis();
		detectTime = (int) (endTime - startTime);

	}

	@Override
	public void eliminate() {
		
		long startTime;
		long endTime;
		
		startTime = Calendar.getInstance().getTimeInMillis();
		
		DecoratorPatternSet anotherSet = new DecoratorPatternSet();
		for(DecoratorPattern dp : dpSet){
			ClassType component = dp.getComponent();
			ClassType decorator = dp.getDecorator();
			List<ClassType> concreteComponents = dp.getConcreteComponents();
			List<ClassType> concreteDecorators = dp.getConcreteDecorators();
			
			if(component.isPublic() && !component.isInner() && decorator.isPublic() && ! decorator.isInner()){
				if(concreteComponents.size()>0){
					for(ClassType concreteComponent : concreteComponents){
						if(!concreteComponent.isPublic() || concreteComponent.isInner()){
							dp.removeConcreteComponent(concreteComponent);
						}
					}
				}
				
				if(concreteDecorators.size()>0){
					for(ClassType concreteDecorator : concreteDecorators){
						if(!concreteDecorator.isPublic() || concreteDecorator.isInner()){
							dp.removeConcreteDecorator(concreteDecorator);
						}
					}
				}
				
				if(dp.hasConcreteDecorator() && dp.hasConcreteComponent()){
					
					anotherSet.addInstance(dp);
					if(!dp.getComponent().getName().equals("Object") && !dp.getComponent().getName().equals("Throwable") && !dp.getComponent().getName().equals("Exception")){
						
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
