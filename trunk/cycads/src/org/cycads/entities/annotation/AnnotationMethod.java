/*
 * Created on 11/11/2008
 */
package org.cycads.entities.annotation;

import org.cycads.entities.note.Note;
import org.cycads.entities.note.NotesContainer;
import org.cycads.entities.note.NoteSource;

public interface AnnotationMethod extends NoteSource, NotesContainer<Note<AnnotationMethod>>
{
	public String getName();

	public int getWeight();

	public Note<AnnotationMethod> addNote(String value, String type);

}