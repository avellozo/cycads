/*
 * Created on 12/11/2008
 */
package org.cycads.entities.sequence;

import java.util.Collection;

import org.cycads.entities.annotation.ExternalAnnotationSource;
import org.cycads.entities.annotation.FeatureAnnotation;
import org.cycads.entities.note.NoteHolder;
import org.cycads.entities.sequence.feature.FeatureFilter;

public interface Sequence extends NoteHolder<Sequence>, ExternalAnnotationSource
{

	public Collection<FeatureAnnotation> getFeatures();

	public Collection<FeatureAnnotation> getFeatures(FeatureFilter featureFilter);

	public String getDescription();

	public double getVersion();

	public String getName();

	public Organism getOrganism();

}