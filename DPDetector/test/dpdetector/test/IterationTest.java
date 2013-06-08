package dpdetector.test;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Stack;

import recoder.CrossReferenceServiceConfiguration;
import recoder.ParserException;
import recoder.convenience.ForestWalker;
import recoder.io.DefaultProjectFileIO;
import recoder.io.PropertyNames;
import recoder.io.SourceFileRepository;
import recoder.java.CompilationUnit;
import recoder.java.ProgramElement;
import recoder.java.declaration.ClassDeclaration;
import recoder.service.SourceInfo;
import dpdetector.composite.CompositeDetector;
import junit.framework.TestCase;

public class IterationTest extends TestCase {
	Stack<Integer> childNumStack = new Stack<Integer>();
	Stack<Integer> iStack = new Stack<Integer>();
	int i;
	int childNum;
	
	public boolean traverseClass(ProgramElement pe){

		if(pe instanceof ClassDeclaration){
			childNumStack.push(childNum);
			iStack.push(i);
			childNum = ((ClassDeclaration) pe).getChildCount();
			if(childNum == 0){
				return false;
			}
			for(i=0;i<childNum;i++){
				
			}
			i= iStack.pop();
			childNum = childNumStack.pop();
		}
		return true;
	}
	public void testIteration(){
		CrossReferenceServiceConfiguration crsc = new CrossReferenceServiceConfiguration();
		//File inputFile = new File("C:\\Users\\xiaoyang\\Documents\\DorisHere\\GraduationProject\\JHotDraw7.0.6\\jhotdraw.prj");
		try {
			//String[] files = new DefaultProjectFileIO(crsc,inputFile).load();
			crsc.getProjectSettings().setProperty(PropertyNames.INPUT_PATH, "C:\\Users\\xiaoyang\\Documents\\DorisHere\\workspace\\gp\\DPDetector\\test\\dpdetector\\teststuff");
	    	SourceFileRepository sfr = crsc.getSourceFileRepository();
	    	List<CompilationUnit> cul = sfr.getAllCompilationUnitsFromPath();
	    	SourceInfo si = crsc.getSourceInfo();
	    	
	    	ForestWalker fw = new ForestWalker(cul);
	    	while(fw.next()){
	    		//System.out.println(fw.getProgramElement().getClass().getName());
	    	}
	    	

	    	ProgramElement pe;
	    	for(CompilationUnit cu : cul){
	    		childNum = cu.getChildCount();
	    		for(i=0;i<childNum;i++){
	    			pe = cu.getChildAt(i);
	    			System.out.println(pe.getClass().getName());
	    			
	    			
	    			
	    			
	    		}
	    	}

	    	
		} 
//		catch (IOException e) {
//			e.printStackTrace();
//		}
		catch (ParserException e) {
			e.printStackTrace();
		}
	}
}
