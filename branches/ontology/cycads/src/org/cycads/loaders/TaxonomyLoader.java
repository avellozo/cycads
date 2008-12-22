/*
 * Created on 15/10/2008
 */
package org.cycads.loaders;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

public interface TaxonomyLoader
{
	public void load(BufferedReader nodes, BufferedReader names) throws IOException;

	public void load(File nodesFile, File namesFile) throws IOException;
}