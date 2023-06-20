package cz.csas.colmanbatch.addons.util;

import java.io.UnsupportedEncodingException;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CodingErrorAction;
import java.text.MessageFormat;

import org.springframework.batch.item.file.transform.PassThroughFieldExtractor;
import org.springframework.beans.factory.InitializingBean;

public class StripUnsupportedCharactersFieldExtractor<T> extends  PassThroughFieldExtractor<T> implements InitializingBean {


	protected String targetCharset;

	protected CharsetEncoder encoder;
	
	protected char[] charsReplacementSource;
	protected char[] charsReplacementDest;
	protected String unmapableCharactersReplacement;
	
	
	public String getUnmapableCharactersReplacement() {
		return unmapableCharactersReplacement;
	}

	public void setUnmapableCharactersReplacement(
			String unmapableCharactersReplacement) {
		this.unmapableCharactersReplacement = unmapableCharactersReplacement;
	}

	public char[] getCharsReplacementSource() {
		return charsReplacementSource;
	}

	public void setCharsReplacementSource(char[] charsReplacementSource) {
		this.charsReplacementSource = charsReplacementSource;
	}

	public char[] getCharsReplacementDest() {
		return charsReplacementDest;
	}

	public void setCharsReplacementDest(char[] charsReplacementDest) {
		this.charsReplacementDest = charsReplacementDest;
	}

	public String getTargetCharset() {
		return targetCharset;
	}

	public void setTargetCharset(String targetCharset) {
		this.targetCharset = targetCharset;
	}

	public void afterPropertiesSet() throws Exception {
		if (charsReplacementSource != null) {
			if (charsReplacementDest == null) {
				throw new IllegalArgumentException("Character replacement feature incorrectly configured: charsReplacementDest is not defined");
			}
			if (charsReplacementSource.length !=  charsReplacementDest.length) {
				throw new IllegalArgumentException("Character replacement feature incorrectly configured: charsReplacementSource and charsReplacementDest have different size");
			}
		}
		
		if (targetCharset != null) {
			encoder = Charset.forName(targetCharset).newEncoder();
			encoder.onUnmappableCharacter(CodingErrorAction.REPLACE);
			if (unmapableCharactersReplacement != null) {
				try {
					encoder.replaceWith(unmapableCharactersReplacement.getBytes(targetCharset));
				} catch (UnsupportedEncodingException e) {
					throw new RuntimeException(MessageFormat.format("Cannot set unmapableCharactesReplacement for encoding {0} to bytes of string \"{1}\"",	targetCharset, unmapableCharactersReplacement), e);
				}
			}
		}
	}

	@Override
	public Object[] extract(T item) {
		try {
			Object[] originalResult = super.extract(item);
			Object[] newResult = new Object[originalResult.length];
					
			for (int i = 0; i < originalResult.length; i++){
				Object oldValue = originalResult[i];
				Object newValue = oldValue;
				if (newValue instanceof String){
					if (encoder != null) {
						newValue =  new String(encoder.encode(CharBuffer.wrap((String) newValue)).array(), targetCharset);
					}
					if (charsReplacementSource != null) {
						for (int j = 0; j < charsReplacementSource.length; j++) {
							char sourceChar = charsReplacementSource[j];
							char destChar = charsReplacementDest[j];
							newValue = ((String) newValue).replace(sourceChar,destChar);
						}
					}
				}
				newResult[i] = newValue;
			}
			return newResult;

		} catch (Exception e) {
			if (e instanceof RuntimeException){
				throw (RuntimeException)e ;
			} else {
				throw new RuntimeException(e);
			}
		}				
	}
}
