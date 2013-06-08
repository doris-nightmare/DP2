package dpdetector.util;

import java.util.ArrayList;
import java.util.List;

import recoder.abstraction.ClassType;
import recoder.abstraction.Constructor;
import recoder.abstraction.Field;
import recoder.abstraction.Method;
import recoder.abstraction.ParameterizedType;
import recoder.abstraction.Type;
import recoder.convenience.TreeWalker;
import recoder.java.ProgramElement;
import recoder.java.declaration.ClassDeclaration;
import recoder.java.declaration.ConstructorDeclaration;
import recoder.java.declaration.Extends;
import recoder.java.declaration.MethodDeclaration;
import recoder.java.declaration.ParameterDeclaration;
import recoder.java.declaration.TypeDeclaration;
import recoder.java.reference.MethodReference;
import recoder.java.reference.TypeReference;
import recoder.service.SourceInfo;

//singleton
public class RelationJudgeUtil {
	private SourceInfo si;
	private static RelationJudgeUtil instance = null;
	
	
	public RelationJudgeUtil(SourceInfo si){
		this.si = si;
	}
	
	public static RelationJudgeUtil getInstance(SourceInfo si){
		if(instance == null){
			instance = new RelationJudgeUtil(si);
		}
		return instance;
	}

	/**
	 * @param sub subclass
	 * @param sup superclass
	 */
	public boolean isSubClass(ClassType sub, ClassType sup){
		//getAllSuperType contains Object & self
		if(sub== null || sup == null ){
			return false;
		}
		if(sub.getName()==null || sup.getName()==null){
			return false;
		}
		if(sub.getName().equals("Object") || sup.getName().equals("Object")){
			return false;
		}
		if(sup.getAllSupertypes().contains(sub)){
			return false;
		}
		return (sub.getAllSupertypes().contains(sup) && !sub.equals(sup));
	}
	
	public boolean isSubClass(ClassType c){
		if(c==null){
			return false;
		}
		if(c.getName()==null ||  c.getAllSupertypes()==null){
			return false;
		}
		if((!c.getName().equals("Object")) && c.getAllSupertypes().size()>0){
			return true;
		}
		return false;
	}

	public boolean isExtend(ClassType sub, ClassType sup){
		if(sub instanceof ClassDeclaration){
			List<ClassType> supTypeList = new ArrayList<ClassType>();
			Extends extendsType = ((ClassDeclaration)sub).getExtendedTypes();
			if(extendsType !=null){
				for(int i=0; i< extendsType.getChildCount();i++){
					if(extendsType.getChildAt(i) instanceof ClassType){
						ClassType currentType = (ClassType) extendsType.getChildAt(i);
						supTypeList.add(currentType);
						if(currentType instanceof ClassDeclaration){
							Extends subExtendsType = ((ClassDeclaration)currentType).getExtendedTypes();
							for(int j=0; j<subExtendsType.getChildCount();j++){
								if(subExtendsType.getChildAt(j) instanceof ClassType){
									supTypeList.add((ClassType) subExtendsType.getChildAt(j));
								}
							}
						}
						
					}
				}
				if(supTypeList.contains(sup)){
					return true;
				}
			}
			
		}
		return false;
	}

	
	public boolean isDelegationMethod(Method m, ClassType c){
		//return true iff method m has delegation to class c
		MethodDeclaration md = si.getMethodDeclaration(m);
		  for(int i=0;i<md.getChildCount();i++){
				TreeWalker tw = new TreeWalker(md.getChildAt(i));
				while(tw.next()){
					ProgramElement pe = tw.getProgramElement();
					//System.out.println(pe.getClass().getName());
					if(pe instanceof MethodReference){
						//System.out.println(((MethodReference) pe).getName());
						Method delegateMethod = si.getMethod((MethodReference) pe);
						ClassType ct = delegateMethod.getContainingClassType();
						
						if(ct.equals(c)){
							return true;
						}
						
					}
				}
			}
		  return false;
	}
	
