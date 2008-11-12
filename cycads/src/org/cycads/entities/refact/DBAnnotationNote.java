package org.cycads.entities.refact;

import org.cycads.entities.annotation.DBAnnotation;
import org.cycads.entities.annotation.DBAnnotationNote;

/*
 * Created on 07/11/2008
 */

public class DBAnnotationNote extends Note implements DBAnnotationNote
{

	private DBAnnotation	dBAnnotation;

	/* (non-Javadoc)
	 * @see org.cycads.entities.refact.IDBAnnotationNote#getDBAnnotation()
	 */
	public DBAnnotation getDBAnnotation()
	{
		return dBAnnotation;
	}

}