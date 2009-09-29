/*
 * Created on 11/11/2008
 */
package org.cycads.entities.sequence;

import java.util.Collection;

import org.cycads.entities.Function;
import org.cycads.entities.annotation.AnnotationFinder;
import org.cycads.entities.annotation.AnnotationMethod;
import org.cycads.entities.annotation.DbxrefAnnotable;
import org.cycads.entities.annotation.FunctionAnnotable;
import org.cycads.entities.annotation.SubseqAnnotation;
import org.cycads.entities.annotation.SubseqDbxrefAnnotation;
import org.cycads.entities.annotation.SubseqFunctionAnnotation;
import org.cycads.entities.note.Noteble;
import org.cycads.entities.note.Type;
import org.cycads.entities.synonym.Dbxref;
import org.cycads.entities.synonym.HasSynonyms;

public interface Subsequence<S extends Sequence< ? , ? , ? , ? , ? , ? >, SA extends SubseqAnnotation< ? , ? , ? , ? , ? >, F extends Function, X extends Dbxref< ? , ? , ? , ? >, T extends Type, M extends AnnotationMethod>
		extends Noteble, HasSynonyms<X>, AnnotationFinder<SA, X, T, M>,
		DbxrefAnnotable<SubseqDbxrefAnnotation< ? , ? , ? , ? , ? >, X, M>,
		FunctionAnnotable<SubseqFunctionAnnotation< ? , ? , ? , ? , ? >, F, M>
{

	public Collection<Intron> getIntrons();

	//	public boolean addIntron(Intron intron);
	//
	//	public boolean removeIntron(Intron intron);
	//
	//	public boolean addExon(int start, int end);
	//
	public boolean isPositiveStrand();

	public int getStart();

	public int getEnd();

	public int getMinPosition();

	public int getMaxPosition();

	public S getSequence();

	public boolean contains(Subsequence< ? , ? , ? , ? , ? , ? > subseq);

	public SubseqAnnotation< ? , ? , ? , ? , ? > createAnnotation(T type, M method);

	/* Add if Annotation doesn't exist */
	public SubseqAnnotation< ? , ? , ? , ? , ? > addAnnotation(T type, M method);

	public Collection< ? extends SubseqFunctionAnnotation< ? , ? , ? , ? , ? >> getFunctionAnnotations(M method,
			F function);

}