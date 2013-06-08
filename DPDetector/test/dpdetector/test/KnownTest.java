package dpdetector.test;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

import recoder.CrossReferenceServiceConfiguration;
import recoder.ParserException;
import recoder.abstraction.ClassType;
import recoder.abstraction.Method;
import recoder.convenience.ForestWalker;
import recoder.io.DefaultProjectFileIO;
import recoder.io.PropertyNames;
import recoder.io.SourceFileRepository;
import recoder.java.CompilationUnit;
import recoder.java.ProgramElement;
import recoder.java.declaration.ClassDeclaration;
import recoder.java.declaration.InterfaceDeclaration;
import recoder.service.NameInfo;
import recoder.service.SourceInfo;
import dpdetector.composite.CompositeDetector;
import dpdetector.test.helper.PathHelper;
import dpdetector.util.RelationJudgeUtil;
import dpdetector.util.RelationRetrivalUtil;

public class KnownTest extends TestCase {
	public void testA(){
		CrossReferenceServiceConfiguration crsc = new CrossReferenceServiceConfiguration();
		//File inputFile = new File(PathHelper.getTestProject());
		//File inputFile = new File(PathHelper.getTestDir()+"JHotDraw7.0.6\\jhotdraw.prj");
		//File inputFile = new File(PathHelper.getTestDir()+"junit4.10\\junit.prj");
		//File inputFile = new File(PathHelper.getTestDir()+"JDK\\jdk.prj");
		//File inputFile = new File(PathHelper.getTestDir()+"J2SE5\\J2SE5.prj");
		//File inputFile = new File(PathHelper.getTestDir()+"JHotDraw5.1\\jhotdraw.prj");
		File inputFile = new File(PathHelper.getTestDir()+"jhotdraw60b1\\jhotdraw.prj");

		try {
			String[] files = new DefaultProjectFileIO(crsc,inputFile).load();
	    	SourceFileRepository sfr = crsc.getSourceFileRepository();
	    	crsc.getProjectSettings().ensureExtensionClassesAreInPath();
	    	List<CompilationUnit> cul = sfr.getAllCompilationUnitsFromPath();
	    	SourceInfo si = crsc.getSourceInfo();
	    	NameInfo ni = crsc.getNameInfo();
	    	
	    	crsc.getChangeHistory().updateModel();
	    	int num = 0;
	    	
	    	Set<ClassType> classes = new HashSet<ClassType>();
	    	ForestWalker fw = new ForestWalker(cul);
	    	System.out.println(cul.size());
	    	while(fw.next()){
	    		ProgramElement pe = fw.getProgramElement();
	    		if(pe instanceof ClassDeclaration || pe instanceof InterfaceDeclaration){
	    			classes.add((ClassType) pe);
	    			num++;
	    		}
	    	}
	    	System.out.println(num);
	    	System.out.println(classes.size());

	    	
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserException e) {
			e.printStackTrace();
		}
	}
}
