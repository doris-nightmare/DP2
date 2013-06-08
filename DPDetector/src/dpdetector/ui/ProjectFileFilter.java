package dpdetector.ui;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class ProjectFileFilter extends FileFilter {

	@Override
	public boolean accept(File file) {
		if (file.isDirectory()) {
		      return true;
	     }
	     String fileName = file.getName();
	     int index = fileName.lastIndexOf('.');
	     if (index > 0 && index < fileName.length() - 1) {
	      String extension = fileName.substring(index + 1).toLowerCase();
	      // �ļ����ˣ���׺Ϊ.sql���ļ�����true
	      if (extension.equals("prj"))
	       return true;
	     }
	     return false;

	}

	@Override
	public String getDescription() {
		   return "Project Files(*.prj)";

	}

}
