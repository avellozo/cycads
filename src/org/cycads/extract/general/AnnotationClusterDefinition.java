/*
 * Created on 19/03/2010
 */
package org.cycads.extract.general;

import java.util.List;

import org.cycads.extract.score.ScoreSystemCollection;

public interface AnnotationClusterDefinition
{
	public List<String> getLocations();

	public ScoreSystemCollection getScoreSystemCollection();

	//	public List<NumberValidator> getRemoveByScore();
	//
	//	public List<Pattern> getRemoveByString();

}