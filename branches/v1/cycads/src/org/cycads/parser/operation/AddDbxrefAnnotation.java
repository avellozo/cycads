/*
 * Created on 15/09/2009
 */
package org.cycads.parser.operation;

import org.cycads.entities.synonym.Dbxref;
import org.cycads.entities.synonym.HasSynonyms;

public class AddDbxrefAnnotation<S extends HasSynonyms> extends SimpleRelationshipOperation<S, Dbxref>
{

	//	private EntityFactory	factory;
	//	private String			synonymDBName;
	//
	//	protected AddDbxrefAnnotation(Pattern tagNameRegex, Pattern tagValueRegex, String synonymDBName, EntityFactory factory) {
	//		super(tagNameRegex, tagValueRegex);
	//		this.synonymDBName = synonymDBName;
	//		this.factory = factory;
	//	}
	//
	//	@Override
	//	protected Collection<Dbxref> execute(S source, Note note) {
	//		Dbxref synonym = factory.getDbxref(synonymDBName, note.getValue());
	//		source.addSynonym(synonym);
	//		Collection<Dbxref> ret = new ArrayList<Dbxref>();
	//		ret.add(synonym);
	//		return ret;
	//	}
}
