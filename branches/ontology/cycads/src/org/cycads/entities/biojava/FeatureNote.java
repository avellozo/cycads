/*
 * Created on 31/10/2008
 */
package org.cycads.entities.biojava;

public interface FeatureNote
{
	public String getValue();

	public FeatureNoteType getType();

	public SequenceFeature getSequenceFeature();
}
