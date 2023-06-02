package cz.csas.colmanbatch.addons.util;

import java.util.Map;

import org.springframework.beans.factory.InitializingBean;

@SuppressWarnings({ "rawtypes", "unchecked" })
public abstract class ItemExtractorBase<T, O> implements cz.csas.colmanbatch.addons.util.ItemExtractor<T, O>, InitializingBean {
	protected Class<? extends Map> itemClassInternal;

	protected String outputItemClass;
	
	public String getOutputItemClass() {
		return outputItemClass;
	}

	public void setOutputItemClass(String outputItemClass) {
		this.outputItemClass = outputItemClass;
	}

	@Override	
	public void afterPropertiesSet() throws Exception {
		try {
			itemClassInternal = (Class<? extends Map>) Class.forName(outputItemClass);
		} catch (Exception e) {
			throw new RuntimeException("Cannot create field names item - cannot load class " + outputItemClass);
		}		
	}
	
	@Override
	public O createItem(String[] fieldNames, Object[] values) {
		Map item;
		try {
			item = itemClassInternal.newInstance();
		} catch (Exception e) {
			throw new RuntimeException("Cannot create field names item - cannot instantiate class " + outputItemClass);
		}

		for (int i = 0; i < fieldNames.length; i++) {
			
			item.put(fieldNames[i], values[i]);
		}
		return (O) item;		
	}

}
