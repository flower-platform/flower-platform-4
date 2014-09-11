/* license-start
 * 
 * Copyright (C) 2008 - 2014 Crispico Software, <http://www.crispico.com/>.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation version 3.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details, at <http://www.gnu.org/licenses/>.
 * 
 * license-end
 */
package org.flowerplatform.team.git;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.AnyObjectId;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevSort;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.revwalk.RevWalkUtils;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.AndTreeFilter;
import org.eclipse.jgit.treewalk.filter.PathFilterGroup;
import org.eclipse.jgit.treewalk.filter.TreeFilter;
import org.flowerplatform.core.file.FileControllerUtils;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.team.git.history.internal.FileDiff;
import org.flowerplatform.team.git.history.internal.WebCommit;
import org.flowerplatform.team.git.history.internal.WebCommitList;
import org.flowerplatform.team.git.history.internal.WebCommitPlotRenderer;
import org.flowerplatform.team.git.history.internal.WebWalk;
import org.flowerplatform.util.Utils;


/**
 * @author Vlad Bogdan Manica
 */

public class GitHistoryService {

	/**
	 * @author Vlad Bogdan Manica
	 */
	public List<String> getCommitedData(String nodeUri, String commitId) throws Exception {
		
		List<String> commitedFiles = new ArrayList<String>();
		String repoPath = Utils.getRepo(nodeUri);
		Repository repo = GitUtils.getRepository(FileControllerUtils.getFileAccessController().getFile(repoPath));			
		
		RevWalk walk = new RevWalk(repo);
		
		RevCommit commit = walk.parseCommit(repo.resolve(commitId));		
		for (RevCommit parent : commit.getParents()) {
			walk.parseBody(parent);
		}
		
		FileDiff[] fileDiffs = FileDiff.compute(createFileWalker(new WebWalk(repo), repo, repoPath), commit, TreeFilter.ALL);
		for (FileDiff fd : fileDiffs) {
			commitedFiles.add(fd.getLabel(fd));
		}
		return commitedFiles;	
	}	
	
	/**
	 * @author Cristina Constantinescu
	 */	
	private String getTagsString(Repository repo, RevCommit commit) {
		StringBuilder sb = new StringBuilder();
		Map<String, Ref> tagsMap = repo.getTags();
		for (Entry<String, Ref> tagEntry : tagsMap.entrySet()) {
			ObjectId target = tagEntry.getValue().getPeeledObjectId();
			if (target == null) {
				target = tagEntry.getValue().getObjectId();
			}
			if (target != null && target.equals(commit)) {
				if (sb.length() > 0) {
					sb.append(", ");
				}
				sb.append(tagEntry.getKey());
			}
		}
		return sb.toString();
	}
	
	/**
	 * @author Cristina Constantinescu
	 */	
	private List<Ref> getAllBranches(Repository repo) throws IOException {		
		List<Ref> ref = new ArrayList<Ref>();		
		ref.addAll(repo.getRefDatabase().getRefs(Constants.R_HEADS).values());
		ref.addAll(repo.getRefDatabase().getRefs(Constants.R_REMOTES).values());		
		return ref;
	}	

	/**
	 * @author Cristina Constantinescu
	 */
	private List<Ref> getBranches(RevCommit commit, Collection<Ref> allRefs, Repository db) throws IOException {
		RevWalk revWalk = new RevWalk(db);
		try {
			revWalk.setRetainBody(false);
			return RevWalkUtils.findBranchesReachableFrom(commit, revWalk, allRefs);
		} finally {
			revWalk.dispose();
		}
	}
	
	/**
	 * @author Cristina Constantinescu
	 * @author Vlad Bogdan Manica
	 */	
	public List<String> getCommitBranches(String nodeUri, String commitId) throws Exception {
		String repoPath = Utils.getRepo(nodeUri);
		Repository repo = GitUtils.getRepository(FileControllerUtils.getFileAccessController().getFile(repoPath));
		WebWalk walk = getWebWalk(nodeUri);
		RevCommit commit = walk.parseCommit(repo.resolve(commitId));
		List<Ref> branches = getBranches((WebCommit) commit, getAllBranches(repo), repo);

		List<String> branchesNames = new ArrayList<String>();
		for (int i = 0; i < branches.size(); i++) {
			branchesNames.add(branches.get(i).getName().substring((branches.get(i).getName()).indexOf('/', 5) + 1));
		}
		return branchesNames;
	}

