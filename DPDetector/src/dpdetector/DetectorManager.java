package dpdetector;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import dpdetector.adapter.AdapterDetector;
import dpdetector.composite.CompositeDetector;
import dpdetector.decorator.DecoratorDetector;


import recoder.CrossReferenceServiceConfiguration;
import recoder.ParserException;
import recoder.io.DefaultProjectFileIO;
import recoder.io.SourceFileRepository;
import recoder.java.CompilationUnit;
import recoder.service.NameInfo;
import recoder.service.SourceInfo;

public class DetectorManager {

	SourceInfo si;
	List<CompilationUnit> cul;
	List<AbstractDetector> detectorList = new ArrayList<AbstractDetector>();
	
	String outputDirPath;
	String prjFilePath;
	List<DetectorEnum> detectorEnumList = new ArrayList<DetectorEnum>();
	
	private int buildASTTime;

	
	public DetectorManager(String prjFilePath){
		this.prjFilePath = prjFilePath;
	}
	public DetectorManager(String prjFilePath,String outputDirPath){
		this.prjFilePath = prjFilePath;
		this.outputDirPath = outputDirPath;
	}
	
	public DetectorManager() throws ParserException, IOException{
		//default constructor
	}
	
	public void setPrjFilePath(String prjFilePath){
		this.prjFilePath = prjFilePath;
	}
	public void setOutputDirPath(String outputDirPath){
		this.outputDirPath = outputDirPath;
	}
	
	public void buildAnalysisModel() throws ParserException, IOException{
		long startTime = Calendar.getInstance().getTimeInMillis();
		buildASTModel(new File(prjFilePath));
		long endTime = Calendar.getInstance().getTimeInMillis();
		buildASTTime = (int) (endTime - startTime);
	}
	
	private void buildASTModel(File prjFile)  throws ParserException, IOException{
		CrossReferenceServiceConfiguration crsc = new CrossReferenceServiceConfiguration();
		String[] files = new DefaultProjectFileIO(crsc,prjFile).load();
    	SourceFileRepository sfr = crsc.getSourceFileRepository();
    	crsc.getProjectSettings().ensureExtensionClassesAreInPath();
    	this.cul = sfr.getAllCompilationUnitsFromPath();
    	this.si = crsc.getSourceInfo();
    	//this.ni = crsc.getNameInfo();
    	
    	crsc.getChangeHistory().updateModel();
	}
	
	private void createDetectors(){
		for(DetectorEnum de : detectorEnumList){
			switch(de){
			case COMPOSITE:
				detectorList.add(new CompositeDetector(cul,si));
				break;
			case ADAPTER:
				detectorList.add(new AdapterDetector(cul,si));
				break;
			case DECORATOR:
				detectorList.add(new DecoratorDetector(cul,si));
				break;

			}
		}
	}

	public void addDetector(DetectorEnum detectorName){
		detectorEnumList.add(detectorName);		
	}
	
	public void clearDetectors(){
		detectorList.clear();
		detectorEnumList.clear();
	}
	public void detect(){
		createDetectors();
		for(AbstractDetector ad : detectorList){
			ad.detect();
		}
	}
	
	public void eliminate(){
		for(AbstractDetector ad : detectorList){
			ad.eliminate();
		}
	}
	
	public String outputConsole(){
		StringBuilder sb = new StringBuilder();
		for(AbstractDetector ad : detectorList){
			sb.append(ad.toString());
		}
		return sb.toString();
		
	}
	
	public void outputXMLFile(){
		File outputDir = new File(outputDirPath);
		if(!outputDir.exists()){
			boolean ret = outputDir.mkdirs();
		//	System.out.println(ret);
		}
		
		for(AbstractDetector ad : detectorList){
			try {
				FileWriter outputFileWriter = new FileWriter(outputDirPath+File.separator+ad.getPatternName()+".xml");
				OutputFormat format = OutputFormat.createPrettyPrint();
				XMLWriter writer = new XMLWriter( outputFileWriter, format );			
				writer.write( ad.toXML() );
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			} 
		}
	}
	
	public String getStatistics(){
		StringBuilder sb = new StringBuilder();
		sb.append("AST Build Time: " + Integer.toString(buildASTTime) + " ms\n");
		for(AbstractDetector ad : detectorList){
			sb.append(ad.getPatternName() + ":\n");
			sb.append("Candidate Detect Time: " + Integer.toString(ad.getDetectTime()) + " ms\n");
			sb.append("Eliminate Time: " + Integer.toString(ad.getEliminateTime()) + " ms\n");
			sb.append("Total Cost Time: " + Integer.toString(ad.getTotalTime()) + " ms\n");
		}
		return sb.toString();
	}
	
	
	public enum DetectorEnum{
		COMPOSITE,ADAPTER,DECORATOR,PROXY;
	}
	
	public enum OutputEnum{
		CONSOLE,XML,BOTH;
	}
}
