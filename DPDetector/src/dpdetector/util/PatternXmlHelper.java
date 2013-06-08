package dpdetector.util;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class PatternXmlHelper {

	public static Element createPattern(String patternName){
		Element pattern = DocumentHelper.createElement("Pattern");
		pattern.addAttribute("name", patternName);
		return pattern;
	}
	
	public static Element createRole(String roleName){
		Element role = DocumentHelper.createElement("Role");
		role.addAttribute("name", roleName);
		return role;
	}
	
	public static Element createClass(String className){
		Element clazz = DocumentHelper.createElement("Class");
		clazz.addAttribute("name", className);
		return clazz;
	}
	
	public static Element createClass(String className, String path){
		Element clazz = DocumentHelper.createElement("Class");
		clazz.addAttribute("name", className);
		clazz.addAttribute("path", path);
		return clazz;
	}
	
	public static Element createPatternRootElement(int patternNum){
		Element patterns = DocumentHelper.createElement("Patterns");
		patterns.addAttribute("number", Integer.toString(patternNum));
		return patterns;
	}
}
