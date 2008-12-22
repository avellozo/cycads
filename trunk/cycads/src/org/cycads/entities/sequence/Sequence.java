/*
 * Created on 12/11/2008
 */
package org.cycads.entities.sequence;

import java.util.Collection;

import org.cycads.entities.annotation.AnnotationMethod;
import org.cycads.entities.annotation.dBLink.DBLinkAnnot;
import org.cycads.entities.annotation.dBLink.DBLinkAnnotContainer;
import org.cycads.entities.annotation.dBLink.DBLinkAnnotSource;
import org.cycads.entities.annotation.dBLink.DBRecord;
import org.cycads.entities.annotation.feature.Feature;
import org.cycads.entities.annotation.feature.FeatureFilter;
import org.cycads.entities.note.Note;
import org.cycads.entities.note.NoteSource;
import org.cycads.entities.note.NotesContainer;

public interface Sequence<D extends DBLinkAnnot<D, S, R, M>, S extends Sequence< ? , ? , ? , ? , ? , ? >, R extends DBRecord< ? , ? , ? , ? >, M extends AnnotationMethod, L extends Location< ? , ? , ? , ? , ? , ? , ? , ? , ? >, F extends Feature< ? , ? , ? , ? >>
		extends NoteSource, NotesContainer<Note<S>>, DBLinkAnnotSource<D, R, M>, DBLinkAnnotContainer<D, S, R, M>
{
	public String getDescription();

	public double getVersion();

	public String getName();

	public int getId();

	public Organism<S> getOrganism();

	public L getOrCreateLocation(int start, int end, Collection<Intron> introns);

	public Collection<F> getFeatures(FeatureFilter<F> featureFilter);

	public Note<S> addNote(String value, String type);

}