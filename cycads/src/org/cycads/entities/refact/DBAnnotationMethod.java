package org.cycads.entities.refact;

import java.util.Collection;

import org.cycads.entities.annotation.dBLink.DBLinkAnnot;

/** 
 */
public class DBAnnotationMethod extends AnnotationMethod implements DBAnnotationMethod
{
	private Collection<DBAnnotation>	dBAnnotations;

	/* (non-Javadoc)
	 * @see org.cycads.entities.refact.IDBAnnotationMethod#getDBAnnotations()
	 */
	public Collection<DBAnnotation> getDBAnnotations()
	{
		return dBAnnotations;
	}

}
