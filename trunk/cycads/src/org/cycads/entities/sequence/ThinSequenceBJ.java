/*
 * Created on 29/10/2008
 */
package org.cycads.entities.sequence;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Set;

import org.biojavax.RankedCrossRef;
import org.biojavax.RichAnnotation;
import org.biojavax.SimpleRankedCrossRef;
import org.biojavax.bio.seq.RichSequence;
import org.biojavax.bio.seq.ThinRichSequence;
import org.cycads.entities.annotation.AnnotationMethodBJ;
import org.cycads.entities.annotation.dBLink.DBLinkFilter;
import org.cycads.entities.annotation.dBLink.BJ.DBRecordBJ;
import org.cycads.entities.annotation.dBLink.BJ.DBRecordDBRecordLinkBJ;
import org.cycads.entities.annotation.dBLink.BJ.ExternalDatabaseBJ;
import org.cycads.entities.annotation.dBLink.BJ.ThinDBLinkBJ;
import org.cycads.entities.annotation.feature.FeatureBJ;
import org.cycads.entities.annotation.feature.FeatureFilter;
import org.cycads.entities.change.ChangeListener;
import org.cycads.entities.change.ChangeType;
import org.cycads.entities.note.Note;
import org.cycads.entities.note.NotesHashTable;
import org.cycads.entities.note.NotesToAnnotationBJ;
import org.cycads.entities.note.SimpleNote;
import org.cycads.exceptions.MethodNotImplemented;
import org.cycads.general.Messages;
import org.cycads.general.biojava.BioJavaxSession;
import org.cycads.general.biojava.BioSql;
import org.hibernate.Query;

