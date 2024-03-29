/*
 * Created on 20/10/2008
 */
package org.cycads.loaders;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.regex.Pattern;

import org.biojava.bio.BioException;
import org.biojava.ontology.InvalidTermException;
import org.biojavax.Note;
import org.biojavax.RichObjectFactory;
import org.biojavax.SimpleRichAnnotation;
import org.biojavax.bio.seq.RichFeature;
import org.biojavax.bio.seq.RichSequence;
import org.biojavax.bio.seq.RichSequenceIterator;
import org.biojavax.bio.seq.SimpleRichFeatureRelationship;
import org.biojavax.ontology.ComparableTerm;
import org.cycads.entities.biojava.CDS;
import org.cycads.entities.biojava.CDSBJ;
import org.cycads.entities.biojava.Method;
import org.cycads.entities.biojava.MethodTypeBJ;
import org.cycads.general.CacheCleanerController;
import org.cycads.general.ParametersDefault;
import org.cycads.general.biojava.BioJavaxSession;
import org.cycads.general.biojava.TermsAndOntologies;
import org.cycads.ui.progress.Progress;

public class GBKLoaderBJ extends FileLoaderAbstract
{
	Method	methodCDSToEC;

	public GBKLoaderBJ(Progress progress, CacheCleanerController cacheCleaner) {
		this(progress, cacheCleaner,
			MethodTypeBJ.CDS_TO_EC.getOrCreateMethod(ParametersDefault.gBKLoaderMethodCDSToECName()));
	}

	private GBKLoaderBJ(Progress progress, CacheCleanerController cacheCleaner, Method methodCDSToEC) {
		super(progress, cacheCleaner);
		this.methodCDSToEC = methodCDSToEC;
	}

	public void load(BufferedReader br) throws IOException {
		progress.init();

		Pattern ecPattern = Pattern.compile(ParametersDefault.gbkECTagExpression());
		Pattern genePattern = Pattern.compile(ParametersDefault.gbkGeneTagExpression());
		Pattern rnaPattern = Pattern.compile(ParametersDefault.gbkRNATagExpression());
		Pattern cdsPattern = Pattern.compile(ParametersDefault.gbkCDSTagExpression());

		//		ComparableTerm ecTerm = TermsAndOntologies.getTermTypeEC();
		//		ComparableTerm geneTerm = TermsAndOntologies.getTermTypeGene();
		//		ComparableTerm cdsTerm = TermsAndOntologies.getTermTypeCDS();
		//
		// we are reading DNA sequences
		RichSequenceIterator seqs = RichSequence.IOTools.readGenbankDNA(br, RichObjectFactory.getDefaultNamespace());
		while (seqs.hasNext()) {
			RichSequence seq;
			try {
				seq = seqs.nextRichSequence();
			}
			catch (BioException e) {
				e.printStackTrace();
				throw new IOException(e.getMessage());
			}
			Set<RichFeature> features = seq.getFeatureSet();
			//			ArrayList<RichFeature> genes = new ArrayList<RichFeature>();
			RichFeature gene = null;
			ArrayList<RichFeature> rNAs = new ArrayList<RichFeature>();
			//			ArrayList<RichFeature> cDSs = new ArrayList<RichFeature>();
			for (RichFeature feature : features) {
				try {
					ComparableTerm ecTerm = TermsAndOntologies.getTermTypeEC();
					ComparableTerm geneTerm = TermsAndOntologies.getTermTypeGene();
					ComparableTerm cdsTerm = TermsAndOntologies.getTermTypeCDS();

					if (genePattern.matcher(feature.getType()).matches()) {
						gene = feature;
						rNAs = new ArrayList<RichFeature>();
						//						genes.add(feature);
						feature.setTypeTerm(geneTerm);
					}
					else if (rnaPattern.matcher(feature.getType()).matches()) {
						RichFeature rNA = feature;
						rNAs.add(rNA);
						if ((gene != null) && gene.getLocation().contains(rNA.getLocation())) {
							gene.addFeatureRelationship(new SimpleRichFeatureRelationship(gene, rNA,
								SimpleRichFeatureRelationship.getContainsTerm(), 0));
						}
					}
					else if (cdsPattern.matcher(feature.getType()).matches()) {
						RichFeature cDS = feature;
						progress.completeStep();
						cDS.setTypeTerm(cdsTerm);
						//						cDSs.add(feature);
						//Adjust EC qualifier
						SimpleRichAnnotation annot = (SimpleRichAnnotation) feature.getAnnotation();
						Object[] notes = annot.getNoteSet().toArray();
						for (Object obj : notes) {
							Note note = (Note) obj;
							String tag = note.getTerm().getName();
							if (ecPattern.matcher(tag).matches()) {
								note.setTerm(ecTerm);
								CDS cds = new CDSBJ(cDS);
								cds.addAnnotation(methodCDSToEC, note.getValue());
							}
						}
						//put CDS in RNAs
						for (RichFeature rNA : rNAs) {
							if (rNA.getLocation().contains(cDS.getLocation())) {
								rNA.addFeatureRelationship(new SimpleRichFeatureRelationship(rNA, cDS,
									SimpleRichFeatureRelationship.getContainsTerm(), 0));
							}
						}

					}
				}
				catch (InvalidTermException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}
			//			for (RichFeature gene : genes) {
			//				for (RichFeature rNA : rNAs) {
			//					for (RichFeature cDS : cDSs) {
			//						if (rNA.getLocation().contains(cDS.getLocation())) {
			//							rNA.addFeatureRelationship(new SimpleRichFeatureRelationship(rNA, cDS,
			//								SimpleRichFeatureRelationship.getContainsTerm(), 0));
			//						}
			//					}
			//					if (gene.getLocation().contains(rNA.getLocation())) {
			//						gene.addFeatureRelationship(new SimpleRichFeatureRelationship(gene, rNA,
			//							SimpleRichFeatureRelationship.getContainsTerm(), 0));
			//					}
			//				}
			//			}
			BioJavaxSession.session.saveOrUpdate("Sequence", seq);
			cacheCleaner.incCache();
			MethodTypeBJ.CDS_TO_EC = new MethodTypeBJ(ParametersDefault.cdsToECMethodType());
			methodCDSToEC = MethodTypeBJ.CDS_TO_EC.getOrCreateMethod(ParametersDefault.gBKLoaderMethodCDSToECName());
		}
		Object[] a1 = {progress.getStep()};
		progress.finish(a1);
	}
}