	/**
	 * @author Cristina Constantinescu
	 * @author Vlad Bogdan Manica 
	 */
	public List<Node> getLogEntries(String nodeUri)throws Exception {
		String repoPath = Utils.getRepo(nodeUri);
		Repository repo = GitUtils.getRepository(FileControllerUtils.getFileAccessController().getFile(repoPath));
		WebWalk walk = getWebWalk(nodeUri);
		WebCommitList loadedCommits = new WebCommitList();
		loadedCommits.source(walk);
	
		loadedCommits.fillTo(GitConstants.MAXCOMMITS);
		WebCommit[] commitsAsArray = new WebCommit[loadedCommits.size()];
		loadedCommits.toArray(commitsAsArray);
		
		List<Node> result = new ArrayList<Node>();
		
		for (WebCommit commit : commitsAsArray) {
			ArrayList<String> parents = new ArrayList<String>();
			ArrayList<String> childs = new ArrayList<String>();
			commit.parseBody();
			
			Node entry = new Node(nodeUri, null);
			entry.getProperties().put(GitConstants.ID, commit.getId().name());
			entry.getProperties().put(GitConstants.ENTRY_SHORT_ID, commit.getId().abbreviate(7).name());
			entry.getProperties().put(GitConstants.SHORT_MESSAGE, commit.getShortMessage());
			entry.getProperties().put(GitConstants.LONG_MESSAGE, commit.getFullMessage());				
			
			PersonIdent person = commit.getAuthorIdent();
			entry.getProperties().put(GitConstants.AUTHOR, person.getName());
			entry.getProperties().put(GitConstants.AUTHOR_EMAIL, person.getEmailAddress());
			entry.getProperties().put(GitConstants.AUTHORED_DATE, person.getWhen());
			
			person = commit.getCommitterIdent();
			entry.getProperties().put(GitConstants.COMMITTER, person.getName());
			entry.getProperties().put(GitConstants.COMMITTER_EMAIL, person.getEmailAddress());
			entry.getProperties().put(GitConstants.COMMITER_DATE, person.getWhen());
			
			WebCommitPlotRenderer renderer = new WebCommitPlotRenderer(nodeUri, commit);
			renderer.paint();
			entry.getProperties().put(GitConstants.SPECIAL_MESSAGE, renderer.getSpecialMessage());
			entry.getProperties().put(GitConstants.DRAWINGS, renderer.getDrawings());
			
			for (int i = 0; i < commit.getParentCount(); i++) {					
				WebCommit p = (WebCommit) commit.getParent(i);
				p.parseBody();						
				Node parent = new Node(nodeUri, null);
				parent.getProperties().put(GitConstants.COMMIT_ID, p.getId().name());
				parent.getProperties().put(GitConstants.LABEL, p.getShortMessage());
				parents.add(p.getName());
				parents.add(p.getShortMessage());
			}				
			
			entry.getProperties().put(GitConstants.PARENT, parents);

			for (int i = 0; i < commit.getChildCount(); i++) {
				WebCommit p = (WebCommit) commit.getChild(i);
				p.parseBody();					
				Node child = new Node(nodeUri, null);
				child.getProperties().put(GitConstants.COMMIT_ID, p.getId().name());
				child.getProperties().put(GitConstants.LABEL, p.getShortMessage());
				childs.add(p.getName());
				childs.add(p.getShortMessage());
			}			
			
			entry.getProperties().put(GitConstants.CHILD, childs);		
			
			List<String> currentBranches = new ArrayList<String>();
			for (int i = 0; i < commit.getRefCount(); i++) {
				int index = commit.getRef(i).getName().lastIndexOf("/");
				if (index > 0) {
					currentBranches.add(commit.getRef(i).getName().substring(index + 1));
				} else {
					currentBranches.add(commit.getRef(i).getName());
				}
			}
			entry.getProperties().put(GitConstants.BRANCHES, currentBranches);
			
			String tagsString = getTagsString(repo, commit);
			if (tagsString.length() > 0) {
				entry.getProperties().put(GitConstants.TAGS, tagsString);
			}
			
			result.add(entry);
		}			
		walk.dispose();
			
		return result;			
	}

