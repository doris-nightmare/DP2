package dpdetector;
import java.util.List;

import org.dom4j.Element;

import dpdetector.util.RelationJudgeUtil;
import dpdetector.util.RelationRetrivalUtil;
import recoder.java.CompilationUnit;
import recoder.java.ProgramElement;
import recoder.service.SourceInfo;

public abstract class AbstractDetector implements IEliminator {
	protected List<CompilationUnit> cul;
	protected SourceInfo si;
	protected RelationJudgeUtil relationJudgeUtil;
	protected RelationRetrivalUtil relationRetrivalUtil;
	protected String name;
	
	protected int detectTime;
	protected int eliminateTime;
	
	public AbstractDetector(String name,List<CompilationUnit> cul,SourceInfo si){
		this.cul = cul;
		this.si = si;
		this.name = name;
		this.relationJudgeUtil = new RelationJudgeUtil(si);
		this.relationRetrivalUtil = new RelationRetrivalUtil(si);
	}
	public abstract void detect();
	public abstract String toString();
	public abstract Element toXML();
	public abstract String getPatternName();
	public abstract int size();
	
	public int getDetectTime(){
		return detectTime;
	}
	
	public int getEliminateTime(){
		return eliminateTime;
	}
	
	public int getTotalTime(){
		return detectTime + eliminateTime;
	}
}
