package cz.csas.colmanbatch.addons.util;

import java.util.Arrays;

import org.springframework.batch.item.file.transform.FieldSet;


public class FieldSetItemExtractor<T, O> extends cz.csas.colmanbatch.addons.util.ItemExtractorBase<T, O> {


	public String[] getFieldNames(T item) {
		if (!(item instanceof FieldSet)) {
			throw new RuntimeException("Cannot create header - first items is not instance of FieldSet");
		}
				
		return ((FieldSet)item).getNames();
	}


	public Object[] getFieldValues(T item) {
		if (!(item instanceof FieldSet)) {
			throw new RuntimeException("Cannot create header - first items is not instance of Map");
		}
		
		String[] values = ((FieldSet)item).getValues();
		return Arrays.copyOf(values, values.length, Object[].class);
	}


	
	public void setFieldValues(T item, Object[] values) {
		throw new UnsupportedOperationException();
	}
}
