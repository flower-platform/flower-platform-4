package org.flowerplatform.team.git;

import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryCache;
import org.eclipse.jgit.lib.RepositoryCache.FileKey;
import org.eclipse.jgit.util.FS;
import org.flowerplatform.core.CorePlugin;

/**
 * @author Cojocea Marius Eduard
 */
public class GitUtils {
	
	public static Repository getRepository(File repoFile) {
		File gitDir = getGitDir(repoFile);
		if (gitDir != null) {
			try {				
				Repository repository = RepositoryCache.open(FileKey.exact(gitDir, FS.DETECTED));				
				return repository;
			}  catch (IOException e) {
				// TODO CC: log
			}
		}
		return null;		
	}
		
	public static File getGitDir(File file) {
		if (file.exists()) {
			while (file != null) {				
				if (new File(CorePlugin.getInstance().getWorkspaceLocation()).getName().equals(file.getName())) {
					return null;
				}
				if (RepositoryCache.FileKey.isGitRepository(file, FS.DETECTED)) {
					return file;
				} else if (RepositoryCache.FileKey.isGitRepository(new File(file, Constants.DOT_GIT), FS.DETECTED)) {
					return new File(file, Constants.DOT_GIT);
				}
				file = file.getParentFile();
			}
		}
		return null;
	}
	
	public static boolean isRepository(File file) {
		return getGitDir(file) != null;
	}
	

	public static String getType(String nodeUri){
		int indexStart = nodeUri.indexOf("|");
		int indexEnd = nodeUri.indexOf("$");
		return nodeUri.substring(indexStart + 1, indexEnd);
	}
	
	public static String getName(String nodeUri){
		int indexStart = nodeUri.indexOf("$");
		int indexEnd = nodeUri.length();
		return nodeUri.substring(indexStart + 1, indexEnd);
	}

		
}
