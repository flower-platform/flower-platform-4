package org.flowerplatform.mindmap.remote;

import static org.flowerplatform.core.CoreConstants.PNG_EXTENSION;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.flowerplatform.core.file.FileControllerUtils;
import org.flowerplatform.core.file.IFileAccessController;

/**
 * @author Cristina Constantinescu
 * @author Alina Bratu
 */
public class MindMapServiceRemote {
		
	public List<String> getRepositoryCustomIcons(String path) throws Exception {
		IFileAccessController fac = FileControllerUtils.getFileAccessController();
		List<String> results = new ArrayList<>();
		
		Object[] files = fac.listFiles(fac.getFile(path));
		if (files == null) {
			return results;
		}
		
		for (Object file : files) {
			path = fac.getPath(file);
			if (fac.isDirectory(file)) {
	    		List<String> icons = getRepositoryCustomIcons(path);	
	    		if (icons != null) {
	    			results.addAll(icons);
	    		}
	    	} else if (fac.getName(file).endsWith(PNG_EXTENSION)) {
		    	// accept only .png icons
		    	BufferedImage image = ImageIO.read(fac.getFileAsFile(file));
		    	if (image.getWidth() == 16 && image.getHeight() == 16) {
		    		// 16x16 dimensions
			        results.add(path);
		    	}
		    }		    	
		}	
		return results;		
	}
	
}