public class ThinSequenceBJ
		implements
		Sequence<ThinDBLinkBJ<ThinSequenceBJ>, ThinSequenceBJ, DBRecordBJ, AnnotationMethodBJ, LocationBJ, FeatureBJ>
{
	int												id;
	RichSequence									richSeq	= null;
	NotesHashTable<Note<ThinSequenceBJ>>			notes;
	NCBIOrganismBJ									organism;
	Hashtable<String, ThinDBLinkBJ<ThinSequenceBJ>>	dbLinks;

	public ThinSequenceBJ(int id, NCBIOrganismBJ organism) {
		this.id = id;
		this.organism = organism;
	}

	public ThinSequenceBJ(RichSequence richSeq) {
		this.richSeq = richSeq;
		organism = new NCBIOrganismBJ(richSeq.getTaxon());
		id = BioSql.getSequenceId(richSeq);
	}

	public int getId() {
		return id;
	}

	public NotesHashTable<Note<ThinSequenceBJ>> getNotesHash() {
		if (notes == null) {
			notes = NotesToAnnotationBJ.createNotesHashTable((RichAnnotation) getRichSeq().getAnnotation(), this);
		}
		return notes;
	}

	public RichSequence getRichSeq() {
		if (richSeq == null) {
			richSeq = getRichSeq(getId());
		}
		return richSeq;
	}

	public Hashtable<String, ThinDBLinkBJ<ThinSequenceBJ>> getDBLinksHash() {
		if (dbLinks == null) {
			dbLinks = new Hashtable<String, ThinDBLinkBJ<ThinSequenceBJ>>();
			Set<RankedCrossRef> refs = (Set<RankedCrossRef>) getRichSeq().getRankedCrossRefs();
			ThinDBLinkBJ<ThinSequenceBJ> dbLink;
			for (RankedCrossRef ref : refs) {
				dbLink = new ThinDBLinkBJ<ThinSequenceBJ>(this, new DBRecordBJ(ref.getCrossRef()));
				dbLinks.put(dbLink.toString(), dbLink);
			}
		}
		return dbLinks;
	}

	private static ThinRichSequence getRichSeq(int id) {
		Query query = BioJavaxSession.createQuery("from ThinSequence where id=:id");
		query.setInteger("id", id);
		return (ThinRichSequence) query.uniqueResult();
	}

	@Override
	public Note<ThinSequenceBJ> addNote(Note<ThinSequenceBJ> note) {
		return getNotesHash().addNote(note);
	}

	@Override
	public Note<ThinSequenceBJ> getNote(String value, String noteTypeName) {
		return getNotesHash().getNote(value, noteTypeName);
	}

	@Override
	public Collection<Note<ThinSequenceBJ>> getNotes() {
		return getNotesHash().getNotes();
	}

	@Override
	public Collection<Note<ThinSequenceBJ>> getNotes(String noteTypeName) {
		return getNotesHash().getNotes(noteTypeName);
	}

	@Override
	public void addChangeListener(ChangeListener<Note<ThinSequenceBJ>> cl, ChangeType ct) {
		getNotesHash().addChangeListener(cl, ct);
	}

	@Override
	public boolean isUnchanging(ChangeType ct) {
		return getNotesHash().isUnchanging(ct);
	}

	@Override
	public void removeChangeListener(ChangeListener<Note<ThinSequenceBJ>> cl, ChangeType ct) {
		getNotesHash().removeChangeListener(cl, ct);
	}

	@Override
	public Note<ThinSequenceBJ> createNote(String value, String noteTypeName) {
		return addNote(new SimpleNote<ThinSequenceBJ>(this, value, noteTypeName));
	}

	public LocationBJ createLocation(int start, int end, Collection<Intron> introns) {
		throw new MethodNotImplemented();
		// return new LocationBJ(start, end, this, introns);
	}

	public String getDescription() {
		return getRichSeq().getDescription();
	}

	public String getName() {
		return getRichSeq().getName();
	}

	public NCBIOrganismBJ getOrganism() {
		return organism;
	}

	public double getVersion() {
		return getRichSeq().getVersion();
	}

	public Collection<FeatureBJ> getFeatures(FeatureFilter<FeatureBJ> featureFilter) {
		Collection<Integer> results = BioSql.getFeaturesId(getId());
		Collection<FeatureBJ> ret = new ArrayList<FeatureBJ>();
		for (Integer featureId : results) {
			FeatureBJ f = new FeatureBJ(featureId);
			if (featureFilter.accept(f)) {
				ret.add(f);
			}
		}
		return ret;
	}

	@Override
	public ThinDBLinkBJ<ThinSequenceBJ> createDBLink(AnnotationMethodBJ method, DBRecordBJ record) {
		ThinDBLinkBJ<ThinSequenceBJ> dbLink;
		if (method == null) {
			dbLink = new ThinDBLinkBJ<ThinSequenceBJ>(this, record);
			addDBLink(dbLink);
		}
		else {
			throw new MethodNotImplemented();
		}
		return dbLink;
	}

	@Override
	public ThinDBLinkBJ<ThinSequenceBJ> createDBLink(AnnotationMethodBJ method, String accession, String dbName) {
		return createDBLink(method, ExternalDatabaseBJ.getOrCreateExternalDB(dbName).getOrCreateDBRecord(accession));
	}

	@Override
	public void addDBLink(ThinDBLinkBJ<ThinSequenceBJ> link) {
		if (link.getSource() != this) {
			throw new IllegalArgumentException(Messages.exceptionHandleDBLinkOtherSequence());
		}
		getRichSeq().addRankedCrossRef(new SimpleRankedCrossRef(link.getDBRecordTarget().getCrossRef(), 0));
	}

	@Override
	public ThinDBLinkBJ<ThinSequenceBJ> getDBLink(ThinSequenceBJ source, AnnotationMethodBJ method, DBRecordBJ target) {
		if (source != this) {
			return null;
		}
		if (method != null) {
			return null;
		}
		return getDBLinksHash().get(DBRecordDBRecordLinkBJ.joinTermName(source.toString(), "", target.toString()));
	}

	@Override
	public Collection<ThinDBLinkBJ<ThinSequenceBJ>> getDBLinks(AnnotationMethodBJ method, DBRecordBJ target) {
		ArrayList<ThinDBLinkBJ<ThinSequenceBJ>> dbLinks = new ArrayList<ThinDBLinkBJ<ThinSequenceBJ>>();
		ThinDBLinkBJ<ThinSequenceBJ> dbLink = getDBLink(this, method, target);
		if (dbLink != null) {
			dbLinks.add(dbLink);
		}
		return dbLinks;
	}

	@Override
	public Collection<ThinDBLinkBJ<ThinSequenceBJ>> getDBLinks(AnnotationMethodBJ method, String dbName,
			String accession) {
		return getDBLinks(method, DBRecordBJ.getOrCreateDBRecordBJ(dbName, accession));
	}

	@Override
	public Collection<ThinDBLinkBJ<ThinSequenceBJ>> getDBLinks(DBLinkFilter<ThinDBLinkBJ<ThinSequenceBJ>> filter) {
		ArrayList<ThinDBLinkBJ<ThinSequenceBJ>> dbLinks = new ArrayList<ThinDBLinkBJ<ThinSequenceBJ>>();
		for (ThinDBLinkBJ<ThinSequenceBJ> dbLink : getDBLinksHash().values()) {
			if (filter.accept(dbLink)) {
				dbLinks.add(dbLink);
			}
		}
		return dbLinks;
	}

	@Override
	public String toString() {
		return getRichSeq().getName();
	}

}
