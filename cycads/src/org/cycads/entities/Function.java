/*
 * Created on 25/02/2009
 */
package org.cycads.entities;

import org.cycads.entities.annotation.AssociationObject;
import org.cycads.entities.note.Noteble;
import org.cycads.entities.synonym.HasSynonyms;

public interface Function extends AssociationObject, HasSynonyms, Noteble
{
	public static final String	OBJECT_TYPE_NAME	= "Function";

	public String getName();

	public String getDescription();

}
