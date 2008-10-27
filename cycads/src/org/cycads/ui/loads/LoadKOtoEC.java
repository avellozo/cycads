/*
 * Created on 09/06/2008
 */
package org.cycads.ui.loads;

import java.io.File;
import java.io.IOException;

import org.cycads.exceptions.LoadLineError;
import org.cycads.general.Messages;
import org.cycads.general.ParametersDefault;
import org.cycads.general.biojava.BioJavaxSession;
import org.cycads.general.biojava.CacheCleanerBJ;
import org.cycads.loaders.KOToECLoaderBJ;
import org.cycads.ui.progress.Progress;
import org.cycads.ui.progress.ProgressPrintInterval;

public class LoadKOtoEC
{
	public static void main(String[] args) {
		BioJavaxSession.init();
		File file = LoadTools.getFile(args, 0, ParametersDefault.koToECLoaderFileName(), Messages.koToECChooseFile());
		if (file == null) {
			return;
		}

		Progress progress = new ProgressPrintInterval(System.out, ParametersDefault.koToECLoaderStepShowInterval(),
			Messages.koToECLoaderInitMsg(file.getPath()), Messages.koToECLoaderFinalMsg());
		try {

			(new KOToECLoaderBJ(progress, new CacheCleanerBJ(ParametersDefault.koToECLoaderStepCache()))).load(file);
		}
		catch (IOException e) {
			BioJavaxSession.finishWithRollback();
			e.printStackTrace();
		}
		catch (LoadLineError e) {
			BioJavaxSession.finishWithRollback();
			e.printStackTrace();
		}
		BioJavaxSession.finish();
	}

}
