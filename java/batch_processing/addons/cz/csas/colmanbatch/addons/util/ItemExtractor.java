package cz.csas.colmanbatch.addons.util;


public interface ItemExtractor<T, O> {
	public String[] getFieldNames(T item);

	public Object[] getFieldValues(T item);
	
	public O createItem(String[] fieldNames, Object[] values);
}
