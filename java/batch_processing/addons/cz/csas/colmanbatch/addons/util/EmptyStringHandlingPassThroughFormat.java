package cz.csas.colmanbatch.addons.util;

import java.text.FieldPosition;
import java.text.ParsePosition;

public class EmptyStringHandlingPassThroughFormat extends java.text.Format{
	private static final long serialVersionUID = 1L;
	
	private java.text.Format parentFormat;
	private boolean emptyStringAsNull = true;
	private boolean nullAsNull = true;
	

	public boolean isEmptyStringAsNull() {
		return emptyStringAsNull;
	}

	public void setEmptyStringAsNull(boolean emptyStringAsNull) {
		this.emptyStringAsNull = emptyStringAsNull;
	}

	public boolean isNullAsNull() {
		return nullAsNull;
	}

	public void setNullAsNull(boolean nullAsNull) {
		this.nullAsNull = nullAsNull;
	}

	public java.text.Format getParentFormat() {
		return parentFormat;
	}

	public void setParentFormat(java.text.Format parentFormat) {
		this.parentFormat = parentFormat;
	}
	
	@Override
	public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
		if (obj == null && nullAsNull) {
			return null;
		}
		
		if (obj != null && emptyStringAsNull && obj.toString().isEmpty()) {
			return null;
		}

		return parentFormat.format(obj, toAppendTo, pos);
	}

	@Override
	public Object parseObject(String source, ParsePosition pos) {
		boolean returnNull = false;
		if (source == null && nullAsNull) {
			returnNull = true;
		}
		
		if (source != null && emptyStringAsNull && source.isEmpty()) {
			returnNull = true;
		}
		
		if (returnNull) {
			// we have to advance the index, because having pos.index == 0 after calling parseObject(...) 
			// is considered as error in the Format.parse(String source) method.
			pos.setIndex(1);
			return null;			
		}

		return parentFormat.parseObject(source, pos);
	}
}
