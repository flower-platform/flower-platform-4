package org.flowerplatform.mindmap;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.flowerplatform.core.file.FileControllerUtils;

public class MindMapService {

	/**
	 * @author Alina Bratu
	 * @return a string containing image names from user repository, separated by semicolons
	 * @throws Exception 
	 */
	
	public String getRepositoryCustomIcons(String repoPath) throws Exception {
		String results = "";
		File[] files = FileControllerUtils.getFileAccessController().getFileAsFile(FileControllerUtils.getFileAccessController().getFile(repoPath)).listFiles();
		for (File file : files) {
		    if (file.isFile() && file.getName().endsWith(".png")) {
		    	BufferedImage image = ImageIO.read(file);
		    	if (image.getWidth() == 16 && image.getHeight() == 16) {
			        results += repoPath + '/' + file.getName() + ";";
		    	}
		    }
		    else {
		    	if (file.isDirectory()) {
		    		results += getRepositoryCustomIcons(repoPath + '/' + file.getName()) + ";";
		    	}
		    }
		    	
		}
		results = results.substring(0, results.lastIndexOf(';'));
		return results;
	}
}
