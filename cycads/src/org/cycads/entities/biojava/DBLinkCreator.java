/*
 * Created on 04/11/2008
 */
package org.cycads.entities.biojava;

import java.util.Collection;

public interface DBLinkCreator
{
	public Collection<DBLink> create(SequenceFeature feature);
}
