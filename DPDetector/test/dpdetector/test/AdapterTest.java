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

public class AdapterTest extends TestCase {
	public void testApdater(){
		CrossReferenceServiceConfiguration crsc = new CrossReferenceServiceConfiguration();
		//String inputFile = PathHelper.getTestProject();
		//String inputFile = PathHelper.getTestDir()+"jrefactory-2.9.18-source\\jrefactory.prj";
		//String inputFile = PathHelper.getTestDir()+"JHotDraw7.0.6\\jhotdraw.prj";
		//String inputFile = PathHelper.getTestDir()+"junit4.10\\junit.prj";
		//String inputFile = PathHelper.getTestDir()+"JDK\\jdk.prj";
		//String inputFile = PathHelper.getTestDir()+"J2SE5\\J2SE5.prj";
		String inputFile = PathHelper.getTestDir()+"JHotDraw7.0.6/jhotdraw.prj";
		//String inputFile = PathHelper.getTestDir()+"JHotDraw60b1/jhotdraw.prj";

		DetectorManager detector = new DetectorManager(inputFile);
		detector.addDetector(DetectorEnum.ADAPTER);
		try {
			detector.buildAnalysisModel();
		} catch (ParserException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		detector.detect();
		detector.eliminate();
		System.out.println(detector.outputConsole());
	}
}
