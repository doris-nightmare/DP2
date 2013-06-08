package dpdetector;

import java.io.File;
import java.io.IOException;
import java.util.List;

import dpdetector.DetectorManager.DetectorEnum;
import dpdetector.adapter.AdapterDetector;
import dpdetector.composite.CompositeDetector;

import recoder.CrossReferenceServiceConfiguration;
import recoder.ParserException;
import recoder.convenience.TreeWalker;
import recoder.io.DefaultProjectFileIO;
import recoder.io.SourceFileRepository;
import recoder.java.CompilationUnit;
import recoder.java.ProgramElement;
import recoder.service.NameInfo;
import recoder.service.SourceInfo;

public class Main {

	
	public static void main(String[] args) {
		
		//File inputFile = new File("C:\\Users\\xiaoyang\\Documents\\DorisHere\\GraduationProject\\JHotDraw7.0.6\\jhotdraw.prj");
		//File inputFile = new File("C:\\Users\\xiaoyang\\Documents\\DorisHere\\GraduationProject\\junit4.10\\junit.prj");

		DetectorManager detector = new DetectorManager("C:\\Users\\xiaoyang\\Documents\\DorisHere\\GraduationProject\\JHotDraw7.0.6\\jhotdraw.prj");
		detector.addDetector(DetectorEnum.COMPOSITE);
		detector.detect();
	}
}
