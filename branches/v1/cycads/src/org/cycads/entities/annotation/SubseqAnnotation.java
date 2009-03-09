/*
 * Created on 27/02/2009
 */
package org.cycads.entities.annotation;

import org.cycads.entities.note.Type;
import org.cycads.entities.sequence.Subsequence;
import org.cycads.entities.synonym.Dbxref;

public interface SubseqAnnotation<SS extends Subsequence< ? , ? , ? , ? , ? , ? >, AParent extends Annotation< ? , ? , ? , ? >, X extends Dbxref< ? >, T extends Type, M extends AnnotationMethod>
		extends Annotation<AParent, X, T, M>
{
	public SS getSubsequence();
}