	/**
	 * @author Cristina Constantinescu
	 * @author Vlad Bogdan Manica
	 */
	private WebWalk getWebWalk(String nodeUri) throws Exception {
		String repositoryPath = Utils.getRepo(nodeUri);
		Repository repo = GitUtils.getRepository(FileControllerUtils.getFileAccessController().getFile(repositoryPath));
		WebWalk walk = new WebWalk(repo);	
		
		if (Utils.getScheme(nodeUri).equals(GitConstants.GIT_SCHEME)) {
			setupWalk(walk, repo, null);
		} else {
			String file = FileControllerUtils.getFilePathFromNodeUri(nodeUri);
			if (file != null && file.equals(GitConstants.DOT_GIT_SCHEME)) {
				setupWalk(walk, repo, null);
			} else {
				setupWalk(walk, repo, file);				
			}
		}
		return walk;
	}

	/**
	 * @author Cristina Constantinescu
	 */
	private void setupWalk(WebWalk walk, Repository repo, String path) throws IOException {
		walk.addAdditionalRefs(repo.getRefDatabase().getAdditionalRefs());
		walk.addAdditionalRefs(repo.getRefDatabase().getRefs(Constants.R_NOTES).values());
	
		walk.sort(RevSort.COMMIT_TIME_DESC, true);
		walk.sort(RevSort.BOUNDARY, true);
		walk.setRetainBody(false);
			
		setWalkStartPoints(walk, repo, walk.parseCommit(repo.resolve(Constants.HEAD)));		
		
		createFileWalker(walk, repo, path);		
	}
	
	/**
	 * @author Cristina Constantinescu
	 */
	private void setWalkStartPoints(RevWalk walk, Repository repo, AnyObjectId headId) throws IOException {	
		markStartAllRefs(repo, walk, Constants.R_HEADS);
		markStartAllRefs(repo, walk, Constants.R_REMOTES);
		markStartAllRefs(repo, walk, Constants.R_TAGS);
		
		markStartAdditionalRefs(repo, walk);
		markUninteresting(repo,  walk, Constants.R_NOTES);

		walk.markStart(walk.parseCommit(headId));		 
	}

	/**
	 * @author Cristina Constantinescu
	 */
	private TreeWalk createFileWalker(RevWalk walk, Repository db, String path) {
		TreeWalk fileWalker = new TreeWalk(db);
		fileWalker.setRecursive(true);
		fileWalker.setFilter(TreeFilter.ANY_DIFF);
		
		if (path == null || path.isEmpty()) {
			walk.setTreeFilter(TreeFilter.ALL);
		} else {
			List<String> stringPaths = new ArrayList<String>(1);
			stringPaths.add(path);
	
			walk.setTreeFilter(AndTreeFilter.create(PathFilterGroup.createFromStrings(stringPaths), TreeFilter.ANY_DIFF));
		}
		return fileWalker;
	}	
	
	/**
	 * @author Cristina Constantinescu
	 */
	private void markStartAllRefs(Repository repo, RevWalk walk, String prefix)	throws IOException {
		for (Entry<String, Ref> refEntry : repo.getRefDatabase().getRefs(prefix).entrySet()) {
			Ref ref = refEntry.getValue();
			if (ref.isSymbolic()) {
				continue;
			}
			markStartRef(repo, walk, ref);
		}
	}

	/**
	 * @author Cristina Constantinescu
	 */
	private void markStartAdditionalRefs(Repository repo, RevWalk walk) throws IOException {
		List<Ref> additionalRefs = repo.getRefDatabase().getAdditionalRefs();
		for (Ref ref : additionalRefs) {
			markStartRef(repo, walk, ref);
		}
	}

	/**
	 * @author Cristina Constantinescu
	 */
	private void markStartRef(Repository repo, RevWalk walk, Ref ref) throws IOException {
		try {
			Object refTarget = walk.parseAny(ref.getLeaf().getObjectId());
			if (refTarget instanceof RevCommit) {
				walk.markStart((RevCommit) refTarget);
			}
			//CHECKSTYLE:OFF
		} catch (MissingObjectException e) {
			// If there is a ref which points to Nirvana then we should simply
			// ignore this ref. We should not let a corrupt ref cause that the
			// history view is not filled at all
			//CHECKSTYLE:ON
		}
	}

	/**
	 * @author Cristina Constantinescu
	 */
	private void markUninteresting(Repository repo, RevWalk walk, String prefix) throws IOException {
		for (Entry<String, Ref> refEntry : repo.getRefDatabase().getRefs(prefix).entrySet()) {
			Ref ref = refEntry.getValue();
			if (ref.isSymbolic()) {
				continue;
			}
			Object refTarget = walk.parseAny(ref.getLeaf().getObjectId());
			if (refTarget instanceof RevCommit) {
				walk.markUninteresting((RevCommit) refTarget);
			}
		}
	}
}