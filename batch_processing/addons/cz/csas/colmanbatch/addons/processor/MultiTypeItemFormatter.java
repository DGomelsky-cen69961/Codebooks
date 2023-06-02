package cz.csas.colmanbatch.addons.processor;

import java.text.Format;
import java.util.Map;

import org.springframework.batch.item.ItemProcessor;

import cz.csas.colmanbatch.addons.util.ItemExtractor;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class MultiTypeItemFormatter<I, O> implements ItemProcessor<I, O> {
	
	protected Map<Class, Format> defaultMappings; 
	
	protected Map<String, Format> fieldSpecificMapping;
	
	protected Map<String, Format> fieldSpecificParsers;
	
	protected ItemExtractor<I, O> itemExtractor;
	

	public Map<Class, Format> getDefaultMappings() {
		return defaultMappings;
	}


	public void setDefaultMappings(Map<Class, Format> defaultMappings) {
		this.defaultMappings = defaultMappings;
	}


	public Map<String, Format> getFieldSpecificMapping() {
		return fieldSpecificMapping;
	}


	public void setFieldSpecificMapping(Map<String, Format> fieldSpecificMapping) {
		this.fieldSpecificMapping = fieldSpecificMapping;
	}


	public ItemExtractor<I, O> getItemExtractor() {
		return itemExtractor;
	}


	public void setItemExtractor(ItemExtractor<I, O> itemExtractor) {
		this.itemExtractor = itemExtractor;
	}

	public Map<String, Format> getFieldSpecificParsers() {
		return fieldSpecificParsers;
	}

	public void setFieldSpecificParsers(Map<String, Format> fieldSpecificParsers) {
		this.fieldSpecificParsers = fieldSpecificParsers;
	}
	
	public O process(I item) throws Exception {
		String[] fieldNames = itemExtractor.getFieldNames(item);
		Object[] fieldValues = itemExtractor.getFieldValues(item);
		
		O outputItem = null;
		
		fieldsLoop:
		for (int i = 0; i < fieldNames.length; i++) {
			Object fieldValue = fieldValues[i];
			
			if (fieldValue == null) {
				continue fieldsLoop;
			}
			
			String fieldName = fieldNames[i];

			try {

				Format format = null;
				if (fieldSpecificMapping != null ) {
					format = fieldSpecificMapping.get(fieldName);
				}
				
				if (format == null && defaultMappings != null) {
					formatLoop:
					for(Map.Entry<Class, Format> entry : defaultMappings.entrySet()) {
						if (entry.getKey().isAssignableFrom(fieldValue.getClass())) {
							format  = entry.getValue();
							break formatLoop;
						}
					}
				}
				
				if (format != null) {
					String newFieldValue = format.format(fieldValue);
					fieldValue = newFieldValue; 
					fieldValues[i] = newFieldValue;

					if (fieldValue == null) {
						continue fieldsLoop;
					}
				}
				
				Format parserFormat = null;
				if (fieldSpecificParsers != null ) {
					parserFormat = fieldSpecificParsers.get(fieldName);
				}
				
				if (parserFormat != null) {					
					Object newFieldValue = parserFormat.parseObject(fieldValue.toString());
					fieldValue = newFieldValue; 
					fieldValues[i] = newFieldValue;
				}			
			} catch (Exception e) {
				throw new RuntimeException("Error occured during processing field \"" + fieldName + "\" with value \"" + fieldValue + "\": " + e.getMessage(), e);
			}
			
		}
		outputItem = itemExtractor.createItem(fieldNames, fieldValues);		
		return outputItem;
	}
}
