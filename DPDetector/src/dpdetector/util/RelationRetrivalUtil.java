package dpdetector.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import recoder.abstraction.ClassType;
import recoder.abstraction.Method;
import recoder.abstraction.Type;
import recoder.convenience.TreeWalker;
import recoder.java.ProgramElement;
import recoder.java.declaration.ClassDeclaration;
import recoder.java.declaration.FieldSpecification;
import recoder.java.declaration.InterfaceDeclaration;
import recoder.java.declaration.MethodDeclaration;
import recoder.java.reference.MethodReference;
import recoder.service.SourceInfo;

public class RelationRetrivalUtil {
	private static RelationRetrivalUtil instance = null;
	private SourceInfo si;
	
	public RelationRetrivalUtil(SourceInfo si){
		this.si = si;
	}
	
	public List<ClassType> getAllSuperTypesWithoutObject(ClassType c){
		List<ClassType> retList = c.getAllSupertypes();
		retList.remove(c);
		for(ClassType ct : retList){
			if(ct.getFullName().equals("java.lang.Object")){
				retList.remove(ct);
				break;
			}
		}
		return retList;
	}
	
	public List<ClassType> getSuperTypesWithoutObject(ClassType c){
		List<ClassType> retList = c.getSupertypes();
		retList.remove(c);
		for(ClassType ct : retList){
			if(ct.getFullName().equals("java.lang.Object")){
				retList.remove(ct);
				break;
			}
		}
		return retList;
	}
	
	public List<ClassType> getAllSuperTypes(ClassType c){
		List<ClassType> retList = c.getAllSupertypes();
		retList.remove(c);
		return retList;
	}
	
	public List<Method> getDelegateeMethods(ClassDeclaration c){
		Set<Method> retSet = new HashSet<Method>();
		List<Method> mdl = c.getMethods();
		for(Method m : mdl){
			if(m instanceof MethodDeclaration){
				MethodDeclaration md = (MethodDeclaration) m;
				TreeWalker tw = new TreeWalker(md);
				while(tw.next()){
					ProgramElement  pe= tw.getProgramElement();
					if(pe instanceof MethodReference){
						Method delegateeMethod = si.getMethod((MethodReference) pe);					
						ClassType ct = delegateeMethod.getContainingClassType();
						if(ct instanceof ClassDeclaration || ct instanceof InterfaceDeclaration){
							retSet.add(delegateeMethod);
						}
	
					}
				}
			}
		}
		List<Method> retList = new ArrayList<Method>();
		retList.addAll(retSet);
		return retList;
	}
	
	public List<ClassType> getDelegateeClasses(Method m){
		Set<ClassType> retSet = new HashSet<ClassType>();
		MethodDeclaration md = si.getMethodDeclaration(m);
		TreeWalker tw = new TreeWalker(md);
		while(tw.next()){
			ProgramElement  pe= tw.getProgramElement();
			if(pe instanceof MethodReference){
				Method delegateeMethod = si.getMethod((MethodReference) pe);				
				ClassType ct = delegateeMethod.getContainingClassType();
				//if m invoke another method of m's class, not delegate
				if((ct instanceof ClassDeclaration || ct instanceof InterfaceDeclaration) && !ct.equals(m.getContainingClassType())){
					retSet.add(ct);
				}
			}
		}
	
		List<ClassType> retList = new ArrayList<ClassType>();
		retList.addAll(retSet);
		return retList;
	}
	
	public List<ClassType> getDelegateeClasses(ClassDeclaration c){
		Set<ClassType> retSet = new HashSet<ClassType>();
		List<Method> mdl = c.getMethods();
		for(Method m : mdl){
			if(m instanceof MethodDeclaration){
				retSet.addAll(getDelegateeClasses(m));
			}
		}
		
		List<ClassType> retList = new ArrayList<ClassType>();
		retList.addAll(retSet);
		return retList;
	}
	
	public List<ClassType> getContainedClasses(ClassType container){
		Set<ClassType> retSet = new HashSet<ClassType>();
		ClassDeclaration cd = (ClassDeclaration) si.getTypeDeclaration(container);
		List<FieldSpecification> flds = cd.getFields();
		for(FieldSpecification fld : flds){
			Type tp = si.getType(fld);
			if(tp instanceof ClassDeclaration || tp instanceof InterfaceDeclaration){
				retSet.add((ClassType) tp);
			}
		}
		List<ClassType> retList = new ArrayList<ClassType>();
		retList.addAll(retSet);
		return retList;
	}

	public static RelationRetrivalUtil getInstance(SourceInfo si) {
		if(instance == null){
			instance = new RelationRetrivalUtil(si);
		}
		return instance;
	}
}
