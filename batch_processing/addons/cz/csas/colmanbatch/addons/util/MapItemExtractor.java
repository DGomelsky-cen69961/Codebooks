package cz.csas.colmanbatch.addons.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.InitializingBean;

public class MapItemExtractor<T, O> implements ItemExtractor<T, O>, InitializingBean {
	protected Class<? extends Map> itemClassInternal;

	protected String outputItemClass;

	public String getOutputItemClass() {
		return this.outputItemClass;
	}

	public void setOutputItemClass(String outputItemClass) {
		this.outputItemClass = outputItemClass;
	}

	public void afterPropertiesSet() throws Exception {
		try {
			this.itemClassInternal = (Class)Class.forName(this.outputItemClass);
		} catch (Exception e) {
			throw new RuntimeException("Cannot create field names item - cannot load class " + this.outputItemClass);
		}
	}

	public String[] getFieldNames(T item) {
		if (!(item instanceof Map))
			throw new RuntimeException("Cannot create header - first items is not instance of Map");
		Set keySet = ((Map)item).keySet();
		List<String> names = new ArrayList<>(keySet.size());
		for (Object s : keySet)
			names.add(s.toString());
		return (String[])keySet.toArray((Object[])new String[keySet.size()]);
	}

	public Object[] getFieldValues(T item) {
		if (!(item instanceof Map))
			throw new RuntimeException("Cannot create header - first items is not instance of Map");
		Collection values = ((Map)item).values();
		return values.toArray();
	}

	public void setFieldValues(T item, Object[] values) {
		if (!(item instanceof Map))
			throw new RuntimeException("Cannot create header - first items is not instance of Map");
		Map<Object, Object> itemMap = (Map)item;
		int i = 0;
		for (Object key : itemMap.keySet()) {
			itemMap.put(key, values[i]);
			i++;
		}
	}

	public O createItem(String[] fieldNames, Object[] values) {
		Map<String, Object> item;
		try {
			item = this.itemClassInternal.newInstance();
		} catch (Exception e) {
			throw new RuntimeException("Cannot create field names item - cannot instantiate class " + this.outputItemClass);
		}
		for (int i = 0; i < fieldNames.length; i++)
			item.put(fieldNames[i], values[i]);
		return (O)item;
	}
}
