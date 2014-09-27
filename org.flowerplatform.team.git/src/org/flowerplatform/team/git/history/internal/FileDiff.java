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
package org.flowerplatform.team.git.history.internal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffEntry.ChangeType;
import org.eclipse.jgit.diff.RawText;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.FileMode;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.treewalk.EmptyTreeIterator;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.TreeFilter;
import org.eclipse.jgit.treewalk.filter.TreeFilterMarker;

/**
 *	@author Cristina Constantinescu
 */
public class FileDiff {

	private final RevCommit commit;

	private DiffEntry diffEntry;

	private static ObjectId[] trees(final RevCommit commit) {
		final ObjectId[] r = new ObjectId[commit.getParentCount() + 1];
		for (int i = 0; i < r.length - 1; i++) {
			r[i] = commit.getParent(i).getTree().getId();
		}
		r[r.length - 1] = commit.getTree().getId();
		return r;
	}

	/**
	 * Computer file diffs for specified tree walk and commit
	 *
	 * @param walk
	 * @param commit
	 * @param markTreeFilters optional filters for marking entries, see {@link #isMarked(int)}
	 * @return non-null but possibly empty array of file diffs
	 * @throws MissingObjectException
	 * @throws IncorrectObjectTypeException
	 * @throws CorruptObjectException
	 * @throws IOException
	 */
	public static FileDiff[] compute(final TreeWalk walk,
			final RevCommit commit, final TreeFilter... markTreeFilters)
			throws IOException {
		final ArrayList<FileDiff> r = new ArrayList<FileDiff>();

		if (commit.getParentCount() > 0) {
			walk.reset(trees(commit));
		} else {
			walk.reset();
			walk.addTree(new EmptyTreeIterator());
			walk.addTree(commit.getTree());
		}

		if (walk.getTreeCount() <= 2) {
			List<DiffEntry> entries = DiffEntry.scan(walk, false, markTreeFilters);
			for (DiffEntry entry : entries) {
				final FileDiff d = new FileDiff(commit, entry);
				r.add(d);
			}
		} else { // DiffEntry does not support walks with more than two trees
			final int nTree = walk.getTreeCount();
			final int myTree = nTree - 1;

			TreeFilterMarker treeFilterMarker = new TreeFilterMarker(
					markTreeFilters);

			while (walk.next()) {
				if (matchAnyParent(walk, myTree)) {
					continue;
				}

				int treeFilterMarks = treeFilterMarker.getMarks(walk);

				final FileDiffForMerges d = new FileDiffForMerges(commit,
						treeFilterMarks);
				d.path = walk.getPathString();
				int m0 = 0;
				for (int i = 0; i < myTree; i++) {
					m0 |= walk.getRawMode(i);
				}
				final int m1 = walk.getRawMode(myTree);
				d.change = ChangeType.MODIFY;
				if (m0 == 0 && m1 != 0) {
					d.change = ChangeType.ADD;
				} else if (m0 != 0 && m1 == 0) {
					d.change = ChangeType.DELETE;
				} else if (m0 != m1 && walk.idEqual(0, myTree)) {
					d.change = ChangeType.MODIFY; // there is no ChangeType.TypeChanged
				}
				d.blobs = new ObjectId[nTree];
				d.modes = new FileMode[nTree];
				for (int i = 0; i < nTree; i++) {
					d.blobs[i] = walk.getObjectId(i);
					d.modes[i] = walk.getFileMode(i);
				}


				r.add(d);
			}

		}

		final FileDiff[] tmp = new FileDiff[r.size()];
		r.toArray(tmp);
		return tmp;
	}

	private static boolean matchAnyParent(final TreeWalk walk, final int myTree) {
		final int m = walk.getRawMode(myTree);
		for (int i = 0; i < myTree; i++) {		
				if (walk.getRawMode(i) == m && walk.idEqual(i, myTree)) {
					return true;
				}
			
		}
		return false;
	}

	private RawText getRawText(ObjectId id, ObjectReader reader)
			throws IOException {
		if (id.equals(ObjectId.zeroId())) {
			return new RawText(new byte[] {});
		}
		ObjectLoader ldr = reader.open(id, Constants.OBJ_BLOB);
		return new RawText(ldr.getCachedBytes(Integer.MAX_VALUE));
	}

	/**
	 * Get commit
	 *
	 * @return commit
	 */
	public RevCommit getCommit() {
		return commit;
	}

	/**
	 * Get path
	 *
	 * @return path
	 */
	public String getPath() {
		if (ChangeType.DELETE.equals(diffEntry.getChangeType())) {
			return diffEntry.getOldPath();
		}
		return diffEntry.getNewPath();
	}

	/**
	 * Get change type
	 *
	 * @return type
	 */
	public ChangeType getChange() {
		return diffEntry.getChangeType();
	}

	/**
	 * Get blob object ids
	 *
	 * @return non-null but possibly empty array of object ids
	 */
	public ObjectId[] getBlobs() {
		List<ObjectId> objectIds = new ArrayList<ObjectId>();
		if (diffEntry.getOldId() != null) {
			objectIds.add(diffEntry.getOldId().toObjectId());
		}
		if (diffEntry.getNewId() != null) {
			objectIds.add(diffEntry.getNewId().toObjectId());
		}
		return objectIds.toArray(new ObjectId[]{});
	}

	/**
	 * Get file modes
	 *
	 * @return non-null but possibly empty array of file modes
	 */
	public FileMode[] getModes() {
		List<FileMode> modes = new ArrayList<FileMode>();
		if (diffEntry.getOldMode() != null) {
			modes.add(diffEntry.getOldMode());
		}
		if (diffEntry.getOldMode() != null) {
			modes.add(diffEntry.getOldMode());
		}
		return modes.toArray(new FileMode[]{});
	}

	/**
	 * Whether the mark tree filter with the specified index matched during scan
	 * or not, see {@link #compute(TreeWalk, RevCommit, TreeFilter...)}.
	 *
	 * @param index the tree filter index to check
	 * @return true if it was marked, false otherwise
	 */
	public boolean isMarked(int index) {
		return diffEntry != null && diffEntry.isMarked(index);
	}

	/**
	 * Create a file diff for a specified {@link RevCommit} and
	 * {@link DiffEntry}
	 *
	 * @param c
	 * @param entry
	 */
	public FileDiff(final RevCommit c, final DiffEntry entry) {
		diffEntry = entry;
		commit = c;
	}

	/**
	 * Is this diff a submodule?
	 *
	 * @return true if submodule, false otherwise
	 */
	public boolean isSubmodule() {
		if (diffEntry == null) {
			return false;
		}
		return diffEntry.getOldMode() == FileMode.GITLINK
				|| diffEntry.getNewMode() == FileMode.GITLINK;
	}

	/**
	 * @author Bogdan Manica
	 */
	public String getLabel(Object object) {
		return getPath();
	}

	private static final class FileDiffForMerges extends FileDiff {
		private String path;

		private ChangeType change;

		private ObjectId[] blobs;

		private FileMode[] modes;

		private final int treeFilterMarks;

		private FileDiffForMerges(final RevCommit c, int treeFilterMarks) {
			super(c, null);
			this.treeFilterMarks = treeFilterMarks;
		}

		@Override
		public String getPath() {
			return path;
		}

		@Override
		public ChangeType getChange() {
			return change;
		}

		@Override
		public ObjectId[] getBlobs() {
			return blobs;
		}

		@Override
		public FileMode[] getModes() {
			return modes;
		}

		@Override
		public boolean isMarked(int index) {
			return (treeFilterMarks & (1L << index)) != 0;
		}
	}
}