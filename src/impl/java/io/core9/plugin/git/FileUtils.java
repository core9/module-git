package io.core9.plugin.git;

import java.io.File;

public class FileUtils {
	
	public static void delete(File f){
		if (f.isDirectory()) {
			for (File c : f.listFiles())
				delete(c);
		}
		if (!f.delete())
			System.out.println("Failed to delete file: " + f + " file does " + f.exists() + " exists (false = not..)");
	}
	
	private FileUtils(){}

}
