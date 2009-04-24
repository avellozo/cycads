/*
 * Created on 09/06/2008
 */
package org.cycads.ui.loader;

import java.io.File;
import java.io.IOException;

import org.cycads.entities.EntityFactorySQL;
import org.cycads.entities.annotation.AnnotationMethod;
import org.cycads.entities.sequence.Organism;
import org.cycads.general.Config;
import org.cycads.general.Messages;
import org.cycads.parser.CDSToKOFileParser;
import org.cycads.ui.Tools;
import org.cycads.ui.progress.Progress;
import org.cycads.ui.progress.ProgressPrintInterval;

public class CDSToKOLoaderSQL {
	public static void main(String[] args) {
		EntityFactorySQL factory = new EntityFactorySQL();
		File file = Tools.getFileToOpen(args, 0, Config.cdsToKOLoaderFileName(), Messages.cdsToKOLoaderChooseFile());
		if (file == null) {
			return;
		}
		Organism< ? , ? , ? , ? , ? , ? > organism = Tools.getOrganism(args, 1, Config.cdsToKOLoaderOrganismNumber(),
			Messages.cdsToKOLoaderChooseOrganismNumber(), factory);
		if (organism == null) {
			return;
		}

		String methodName = Tools.getString(args, 2, Messages.cdsToKOLoaderChooseMethodName(),
			Config.cdsToKOMethodName());
		if (methodName == null) {
			return;
		}
		AnnotationMethod method = factory.getAnnotationMethod(methodName);

		String cdsDBName = Tools.getString(args, 3, Messages.cdsToKOLoaderChooseCDSDBName(), Config.cdsToKOCDSDBName());
		if (cdsDBName == null) {
			return;
		}

		Progress progress = new ProgressPrintInterval(System.out, Messages.cdsToKOLoaderStepShowInterval());
		try {
			progress.init(Messages.cdsToKOLoaderInitMsg(file.getPath()));
			(new CDSToKOFileParser(factory, progress, method, organism, cdsDBName)).parse(file);
			progress.finish(Messages.cdsToKOLoaderFinalMsg(progress.getStep()));
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			((EntityFactorySQL) factory).finish();
		}
	}

}
