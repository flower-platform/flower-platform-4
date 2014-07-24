package org.flowerplatform.team.git;

import java.io.File;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;
import org.flowerplatform.codesync.sdiff.FileContent;
import org.flowerplatform.codesync.sdiff.IFileContentProvider;
import org.flowerplatform.core.file.FileControllerUtils;

/**
 * @author Valentina-Camelia Bojan
 */
public class GitFileContentProvider implements IFileContentProvider {

	private String newHash;

	private String oldHash;

	private String repositoryPath;

	public GitFileContentProvider(String newHash, String oldHash, String repositoryPath) {
		this.newHash = newHash;
		this.oldHash = oldHash;
		this.repositoryPath = repositoryPath;
	}

	@Override
	public FileContent getFileContent(String filePath, String repo, Object patch) {
		String oldFileContent, newFileContent;

		try {
			Repository repository = GitUtils.getRepository((File) FileControllerUtils
											.getFileAccessController()
											.getFile(repositoryPath));
			RevWalk revWalk = new RevWalk(repository);
			TreeWalk treeWalk = new TreeWalk(repository);

			// get the old file content
			RevTree tree = revWalk.parseCommit(repository.resolve(oldHash)).getTree();
			treeWalk.addTree(tree);
			treeWalk.setRecursive(true);
			treeWalk.setFilter(PathFilter.create(filePath));

			if (treeWalk.next()) {
				InputStream inStream = repository.open(treeWalk.getObjectId(0)).openStream();
				oldFileContent = IOUtils.toString(inStream);
			} else {
				oldFileContent = "";
			}

			// get the new file content
			treeWalk = new TreeWalk(repository);
			tree = revWalk.parseCommit(repository.resolve(newHash)).getTree();
			treeWalk.addTree(tree);
			treeWalk.setRecursive(true);
			treeWalk.setFilter(PathFilter.create(filePath));

			if (treeWalk.next()) {
				InputStream inStream = repository.open(treeWalk.getObjectId(0)).openStream();
				newFileContent = IOUtils.toString(inStream);
			} else {
				newFileContent = "";
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return new FileContent(oldFileContent, newFileContent);
	}

}
