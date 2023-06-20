package cz.csas.colmanbatch.addons.csops;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

import org.springframework.batch.item.file.FlatFileFooterCallback;

import cz.csas.colmanbatch.addons.csops.CSOPSConstants;

public class CSOPSFooterFileCallback implements FlatFileFooterCallback {
	private String endOfFileMark = CSOPSConstants.END_OF_FILE;

	public String getEndOfFileMark() {
		return endOfFileMark;
	}

	public void setEndOfFileMark(String endOfFileMark) {
		this.endOfFileMark = endOfFileMark;
	}

	public void writeFooter(Writer writer) throws IOException {
		PrintWriter pw = new PrintWriter(writer);
		if (getEndOfFileMark() != null) {
			pw.println(getEndOfFileMark());
		}
	}
}
