package dpdetector.test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import recoder.CrossReferenceServiceConfiguration;
import recoder.ParserException;
import recoder.abstraction.ClassType;
import recoder.convenience.ForestWalker;
import recoder.io.DefaultProjectFileIO;
import recoder.io.SourceFileRepository;
import recoder.java.CompilationUnit;
import recoder.java.ProgramElement;
import recoder.java.declaration.ClassDeclaration;
import recoder.java.declaration.Extends;
import recoder.java.declaration.Implements;
import recoder.service.NameInfo;
import recoder.service.SourceInfo;
import dpdetector.DetectorManager;
import dpdetector.DetectorManager.DetectorEnum;
import junit.framework.TestCase;

public class SuperTypeTest extends TestCase {

	public void testSuper(){
		CrossReferenceServiceConfiguration crsc = new CrossReferenceServiceConfiguration();
		File inputFile = new File("C:\\Users\\xiaoyang\\Documents\\DorisHere\\GraduationProject\\JHotDraw7.0.6\\jhotdraw.prj");
		//File inputFile = new File("C:\\Users\\xiaoyang\\Documents\\DorisHere\\GraduationProject\\junit4.10\\junit.prj");

		try {
			String[] files = new DefaultProjectFileIO(crsc,inputFile).load();
	    	SourceFileRepository sfr = crsc.getSourceFileRepository();
	    	crsc.getProjectSettings().ensureExtensionClassesAreInPath();
	    	List<CompilationUnit> cul = sfr.getAllCompilationUnitsFromPath();
	    	SourceInfo si = crsc.getSourceInfo();
	    	NameInfo ni = crsc.getNameInfo();
	    	
	    	crsc.getChangeHistory().updateModel();
	    	
	    	ForestWalker fw = new ForestWalker(cul);
	    	while(fw.next()){
	    		ProgramElement pe = fw.getProgramElement();
	    		 if(pe instanceof ClassDeclaration){
	    			 
	    			 List<ClassType> superTypes = ((ClassDeclaration) pe).getAllSupertypes();
	    			// Extends extendsTypes = ((ClassDeclaration) pe).getExtendedTypes();
	    			 //Implements implementedTypes = ((ClassDeclaration) pe).getImplementedTypes();
	    			 for(ClassType ct : superTypes){
	    				 System.out.println(ct.getFullName());
	    			 }
	    			 System.out.println("          vs.");
	    			 List<ClassType> allTypes = ((ClassDeclaration) pe).getSupertypes();
	    			 for(ClassType ct: allTypes){
	    				 System.out.println(ct.getFullName());
	    			 }
	    				System.out.println("----------------------");
	    		 }

	    	}

	    	
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserException e) {
			e.printStackTrace();
		}
	
	}
}
