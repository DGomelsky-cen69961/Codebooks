package cz.csas.colmanbatch.addons.csops;

import java.io.PrintWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;

import org.apache.commons.lang.StringUtils;
import org.springframework.batch.core.StepExecution;

import cz.csas.colmanbatch.addons.csops.CSOPSExportManager.ManagedFile;
import cz.csas.colmanbatch.addons.reader.ItemStreamMetadata;

public class CSOPSHeaderWriter {
	public static String DAY_SEQUENCE_NUMBER_FORMAT_PATTERN = "000";
	public static String CURRENT_DATE_FORMAT_PATTERN = "yyyyMMdd";	

	private String structureVersion = "1.0";	
	private String controlCharacter = "A¡BC»DœE…ÃFGHIÕJKLºMN“O”PQRÿSäTçU⁄ŸVWXY›Zéa·bcËdÔeÈÏfghiÌjklæmnÚoÛpqr¯sötùu˙˘vwxy˝zû";	
	private String lineSeparator;
	private String fieldNamesDelimiter;
	private boolean writeStandardHeader = true;
	private boolean writeColumnsHeader = true;

	public String getFieldNamesDelimiter() {
		return fieldNamesDelimiter;
	}

	public void setFieldNamesDelimiter(String fieldNamesDelimiter) {
		this.fieldNamesDelimiter = fieldNamesDelimiter;
	}

	public String getLineSeparator() {
		return lineSeparator;
	}

	public void setLineSeparator(String lineSeparator) {
		this.lineSeparator = lineSeparator;
	}

	public String getControlCharacter() {
		return controlCharacter;
	}

	public void setControlCharacter(String controlCharacter) {
		this.controlCharacter = controlCharacter;
	}


	public String getStructureVersion() {
		return structureVersion;
	}

	public void setStructureVersion(String structureVersion) {
		this.structureVersion = structureVersion;
	}


	public boolean isWriteStandardHeader() {
		return writeStandardHeader;
	}

	public void setWriteStandardHeader(boolean writeStandardHeader) {
		this.writeStandardHeader = writeStandardHeader;
	}

	public boolean isWriteColumnsHeader() {
		return writeColumnsHeader;
	}

	public void setWriteColumnsHeader(boolean writeColumnsHeader) {
		this.writeColumnsHeader = writeColumnsHeader;
	}
	
	public void writeHeader(Writer writer, ManagedFile managedFile, ItemStreamMetadata itemStreamMetadata) {
		PrintWriter pw = new PrintWriter(writer);

		if (isWriteStandardHeader()) {
			NumberFormat daySequenceNumberFormat = new DecimalFormat(DAY_SEQUENCE_NUMBER_FORMAT_PATTERN);
			DateFormat currentDateFormat = new SimpleDateFormat(CURRENT_DATE_FORMAT_PATTERN);		
			String daySequenceStr = daySequenceNumberFormat.format(managedFile.getDaySequence());                 // 00
			pw.print("Agenda code       : ");pw.print(managedFile.getAgendaCode());pw.print(lineSeparator);
			pw.print("Extract name      : ");pw.print(managedFile.getName());pw.print(lineSeparator);
			pw.print("File name         : ");pw.print(managedFile.getFileName());pw.print(lineSeparator);
			pw.print("Current Date      : ");pw.print(currentDateFormat.format(managedFile.getCurrentDate()));pw.print(lineSeparator);		
			pw.print("Day Sequence      : ");pw.print(daySequenceStr);pw.print(lineSeparator);
			pw.print("Source system     : ");pw.print(managedFile.getSourceSystem());pw.print(lineSeparator);
			pw.print("Structure Version : ");pw.print(getStructureVersion());pw.print(lineSeparator);
			pw.print("Control character : ");pw.print(getControlCharacter());			
		}		
		if (isWriteColumnsHeader() && itemStreamMetadata != null) {
			// HeaderCallback (HeaderWriter) is supposed NOT TO put the line separator at the end of the header (the separator is always put automatically
			// by the org.springframework.batch.item.file.FlatFileItemWriter<T>, so we have to put the delimiter  after the "Control character: ..." line
			// only if there follows the columns header line
			if (isWriteStandardHeader()) {
				pw.print(lineSeparator);
			}
			String fieldNamesLine = StringUtils.join(itemStreamMetadata.getFieldNames(), getFieldNamesDelimiter());
			pw.print(fieldNamesLine);
		}
		
	}
}
