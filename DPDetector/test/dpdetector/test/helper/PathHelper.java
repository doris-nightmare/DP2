package dpdetector.test.helper;

public class PathHelper {
	private static String HOME="/Users/Doris/Dropbox/ResearchesOnDropBox/ResearchOnDPD/GraduationProject/TestCases/";
	private static String HOMETEST="/Users/Doris/Documents/workspace/TestProject/testproject.prj";
	private static String COMPANY="C:\\Users\\xiaoyang\\Documents\\DorisHere\\GraduationProject\\";
	private static String COMPANYTEST="C:\\Users\\xiaoyang\\Documents\\DorisHere\\workspace\\TestProject\\testproject.prj";
	static String place ="home";
	
	
	public static String getTestDir(){
		switch(place){
		case "home":
			return HOME;
		case "company":
			return COMPANY;
		}
		return null;
	}
	
	public static String getTestProject(){
		switch(place){
		case "home":
			return HOMETEST;
		case "company":
			return COMPANYTEST;
		}
		return null;
	}

}
