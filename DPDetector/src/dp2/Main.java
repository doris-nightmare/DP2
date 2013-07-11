package dp2;

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
import recoder.service.SourceInfo;
import dpdetector.DetectorManager;
import dpdetector.DetectorManager.DetectorEnum;
import dpdetector.test.helper.PathHelper;

public class Main {

	public static void main(String[] args) {
		List<CompilationUnit> cul;
		SourceInfo si;
		String inputFile = PathHelper.getTestDir()
				+ "JHotDraw7.0.6/jhotdraw.prj";
		CrossReferenceServiceConfiguration crsc = new CrossReferenceServiceConfiguration();

		try {
			String[] files = new DefaultProjectFileIO(crsc, new File(inputFile))
					.load();

			SourceFileRepository sfr = crsc.getSourceFileRepository();
			crsc.getProjectSettings().ensureExtensionClassesAreInPath();
			cul = sfr.getAllCompilationUnitsFromPath();
			si = crsc.getSourceInfo();
			// this.ni = crsc.getNameInfo();

			crsc.getChangeHistory().updateModel();
			
			int count =0;
			ForestWalker fw = new ForestWalker(cul);
			while(fw.next()){
				ProgramElement pe = fw.getProgramElement();
				if(pe instanceof ClassDeclaration){
					System.out.println(((ClassDeclaration) pe).getFullName() + 
							" " + ((ClassDeclaration) pe).getName()) ;
					count++;
				}
			}
			System.out.println(count);
		} catch (IOException | ParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