	public boolean isDelegation(ClassType invoker, ClassType invokee){
		//return true if adapter contains a delegation method to adaptee
		boolean ret=false; 
		List<Method> mdl = invoker.getMethods();
		  for(Method m : mdl){
			 if(isDelegationMethod(m,invokee)){
				 ret = true;
			 }
		  }
		return ret;
	}
	
	public boolean isSingleComposition(ClassType container, ClassType containee){
		//check has containee field and constructor
		boolean hasField = false;
		boolean hasConstructor = false;
		List<? extends Field> flds = container.getFields();

		for (Field fld : flds) {
			// get the types of the fields
			Type tp = si.getType(fld);
			// practice:
			// tp should be typedeclaration
			// instead of classtype
			if (tp instanceof TypeDeclaration) {
				// if there's a field which is instanceof class
				if(containee.equals((ClassType) tp)){
					//hasField = true;
					return true;
				}

			}
		}
		return false;
		
//		
	}
	
	
	public boolean isMultipleComposition(ClassType container, ClassType containee){
		//check has containee field and constructor
		boolean hasField = false;
		boolean hasConstructor = false;
		List<? extends Field> flds = container.getFields();

		for (Field fld : flds) {
			// get the types of the fields
			Type tp = si.getType(fld);
			// practice:
			// tp should be typedeclaration
			// instead of classtype
			if(tp instanceof ParameterizedType){
				if(((ParameterizedType)tp).getAllTypeArgs().get(0).getTypeName().equals(containee.getFullName())){
					hasField = true;
					return true;
				}
			}
		}
		return false;
	}
	public boolean isComposition(ClassType container, ClassType containee){
		if(isSingleComposition(container,containee) || isMultipleComposition(container,containee)){
			return true;
		}
		return false;
	}
	
	public boolean isComposition(ClassType container, List<ClassType> containees){
		for(ClassType c : containees){
			if(isComposition(container,c)){
				return true;
			}
		}
		return false;
	}
	
	public boolean isOverrideMethod(ClassType superType, Method mSub){
		for(Method m : superType.getMethods()){
			if(m.getName().equals(mSub.getName())){
				//after checking of name, then param type 
				MethodDeclaration mdSub = si.getMethodDeclaration(mSub);
				MethodDeclaration md = si.getMethodDeclaration(m);
				if(mdSub == null || md ==null){
					return false;
				}
				if(md.getParameters().size() ==0 && mdSub.getParameters().size()==0){
					return true;
				}else if(md.getParameters().size() == mdSub.getParameters().size()){
					//equal but not zero
					int size = md.getParameters().size();
					List<ParameterDeclaration> pdl = md.getParameters();
					List<ParameterDeclaration> pdlSub = mdSub.getParameters();
					for(int i=0;i<size;i++){
						if(!pdl.get(i).getChildAt(0).equals(pdlSub.get(i).getChildAt(0))){
							return false;
						}
					}
					return true;
				}
			}
		}		

		return false;
	}
	
	public boolean isParameter(ClassType ct, Method m){
		
		MethodDeclaration md = si.getMethodDeclaration(m);
		
		if(md.getParameters().size()>0){
			List<ParameterDeclaration> pdl = md.getParameters();
			for(ParameterDeclaration pd : pdl){
				if(pd.getChildAt(0) instanceof TypeReference){
					if(si.getType(pd.getChildAt(0)).equals(ct)){
						return true;
					}
				}
			}
		}
		return false;
	}

	public boolean hasCopyConstructor(ClassType clazz, ClassType param) {
		List<? extends Constructor> consList = clazz.getConstructors();
		for (Constructor con : consList) {
			ConstructorDeclaration conDec = si.getConstructorDeclaration(con);
			if (conDec != null) {
				List<ParameterDeclaration> pdl = conDec.getParameters();
				for (ParameterDeclaration pd : pdl) {
					if (pd.getChildAt(0) instanceof TypeReference) {
						if (si.getType(pd.getChildAt(0)).equals(param)) {
							return true;
						}
					}
				}
			}

		}

		return false;
	}
}
