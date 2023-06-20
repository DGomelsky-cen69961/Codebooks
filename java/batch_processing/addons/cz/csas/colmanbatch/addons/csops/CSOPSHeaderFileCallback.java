package cz.csas.colmanbatch.addons.csops;

import java.io.IOException;
import java.io.Writer;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.file.FlatFileHeaderCallback;

import cz.csas.colmanbatch.addons.csops.CSOPSExportManager.ManagedFile;
import cz.csas.colmanbatch.addons.reader.ItemStreamMetadata;
import cz.csas.colmanbatch.addons.util.ScopeHelper;

public class CSOPSHeaderFileCallback implements FlatFileHeaderCallback, StepExecutionListener {

	protected CSOPSHeaderWriter csopsHeaderWriter;
	
	public CSOPSHeaderWriter getCsopsHeaderWriter() {
		return csopsHeaderWriter;
	}


	public void setCsopsHeaderWriter(CSOPSHeaderWriter csopsHeaderWriter) {
		this.csopsHeaderWriter = csopsHeaderWriter;
	}


	private StepExecution stepExecution;
	
	public void beforeStep(StepExecution stepExecution) {
		this.stepExecution = stepExecution;
	}
	

	public ExitStatus afterStep(StepExecution stepExecution) {
		return null;
	}

	
	public void writeHeader(Writer writer) throws IOException {
		ManagedFile managedFile = (ManagedFile) ScopeHelper.getCurrentStepProperty(stepExecution.getExecutionContext(), CSOPSExportManager.CURRENT_FILE);
		ItemStreamMetadata itemStreamMetadata = (ItemStreamMetadata) ScopeHelper.getCurrentStepProperty(stepExecution.getExecutionContext(), CSOPSExportManager.ITEM_STREAM_METADATA);
		csopsHeaderWriter.writeHeader(writer, managedFile, itemStreamMetadata);
	}
}
