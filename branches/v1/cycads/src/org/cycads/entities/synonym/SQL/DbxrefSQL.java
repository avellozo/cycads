/*
 * Created on 24/02/2009
 */
package org.cycads.entities.synonym.SQL;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

import org.cycads.entities.annotation.AnnotationFilter;
import org.cycads.entities.annotation.SQL.AnnotationMethodSQL;
import org.cycads.entities.annotation.SQL.DbxrefDbxrefAnnotationSQL;
import org.cycads.entities.note.SQL.TypeSQL;
import org.cycads.entities.synonym.Dbxref;
import org.cycads.general.ParametersDefault;

public class DbxrefSQL extends HasSynonymsNotebleSQL
		implements Dbxref<DbxrefDbxrefAnnotationSQL, DbxrefSQL, TypeSQL, AnnotationMethodSQL>
{
	public final static int		INVALID_ID	= -1;
	private String				dbName;
	private String				accession;
	private int					id;
	private final Connection	con;

	public DbxrefSQL(int id, Connection con) throws SQLException {
		this.id = id;
		this.con = con;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT dbname, accession from dbxref WHERE dbxref_id=" + id);
			if (rs.next()) {
				dbName = rs.getString("dbname");
				accession = rs.getString("accession");
			}
			else {
				throw new SQLException("Dbxref does not exist:" + id);
			}
		}
		finally {
			if (rs != null) {
				try {
					rs.close();
				}
				catch (SQLException ex) {
					// ignore
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				}
				catch (SQLException ex) {
					// ignore
				}
			}
		}
	}

	public DbxrefSQL(String dbName, String accession, Connection con) throws SQLException {
		this.dbName = dbName;
		this.accession = accession;
		this.con = con;
		this.id = getId(dbName, accession, con);
		if (this.id == INVALID_ID) {
			Statement stmt = null;
			try {
				stmt = con.createStatement();
				stmt.executeUpdate("INSERT INTO dbxref (dbname, accession) VALUES ('" + dbName + "','" + accession
					+ "')");
				this.id = getId(dbName, accession, con);
				if (this.id == INVALID_ID) {
					throw new SQLException("Error creating dbxref:" + dbName + ", " + accession);
				}
			}
			finally {
				if (stmt != null) {
					try {
						stmt.close();
					}
					catch (SQLException ex) {
						// ignore
					}
				}
			}
		}
	}

	public static int getId(String dbName, String accession, Connection con) throws SQLException {
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT dbxref_id from dbxref WHERE dbname='" + dbName + "' AND accession='"
				+ accession + "'");
			int id = INVALID_ID;
			if (rs.next()) {
				id = rs.getInt("dbxref_id");
			}
			return id;
		}
		finally {
			if (rs != null) {
				try {
					rs.close();
				}
				catch (SQLException ex) {
					// ignore
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				}
				catch (SQLException ex) {
					// ignore
				}
			}
		}
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public String getAccession() {
		return accession;
	}

	@Override
	public String getDbName() {
		return dbName;
	}

	@Override
	public SynonymsSQL getSynonymsSQL() {
		if (synonymsSQL == null) {
			synonymsSQL = new SynonymsSQL(getId(), getSynonymTableName(), "dbxref_id1", getConnection());
		}
		return synonymsSQL;
	}

	@Override
	public String getSynonymTableName() {
		return "dbxref_synonym";
	}

	@Override
	public Connection getConnection() {
		return con;
	}

	@Override
	public String getIdFieldName() {
		return "dbxref_id";
	}

	@Override
	public String getNoteTableName() {
		return "dbxref_note";
	}

	@Override
	public DbxrefSQL addSynonym(String dbName, String accession) {
		DbxrefSQL dbxref = super.addSynonym(dbName, accession);
		try {
			dbxref.getSynonymsSQL().addSynonym(this);
			return dbxref;
		}
		catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Override
	public void addSynonym(DbxrefSQL dbxref) {
		super.addSynonym(dbxref);
		try {
			dbxref.getSynonymsSQL().addSynonym(this);
		}
		catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Override
	public String toString() {
		return getDbName() + ParametersDefault.getDbxrefToStringSeparator() + getAccession();
	}

	@Override
	public DbxrefDbxrefAnnotationSQL createDbxrefAnnotation(AnnotationMethodSQL method, DbxrefSQL dbxref) {
		try {
			return new DbxrefDbxrefAnnotationSQL(DbxrefDbxrefAnnotationSQL.createDbxrefDbxrefAnnotationSQL(method,
				this, dbxref, getConnection()), getConnection());
		}
		catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Override
	public DbxrefDbxrefAnnotationSQL addDbxrefAnnotation(AnnotationMethodSQL method, DbxrefSQL dbxref) {
		Collection< ? extends DbxrefDbxrefAnnotationSQL> annots = getDbxrefAnnotations(method, dbxref);
		if (annots.isEmpty()) {
			return createDbxrefAnnotation(method, dbxref);
		}
		else {
			return annots.iterator().next();
		}
	}

	@Override
	public Collection< ? extends DbxrefDbxrefAnnotationSQL> getAnnotations(AnnotationMethodSQL method,
			Collection<TypeSQL> types, DbxrefSQL synonym) {
		String extraWhere = " XXA.dbxref_source=" + getId();
		String extraFrom = "";
		return DbxrefDbxrefAnnotationSQL.getAnnotations(method, types, synonym, null, extraFrom, extraWhere,
			getConnection());
	}

	@Override
	public Collection< ? extends DbxrefDbxrefAnnotationSQL> getAnnotations(
			AnnotationFilter<DbxrefDbxrefAnnotationSQL> filter) {
		String extraWhere = " XXA.dbxref_source=" + getId();
		String extraFrom = "";
		Collection<DbxrefDbxrefAnnotationSQL> annots = DbxrefDbxrefAnnotationSQL.getAnnotations(null, null, null, null,
			extraFrom, extraWhere, getConnection());
		Collection<DbxrefDbxrefAnnotationSQL> ret = new ArrayList<DbxrefDbxrefAnnotationSQL>();
		for (DbxrefDbxrefAnnotationSQL annot : annots) {
			if (filter.accept(annot)) {
				ret.add(annot);
			}
		}
		return ret;
	}

	@Override
	public Collection< ? extends DbxrefDbxrefAnnotationSQL> getDbxrefAnnotations(AnnotationMethodSQL method,
			DbxrefSQL dbxref) {
		String extraWhere = " XXA.dbxref_source=" + getId();
		String extraFrom = "";
		return DbxrefDbxrefAnnotationSQL.getAnnotations(method, null, null, dbxref, extraFrom, extraWhere,
			getConnection());
	}

	@Override
	public Collection< ? extends DbxrefDbxrefAnnotationSQL> getDbxrefAnnotations(String dbxrefDbname) {
		String extraWhere = " XXA.dbxref_source=" + getId() + " AND XXA.dbxref_target=X.dbxref_id AND X.dbname='"
			+ dbxrefDbname + "'";
		String extraFrom = " dbxref X";
		return DbxrefDbxrefAnnotationSQL.getAnnotations(null, null, null, null, extraFrom, extraWhere, getConnection());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof DbxrefSQL) {
			return getId() == ((DbxrefSQL) obj).getId();
		}
		if (obj instanceof Dbxref) {
			DbxrefSQL x = (DbxrefSQL) obj;
			return getDbName().equals(x.getDbName()) && getAccession().equals(x.getAccession());
		}
		return false;
	}

}
