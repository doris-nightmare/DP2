package dpdetector.test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import dpdetector.DetectorManager;
import dpdetector.test.helper.PathHelper;
import dpdetector.util.RelationRetrivalUtil;
import dpdetector.util.RelationJudgeUtil;

import recoder.CrossReferenceServiceConfiguration;
import recoder.ParserException;
import recoder.abstraction.ClassType;
import recoder.abstraction.Method;
import recoder.abstraction.Type;
import recoder.convenience.ForestWalker;
import recoder.convenience.TreeWalker;
import recoder.io.DefaultProjectFileIO;
import recoder.io.PropertyNames;
import recoder.io.SourceFileRepository;
import recoder.java.CompilationUnit;
import recoder.java.ProgramElement;
import recoder.java.declaration.ClassDeclaration;
import recoder.java.declaration.ParameterDeclaration;
import recoder.java.declaration.VariableDeclaration;
import recoder.java.expression.Assignment;
import recoder.java.expression.operator.BinaryAndAssignment;
import recoder.java.expression.operator.CopyAssignment;
import recoder.java.expression.operator.New;
import recoder.service.NameInfo;
import recoder.service.SourceInfo;
import junit.framework.TestCase;

public class NormalTest extends TestCase {
	public void testNormal(){
		CrossReferenceServiceConfiguration crsc = new CrossReferenceServiceConfiguration();
		//File inputFile = new File(PathHelper.getTestProject());
		//File inputFile = new File(PathHelper.getTestDir()+"JHotDraw7.0.6\\jhotdraw.prj");
		//File inputFile = new File(PathHelper.getTestDir()+"junit4.10\\junit.prj");
		//File inputFile = new File(PathHelper.getTestDir()+"JDK\\jdk.prj");
		File inputFile = new File(PathHelper.getTestDir()+"jhotdraw60b1\\jhotdraw.prj");

		try {
			String[] files = new DefaultProjectFileIO(crsc,inputFile).load();
	    	SourceFileRepository sfr = crsc.getSourceFileRepository();
	    	crsc.getProjectSettings().ensureExtensionClassesAreInPath();
	    	List<CompilationUnit> cul = sfr.getAllCompilationUnitsFromPath();
	    	SourceInfo si = crsc.getSourceInfo();
	    	NameInfo ni = crsc.getNameInfo();

	    	
	    	crsc.getChangeHistory().updateModel();
	    	
	    	
	    	TreeWalker tw = new TreeWalker(cul.get(0));
	    	while(tw.next()){
	    		ProgramElement pe = tw.getProgramElement();
	    		System.out.println(pe);
	    	}
	    	
//	    	ForestWalker fw = new ForestWalker(cul);
//	    	while(fw.next()){
//	    		ProgramElement pe = fw.getProgramElement();
//	    		if(pe instanceof ClassDeclaration){
//	    			List<ClassType> superTypes = helper.getSuperTypesWithoutObject((ClassType) pe);
//	    			List<Method> mdl = ((ClassDeclaration) pe).getMethods();
//	    			for(Method m : mdl){
//	    				for(ClassType sup : superTypes){
//	    					if(util.isOverrideMethod(sup, m)){
//	    						List<ClassType> delegateeClasses = helper.getDelegateeClasses(m);
//	    						if(delegateeClasses.size()>0){
//	    							for(ClassType ct : delegateeClasses){
//	    								System.out.println("Delegate Method: "+m.getFullName());
//	    								System.out.println("Target:"+sup.getName()+" Adapter:" + ((ClassType) pe).getName()+" Adaptee:"+ct.getName());
//	    								System.out.println(" ");
//	    							}
//	    							
//	    						}
//	    						
//	    					}
//	    				}
//	    			}
//	    			
//	    			
//	    			
//	    		}
//	    	}

	    	
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserException e) {
			e.printStackTrace();
		}


	}
	
}
