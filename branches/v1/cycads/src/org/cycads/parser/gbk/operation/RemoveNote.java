/*
 * Created on 14/09/2009
 */
package org.cycads.parser.gbk.operation;

import java.util.Collection;
import java.util.regex.Pattern;

import org.biojavax.Note;

public class RemoveNote extends SimpleNoteOperation implements NoteOperation
{

	protected RemoveNote(Pattern tagNameRegex, Pattern tagValueRegex) {
		super(tagNameRegex, tagValueRegex);
	}

	@Override
	public Note execute(Note note, Collection<Note> newNotes) {
		return null;
	}

}