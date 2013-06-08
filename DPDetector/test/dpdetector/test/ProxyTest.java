package dpdetector.test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import recoder.CrossReferenceServiceConfiguration;
import recoder.ParserException;
import recoder.io.DefaultProjectFileIO;
import recoder.io.SourceFileRepository;
import recoder.java.CompilationUnit;
import recoder.service.NameInfo;
import recoder.service.SourceInfo;
import dpdetector.DetectorManager;
import dpdetector.DetectorManager.DetectorEnum;
import dpdetector.test.helper.PathHelper;
import junit.framework.TestCase;

public class ProxyTest extends TestCase {
	public void testProxy(){
		CrossReferenceServiceConfiguration crsc = new CrossReferenceServiceConfiguration();
		String inputFile = PathHelper.getTestProject();
		//File inputFile = new File(PathHelper.getTestDir()+"JHotDraw7.0.6\\jhotdraw.prj");
		//File inputFile = new File(PathHelper.getTestDir()+"junit4.10\\junit.prj");
		//File inputFile = new File(PathHelper.getTestDir()+"JDK\\jdk.prj");

	

	    	
	    	DetectorManager detector = new DetectorManager(inputFile);
	    	detector.addDetector(DetectorEnum.PROXY);
	    	detector.detect();
	    	
	
	}
}
