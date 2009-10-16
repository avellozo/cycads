/*
 * Created on 10/07/2009
 */
package org.cycads.entities.reaction;

import java.util.Collection;

import org.cycads.entities.note.Noteble;
import org.cycads.entities.synonym.Dbxref;
import org.cycads.entities.synonym.EC;
import org.cycads.entities.synonym.HasSynonyms;

public interface Reaction<X extends Dbxref< ? , ? , ? , ? >, E extends EC< ? , ? , ? , ? >, CR extends CompoundReaction< ? , ? >, C extends Compound< ? >>
		extends Noteble, HasSynonyms<X>
{
	public E getEC();

	public boolean isReversible();

	public Collection<CR> getCompounds();

	public Collection<CR> getCompoundsSideA();

	public Collection<CR> getCompoundsSideB();

	public CR addCompoundSideA(C compound, int quantity);

	public CR addCompoundSideB(C compound, int quantity);

}