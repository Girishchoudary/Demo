package com.dipl.stream.util;

import java.io.File;

import org.springframework.stereotype.Component;

/**
 * 
 * @author rajeshwar.y
 *
 */
@Component
public class LiveStreamingDeleteOldFoldersFiles {

	/**
	 * 
	 * @param dir
	 */
	public void removeDirectory(File dir) {
		long purgeTime = System.currentTimeMillis() - (7 * 24 * 60 * 60 * 1000);

		if (dir.isDirectory() == false && dir.lastModified() < purgeTime) {
			dir.delete();
		} else if (dir.isDirectory() == true && dir.lastModified() < purgeTime)
			this.recursiveDelete(dir);
		else if (dir.isDirectory()) {
			File[] files = dir.listFiles();
			for (File aFile : files)
				this.removeDirectory(aFile);
		}
	}

	/**
	 * 
	 * @param file
	 */
	public void recursiveDelete(File file) {
		if (!file.exists())
			return;

		if (file.isDirectory()) {
			for (File f : file.listFiles()) {
				this.recursiveDelete(f);
			}
		}
//		System.out.println("file --> "+file.getAbsolutePath());
		file.delete();
	}

}
