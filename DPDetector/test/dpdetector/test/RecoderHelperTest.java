package dpdetector.test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import recoder.CrossReferenceServiceConfiguration;
import recoder.ParserException;
import recoder.abstraction.ClassType;
import recoder.abstraction.Method;
import recoder.convenience.ForestWalker;
import recoder.io.DefaultProjectFileIO;
import recoder.io.SourceFileRepository;
import recoder.java.CompilationUnit;
import recoder.java.ProgramElement;
import recoder.java.declaration.ClassDeclaration;
import recoder.service.NameInfo;
import recoder.service.SourceInfo;
import dpdetector.test.helper.PathHelper;
import dpdetector.util.RelationJudgeUtil;
import dpdetector.util.RelationRetrivalUtil;
import junit.framework.TestCase;

public class RecoderHelperTest extends TestCase {
	public void testDelegatee(){
		CrossReferenceServiceConfiguration crsc = new CrossReferenceServiceConfiguration();
		File inputFile = new File(PathHelper.getTestProject());
		//File inputFile = new File(PathHelper.getTestDir()+"JHotDraw7.0.6\\jhotdraw.prj");
		//File inputFile = new File(PathHelper.getTestDir()+"junit4.10\\junit.prj");
		//File inputFile = new File(PathHelper.getTestDir()+"JDK\\jdk.prj");

		try {
			String[] files = new DefaultProjectFileIO(crsc,inputFile).load();
	    	SourceFileRepository sfr = crsc.getSourceFileRepository();
	    	crsc.getProjectSettings().ensureExtensionClassesAreInPath();
	    	List<CompilationUnit> cul = sfr.getAllCompilationUnitsFromPath();
	    	SourceInfo si = crsc.getSourceInfo();
	    	NameInfo ni = crsc.getNameInfo();
	    	RelationRetrivalUtil helper = RelationRetrivalUtil.getInstance(si);
	    	RelationJudgeUtil util = RelationJudgeUtil.getInstance(si);
	    	
	    	crsc.getChangeHistory().updateModel();
	    	
	    	ForestWalker fw = new ForestWalker(cul);
	    	while(fw.next()){
	    		ProgramElement pe = fw.getProgramElement();
	    		if(pe instanceof ClassDeclaration){
	    			ForestWalker fw2 = new ForestWalker(cul);
	    			while(fw2.next()){
	    				ProgramElement pe2 = fw2.getProgramElement();
	    				if(pe2 instanceof ClassDeclaration){
	    					if(util.isComposition((ClassDeclaration)pe, (ClassDeclaration)pe2)){
	    						System.out.println(pe);
	    						System.out.println(pe2);
	    						System.out.println(" ");
	    					}
	    				}
	    			}
	    		}
	    	}

	    	
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserException e) {
			e.printStackTrace();
		}
		
	}
}
