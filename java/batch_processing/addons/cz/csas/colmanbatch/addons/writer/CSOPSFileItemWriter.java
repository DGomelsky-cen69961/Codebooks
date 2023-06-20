package cz.csas.colmanbatch.addons.writer;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.core.io.FileSystemResource;

import cz.csas.colmanbatch.addons.csops.CSOPSExportManager;
import cz.csas.colmanbatch.addons.csops.CSOPSExportManager.ManagedFile;
import cz.csas.colmanbatch.addons.reader.ItemStreamMetadataProvider;
import cz.csas.colmanbatch.addons.util.ScopeHelper;
import cz.csas.colmanbatch.addons.writer.CSOPSFileItemWriter;

public class CSOPSFileItemWriter<T> extends org.springframework.batch.item.file.FlatFileItemWriter<T> implements ItemStream {

	protected String extractName;
	protected String extractVersion;
	protected String incrementFlag;
	protected Date dataFromDate;
	protected String sourceSystem;
	protected CSOPSExportManager manager;

	protected ExecutionContext executionContext;

	protected ItemStreamMetadataProvider metadataProvider;

	protected Logger logger = LoggerFactory.getLogger(CSOPSFileItemWriter.class);
	
	public ItemStreamMetadataProvider getMetadataProvider() {
		return metadataProvider;
	}

	public void setMetadataProvider(ItemStreamMetadataProvider metadataProvider) {
		this.metadataProvider = metadataProvider;
	}

	public String getSourceSystem() {
		return sourceSystem;
	}

	public void setSourceSystem(String sourceSystem) {
		this.sourceSystem = sourceSystem;
	}

	public Date getDataFromDate() {
		return dataFromDate;
	}

	public void setDataFromDate(Date dataFromDate) {
		this.dataFromDate = dataFromDate;
	}

	public String getExtractName() {
		return extractName;
	}

	public void setExtractName(String extractName) {
		this.extractName = extractName;
	}

	public String getExtractVersion() {
		return extractVersion;
	}

	public void setExtractVersion(String extractVersion) {
		this.extractVersion = extractVersion;
	}

	public String getIncrementFlag() {
		return incrementFlag;
	}

	public void setIncrementFlag(String incrementFlag) {
		this.incrementFlag = incrementFlag;
	}

	public CSOPSExportManager getManager() {
		return manager;
	}

	public void setManager(CSOPSExportManager manager) {
		this.manager = manager;
	}

	public void open(ExecutionContext executionContext) throws ItemStreamException {
		this.executionContext = executionContext;
		ManagedFile managedFile = manager.registerExportFile(getExtractName(), getExtractVersion(), getIncrementFlag(), getSourceSystem(), getDataFromDate() != null ? getDataFromDate() : new Date());
		managedFile.setStartTime(new Date());
		managedFile.setRecordsCount(0);
		ScopeHelper.setCurrentStepProperty(executionContext, CSOPSExportManager.CURRENT_FILE, managedFile);
		if (metadataProvider != null) {
			ScopeHelper.setCurrentStepProperty(executionContext, CSOPSExportManager.ITEM_STREAM_METADATA, metadataProvider.getItemStreamMetadata());
		}

		logger.info("Writing to file: {}", managedFile.getFullFileName());
		
		setResource(new FileSystemResource(managedFile.getFullFileName()));
		super.open(executionContext);
	}

	public void close() throws ItemStreamException {
		ManagedFile managedFile = (ManagedFile) ScopeHelper.getCurrentStepProperty(executionContext, CSOPSExportManager.CURRENT_FILE);
		if (managedFile != null) {
			managedFile.setEndTime(new Date());
		}

		logger.info("File closed");
		super.close();
	}

	@Override
	public void write(List<? extends T> items) throws Exception {
		ManagedFile managedFile = (ManagedFile) ScopeHelper.getCurrentStepProperty(executionContext, CSOPSExportManager.CURRENT_FILE);
		managedFile.setRecordsCount(managedFile.getRecordsCount() + items.size());
		super.write(items);
	}

}
