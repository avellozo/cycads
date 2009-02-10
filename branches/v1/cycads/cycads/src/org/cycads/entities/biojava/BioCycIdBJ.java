/*
 * Created on 28/10/2008
 */
package org.cycads.entities.biojava;

import java.text.MessageFormat;
import java.text.NumberFormat;

import org.biojavax.ontology.ComparableTerm;
import org.cycads.general.CacheCleanerListener;
import org.cycads.general.ParametersDefault;
import org.cycads.general.biojava.TermsAndOntologies;

public class BioCycIdBJ implements BioCycId, CacheCleanerListener
{
	NumberFormat			numberIdFormat	= NumberFormat.getIntegerInstance();
	static ComparableTerm	nextBiocycId	= TermsAndOntologies.getTermNextBiocycId();

	{
		if (nextBiocycId.getDescription() == null || nextBiocycId.getDescription().length() == 0
			|| nextBiocycId.getDescription().equals("auto-generated by biojavax")) {
			nextBiocycId.setDescription("1");
		}
		numberIdFormat.setGroupingUsed(false);
		numberIdFormat.setParseIntegerOnly(true);
	}

	public String getNextId() {
		long nextId = Long.parseLong(nextBiocycId.getDescription());
		Object[] a = {numberIdFormat.format(nextId)};
		String ret = MessageFormat.format(ParametersDefault.bioCycIdFormat(), a);
		nextBiocycId.setDescription("" + (++nextId));
		return ret;
	}

	public void clearCache() {
		nextBiocycId = TermsAndOntologies.getTermNextBiocycId();
	}

}
