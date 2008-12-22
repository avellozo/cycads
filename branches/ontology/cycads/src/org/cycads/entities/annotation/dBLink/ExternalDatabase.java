/*
 * Created on 12/11/2008
 */
package org.cycads.entities.annotation.dBLink;

import java.util.Collection;

public interface ExternalDatabase
{
	public Collection<DBRecord> getRecords();

	public String getDbName();

	public DBRecord getOrCreateDBRecord(String accession);

}