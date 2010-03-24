package org.cycads.extract.general;

import java.util.ArrayList;
import java.util.List;

import org.cycads.entities.annotation.AnnotationMethod;

public class SimpleAnnotationWay extends ArrayList<Object> implements
		AnnotationWay {

	public SimpleAnnotationWay() {
		super();
	}

	public SimpleAnnotationWay(AnnotationWay c) {
		super(c);
	}

	public SimpleAnnotationWay(Object o) {
		super();
		add(o);
	}

	public SimpleAnnotationWay(int initialCapacity) {
		super(initialCapacity);
	}

	@Override
	public void addFirst(Object o) {
		if (o != null && !o.equals(get(0))) {
			add(0, o);
		}
	}

	@Override
	public void addLast(Object o) {
		if (o != null && !o.equals(get(size() - 1))) {
			add(o);
		}
	}

	@Override
	public Object getSource() {
		return get(0);
	}

	@Override
	public Object getTarget() {
		return get(size() - 1);
	}

	@Override
	public List<AnnotationMethod> getMethods() {
		// TODO Auto-generated method stub
		return null;
	}

}
