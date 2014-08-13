/* license-start
 *
 * Copyright (C) 2008 - 2013 Crispico Software, <http://www.crispico.com/>.
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
package org.flowerplatform.ant.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author Florin
 *
 */
public class FileUtil {

	public static String readFile(File file) {
		StringBuilder sb = new StringBuilder();
		FileReader fr = null;
		BufferedReader br = null;

		try {
			fr = new FileReader(file);
			br = new BufferedReader(fr);

			String line;
			while ((line = br.readLine()) != null) {
				if (sb.length() > 0) {
					sb.append(System.getProperty("line.separator"));
				}
				sb.append(line);
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (br != null) {
					br.close();
				}
			} catch (IOException e) {
				// nothing to do
			}
		}

		return sb.toString();
	}

	public static void writeFile(File file, String text) {
		FileWriter fw = null;
		BufferedWriter bw = null;

		try {
			fw = new FileWriter(file);
			bw = new BufferedWriter(fw);
			bw.write(text);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (bw != null) {
					bw.close();
				}
			} catch (IOException e) {
				//do nothing
			}
		}
	}
}
