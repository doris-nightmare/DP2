package dpdetector.test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import recoder.CrossReferenceServiceConfiguration;
import recoder.ParserException;
import recoder.convenience.ForestWalker;
import recoder.io.DefaultProjectFileIO;
import recoder.io.SourceFileRepository;
import recoder.java.CompilationUnit;
import recoder.java.ProgramElement;
import recoder.java.declaration.ClassDeclaration;
import recoder.java.declaration.MethodDeclaration;
import recoder.service.NameInfo;
import recoder.service.SourceInfo;
import dpdetector.DetectorManager;
import dpdetector.DetectorManager.DetectorEnum;
import dpdetector.test.helper.PathHelper;
import junit.framework.TestCase;

public class StatisticTest extends TestCase {

	public void testJHotDraw(){
		CrossReferenceServiceConfiguration crsc = new CrossReferenceServiceConfiguration();
		//File inputFile = new File(PathHelper.getTestProject());
		File inputFile = new File(PathHelper.getTestDir()+"JHotDraw7.0.6\\jhotdraw.prj");
		//File inputFile = new File(PathHelper.getTestDir()+"junit4.10\\junit.prj");

		try {
			String[] files = new DefaultProjectFileIO(crsc,inputFile).load();
	    	SourceFileRepository sfr = crsc.getSourceFileRepository();
	    	crsc.getProjectSettings().ensureExtensionClassesAreInPath();
	    	List<CompilationUnit> cul = sfr.getAllCompilationUnitsFromPath();
	    	SourceInfo si = crsc.getSourceInfo();
	    	NameInfo ni = crsc.getNameInfo();
	    	
	    	crsc.getChangeHistory().updateModel();
	    	
	    	int numOfClass = 0;
	    	int numOfMethod = 0;
	    	ForestWalker fw = new ForestWalker(cul);
	    	while(fw.next()){
	    		ProgramElement pe = fw.getProgramElement();
	    		if(pe instanceof ClassDeclaration){
	    			//System.out.println(((ClassDeclaration) pe).getFullName());
	    			numOfClass++;
	    		}
	    		if(pe instanceof MethodDeclaration){
	    			numOfMethod ++;
	    		}
	    	}
	    	System.out.println("Num Of Classes: " + numOfClass);
	    	System.out.println("Num Of Methods: " + numOfMethod);
	    	
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserException e) {
			e.printStackTrace();
		}
	}
}
