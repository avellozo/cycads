/*
 * Created on 12/11/2008
 */
package org.cycads.entities.annotation;

import java.util.Collection;

import org.cycads.entities.EntityObject;
import org.cycads.entities.note.Type;

public interface Association<SO extends EntityObject, TA extends EntityObject> extends EntityObject
{
	public static final String	ENTITY_TYPE_NAME	= "Association";

	public Collection< ? extends Type> getTypes();

	public boolean isType(String type);

	public boolean isType(Type type);

	public Type addType(String type);

	public Type addType(Type type);

	public SO getSource();

	public TA getTarget();
}