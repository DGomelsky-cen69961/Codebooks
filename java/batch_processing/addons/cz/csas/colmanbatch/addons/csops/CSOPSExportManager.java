package cz.csas.colmanbatch.addons.csops;

import cz.csas.colmanbatch.addons.util.CSOPSFile;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

public class CSOPSExportManager implements Serializable, JobExecutionListener {
	public static final int DEFAULT_DAY_SEQUENCE = 1;

	public static final String CURRENT_FILE = "currentFile";

	public static final String ITEM_STREAM_METADATA = "itemStreamMetadata";

	public static final String JOB_PARAM_DAY_SEQUENCE = "daySeq";

	public static final String JOB_PARAM_EXTRACT_DATE = "date";

	private static final Logger LOGGER = LoggerFactory.getLogger(CSOPSExportManager.class);

	protected CSOPSHeaderWriter csopsHeaderWriter;

	private String endOfFileMark = "END OF FILE";

	private String lineSeparator;

	private String encoding;

	private static final long serialVersionUID = 5738023835002297594L;

	public String getEncoding() {
		return this.encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public String getLineSeparator() {
		return this.lineSeparator;
	}

	public void setLineSeparator(String lineSeparator) {
		this.lineSeparator = lineSeparator;
	}

	public CSOPSHeaderWriter getCsopsHeaderWriter() {
		return this.csopsHeaderWriter;
	}

	public void setCsopsHeaderWriter(CSOPSHeaderWriter csopsHeaderWriter) {
		this.csopsHeaderWriter = csopsHeaderWriter;
	}

	public String getEndOfFileMark() {
		return this.endOfFileMark;
	}

	public void setEndOfFileMark(String endOfFileMark) {
		this.endOfFileMark = endOfFileMark;
	}

	public static class ManagedFile implements Serializable {
		private static final long serialVersionUID = 4449999355611243754L;

		private String agendaCode;

		private String name;

		private String fileName;

		private String fullFileName;

		private Date currentDate;

		private long daySequence;

		private String sourceSystem;

		private String version;

		private String incrementFlag;

		private CSOPSFile csopsFile;

		private int recordsCount;

		private Date startTime;

		private Date endTime;

		private Date dataFromDate;

		public int getRecordsCount() {
			return this.recordsCount;
		}

		public void setRecordsCount(int recordsCount) {
			this.recordsCount = recordsCount;
		}

		public Date getStartTime() {
			return this.startTime;
		}

		public void setStartTime(Date startTime) {
			this.startTime = startTime;
		}

		public Date getEndTime() {
			return this.endTime;
		}

		public void setEndTime(Date endTime) {
			this.endTime = endTime;
		}

		public ManagedFile(String agendaCode, String name, String fileName, String fullFileName, Date currentDate, long daySequence, String sourceSystem, String version, String incrementFlag, Date dataFromDate) {
			this.agendaCode = agendaCode;
			this.name = name;
			this.fileName = fileName;
			this.currentDate = currentDate;
			this.daySequence = daySequence;
			this.sourceSystem = sourceSystem;
			this.version = version;
			this.incrementFlag = incrementFlag;
			this.dataFromDate = dataFromDate;
			this.fullFileName = fullFileName;
			this.csopsFile = new CSOPSFile(fullFileName);
		}

		public String toString() {
			return "ManagedFile [agendaCode=" + this.agendaCode + ", name=" + this.name +
					", fileName=" + this.fileName + ", currentDate=" + this.currentDate +
					", daySequence=" + this.daySequence +
					", sourceSystem=" + this.sourceSystem + ", version=" + this.version +
					", incrementFlag=" + this.incrementFlag + ", dataFromDate=" +
					this.dataFromDate + "]";
		}

		public String getAgendaCode() {
			return this.agendaCode;
		}

		public void setAgendaCode(String agendaCode) {
			this.agendaCode = agendaCode;
		}

		public String getName() {
			return this.name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getFileName() {
			return this.fileName;
		}

		public void setFileName(String fileName) {
			this.fileName = fileName;
		}

		public Date getCurrentDate() {
			return this.currentDate;
		}

		public void setCurrentDate(Date currentDate) {
			this.currentDate = currentDate;
		}

		public long getDaySequence() {
			return this.daySequence;
		}

		public void setDaySequence(long daySequence) {
			this.daySequence = daySequence;
		}

		public String getSourceSystem() {
			return this.sourceSystem;
		}

		public void setSourceSystem(String sourceSystem) {
			this.sourceSystem = sourceSystem;
		}

		public String getVersion() {
			return this.version;
		}

		public void setVersion(String version) {
			this.version = version;
		}

		public String getIncrementFlag() {
			return this.incrementFlag;
		}

		public void setIncrementFlag(String incrementFlag) {
			this.incrementFlag = incrementFlag;
		}

		public Date getDataFromDate() {
			return this.dataFromDate;
		}

		public void setDataFromDate(Date dataFromDate) {
			this.dataFromDate = dataFromDate;
		}

		public CSOPSFile getCSOPSFile() {
			return this.csopsFile;
		}

		public String getFullFileName() {
			return this.fullFileName;
		}
	}

	public static String DAY_SEQUENCE_NUMBER_FORMAR_PATTERN = "000";

	public static String EXTRACT_DATE_FORMAT_PATTERN = "yyMMdd";

	public static String EXTRACT_DATE_LONG_FORMAT_PATTERN = "yyyyMMdd";

	public static String TIME_FORMAT_PATTERN = "yyyyMMddHHmmss";

	public static String TEMP_SUFFIX = "TEMP";

	public static String SEND_SUFFIX = "SEND";

	protected String tempDirectory;

	protected String exportDirectory;

	protected String agendaCode;

	protected String summaryFileCode;

	protected boolean createSummaryFile;

	protected boolean summaryFileWriteColumnsHeader = true;

	private long daySequence;

	private String sourceSystem;

	private String sourceSystemCsopsCode;

	private String targetSystemCsopsCode;

	private Date extractDate;

	protected boolean deleteExistingFile = true;

	protected String[] summaryFileColumns = new String[] {
			"EXTRACT_AGENDA_CODE", "EXTRACT_NAME", "EXTRACT_FILE_NAME", "EXTRACT_CURRENT_DATE", "EXTRACT_DAY_SEQUENCE", "EXTRACT_START_TIME", "EXTRACT_END_TIME", "EXTRACT_RECORD_COUNT", "EXTRACT_SOURCE_SYSTEM", "EXTRACT_VERSION",
			"EXTRACT_INCREMENT_FLAG", "EXTRACT_DATA_FROM_DATE" };

	public String currentExtractName;

	public String currentExtractShortFileName;

	protected List<ManagedFile> reqisteredFiles = new ArrayList<>();

	public String[] getSummaryFileColumns() {
		return this.summaryFileColumns;
	}

	public void setSummaryFileColumns(String[] summaryFileColumns) {
		this.summaryFileColumns = summaryFileColumns;
	}

	public String getSourceSystem() {
		return this.sourceSystem;
	}

	public void setSourceSystem(String sourceSystem) {
		this.sourceSystem = sourceSystem;
	}

	public String getSummaryFileCode() {
		return this.summaryFileCode;
	}

	public void setSummaryFileCode(String summaryFileCode) {
		this.summaryFileCode = summaryFileCode;
	}

	public boolean isDeleteExistingFile() {
		return this.deleteExistingFile;
	}

	public void setDeleteExistingFile(boolean deleteExistingFile) {
		this.deleteExistingFile = deleteExistingFile;
	}

	public boolean isSummaryFileWriteColumnsHeader() {
		return this.summaryFileWriteColumnsHeader;
	}

	public void setSummaryFileWriteColumnsHeader(boolean summaryFileWriteColumnsHeader) {
		this.summaryFileWriteColumnsHeader = summaryFileWriteColumnsHeader;
	}

	public Date getExtractDate() {
		return this.extractDate;
	}

	public void setExtractDate(Date extractDate) {
		this.extractDate = extractDate;
	}

	public String getCurrentExtractName() {
		return this.currentExtractName;
	}

	public void setCurrentExtractName(String currentExtractName) {
		this.currentExtractName = currentExtractName;
	}

	public String getCurrentExtractShortFileName() {
		return this.currentExtractShortFileName;
	}

	public void setCurrentExtractShortFileName(String currentShortFileName) {
		this.currentExtractShortFileName = currentShortFileName;
	}

	public boolean isCreateSummaryFile() {
		return this.createSummaryFile;
	}

	public void setCreateSummaryFile(boolean createSummaryFile) {
		this.createSummaryFile = createSummaryFile;
	}

	public long getDaySequence() {
		return this.daySequence;
	}

	public void setDaySequence(long daySequence) {
		this.daySequence = daySequence;
	}

	public String getSourceSystemCsopsCode() {
		return this.sourceSystemCsopsCode;
	}

	public void setSourceSystemCsopsCode(String sourceSystem) {
		this.sourceSystemCsopsCode = sourceSystem;
	}

	public String getTargetSystemCsopsCode() {
		return this.targetSystemCsopsCode;
	}

	public void setTargetSystemCsopsCode(String targetSystem) {
		this.targetSystemCsopsCode = targetSystem;
	}

	public List<ManagedFile> getReqisteredFiles() {
		return this.reqisteredFiles;
	}

	public void setReqisteredFiles(List<ManagedFile> reqisteredFiles) {
		this.reqisteredFiles = reqisteredFiles;
	}

	public String getExportDirectory() {
		return this.exportDirectory;
	}

	public void setExportDirectory(String exportDirectory) {
		this.exportDirectory = exportDirectory;
	}

	public String getAgendaCode() {
		return this.agendaCode;
	}

	public void setAgendaCode(String agendaCode) {
		this.agendaCode = agendaCode;
	}

	public synchronized ManagedFile registerExportFile(String extractName, String version, String incrementFlag, String sourceSystem, Date dataFromDate) {
		this.currentExtractName = extractName;
		this.currentExtractShortFileName = createShortFileName(extractName);
		String fullFileNameTemp = createFullFileNameTemp(this.currentExtractShortFileName);
		ManagedFile managedFile = new ManagedFile(getAgendaCode(), extractName, this.currentExtractShortFileName, fullFileNameTemp, getExtractDate(),
				getDaySequence(), sourceSystem, version, incrementFlag, dataFromDate);
		boolean canAddFile = true;
		for (ManagedFile mFile : this.reqisteredFiles) {
			if (mFile.getFileName().equals(managedFile.getFileName())) {
				canAddFile = false;
				LOGGER.debug("File already exists in list, skip. File: [{}]", fullFileNameTemp);
			}
		}
		if (canAddFile)
			this.reqisteredFiles.add(managedFile);
		LOGGER.debug("For extractName [{}] returned file name [{}]", extractName, fullFileNameTemp);
		return managedFile;
	}

	protected String createShortFileName(String extractName) {
		DateFormat extractDateFormat = new SimpleDateFormat(EXTRACT_DATE_FORMAT_PATTERN);
		String extractDateStr = extractDateFormat.format(getExtractDate());
		NumberFormat daySequenceNumberFormat = new DecimalFormat(DAY_SEQUENCE_NUMBER_FORMAR_PATTERN);
		String daySequenceStr = daySequenceNumberFormat.format(getDaySequence());
		return String.valueOf(getAgendaCode()) + "." + getSourceSystemCsopsCode() + "." + getTargetSystemCsopsCode() + "." + extractName + extractDateStr + ".A" + daySequenceStr;
	}

	protected String createFullFileNameTemp(String shortFileName) {
		String fullFileNameTemp = String.valueOf(getExportDirectory()) + File.separatorChar + shortFileName + "." + TEMP_SUFFIX;
		return fullFileNameTemp;
	}

	protected String createFullFileNameExport(String shortFileName) {
		String fullFileNameTemp = String.valueOf(getExportDirectory()) + File.separatorChar + shortFileName + "." + SEND_SUFFIX;
		return fullFileNameTemp;
	}

	public synchronized void cleanUp() {
		this.currentExtractShortFileName = null;
		this.currentExtractName = null;
	}

	public synchronized void handleJobSuccess() throws FileNotFoundException {
		cleanUp();
		if (isCreateSummaryFile())
			generateSummaryFile();
		for (ManagedFile managedFile : this.reqisteredFiles) {
			File fileTemp = new File(managedFile.getFullFileName());
			File fileExport = new File(createFullFileNameExport(managedFile.getFileName()));
			if (fileExport.exists())
				if (this.deleteExistingFile) {
					if (!fileExport.delete())
						throw new RuntimeException("Unable to delete file " + fileExport);
				} else {
					throw new RuntimeException("Unable to rename file " + fileTemp + " to file " + fileExport + " - file already exists and the deleteExistingFile option is disabled");
				}
			if (!fileTemp.renameTo(fileExport))
				throw new RuntimeException("Unable to rename file " + fileTemp + " to file " + fileExport);
		}
	}

	public synchronized void afterJob(JobExecution jobExecution) {
		try {
			if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
				handleJobSuccess();
			} else if (jobExecution.getStatus() == BatchStatus.FAILED) {
				handleJobFailure();
			} else {
				jobExecution.setStatus(BatchStatus.FAILED);
				throw new RuntimeException("Unsupported job status [" + jobExecution.getStatus() + "]");
			}
		} catch (Exception e) {
			jobExecution.setStatus(BatchStatus.FAILED);
			if (e instanceof RuntimeException)
				throw (RuntimeException)e;
			throw new RuntimeException(e);
		}
	}

	public synchronized void beforeJob(JobExecution jobExecution) {
		this.reqisteredFiles.clear();
		String daySequenceStr = null;
		try {
			long daySequence;
			daySequenceStr = jobExecution.getJobParameters().getString("daySeq");
			if (daySequenceStr == null) {
				daySequence = 1L;
				LOGGER.info("Using default day sequence 1");
			} else {
				daySequence = Long.parseLong(daySequenceStr);
			}
			setDaySequence(daySequence);
		} catch (Exception e) {
			throw new RuntimeException("Cannot process daySeq job param with value " + daySequenceStr, e);
		}
		String extractDateStr = null;
		try {
			extractDateStr = jobExecution.getJobParameters().getString("date");
			Date extractDate = (new SimpleDateFormat("yyMMdd")).parse(extractDateStr);
			setExtractDate(extractDate);
		} catch (Exception e) {
			throw new RuntimeException("Cannot process date job param with value " + extractDateStr, e);
		}
	}

	protected void generateSummaryFile() {
		try {
			DateFormat extractDateFormat = new SimpleDateFormat(EXTRACT_DATE_LONG_FORMAT_PATTERN);
			DateFormat timeFormat = new SimpleDateFormat(TIME_FORMAT_PATTERN);
			NumberFormat daySequenceNumberFormat = new DecimalFormat(DAY_SEQUENCE_NUMBER_FORMAR_PATTERN);
			List<ManagedFile> reqisteredFilesOriginal = new ArrayList<>(this.reqisteredFiles);
			Collections.sort(reqisteredFilesOriginal, new Comparator<ManagedFile>() {
				public int compare(CSOPSExportManager.ManagedFile o1, CSOPSExportManager.ManagedFile o2) {
					return o1.getName().compareTo(o2.getName());
				}
			});
			ManagedFile summaryFileTemp = registerExportFile(getSummaryFileCode(), "1.0", "F", getSourceSystem(), null);
			PrintWriter summaryFileWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(summaryFileTemp.getFullFileName()), this.encoding));
			try {
				this.csopsHeaderWriter.writeHeader(summaryFileWriter, summaryFileTemp, null);
				if (this.csopsHeaderWriter.isWriteStandardHeader())
					summaryFileWriter.print(this.lineSeparator);
				if (isSummaryFileWriteColumnsHeader()) {
					boolean firstItem = true;
					byte b;
					int i;
					String[] arrayOfString;
					for (i = (arrayOfString = getSummaryFileColumns()).length, b = 0; b < i; ) {
						String column = arrayOfString[b];
						if (!firstItem)
							summaryFileWriter.print("|");
						summaryFileWriter.print(column);
						firstItem = false;
						b++;
					}
					summaryFileWriter.print(this.lineSeparator);
				}
				for (ManagedFile managedFile : reqisteredFilesOriginal) {
					boolean firstItem = true;
					byte b;
					int i;
					String[] arrayOfString;
					for (i = (arrayOfString = getSummaryFileColumns()).length, b = 0; b < i; ) {
						String value, column = arrayOfString[b];
						if (!firstItem)
							summaryFileWriter.print("|");
						String str1;
						switch ((str1 = column).hashCode()) {
							case -1956091632:
								if (str1.equals("EXTRACT_FILE_NAME")) {
									value = managedFile.getFileName();
									break;
								}
							case -1365917835:
								if (str1.equals("EXTRACT_SOURCE_SYSTEM")) {
									value = managedFile.getSourceSystem();
									break;
								}
							case -1264068695:
								if (str1.equals("EXTRACT_NAME")) {
									value = managedFile.getName();
									break;
								}
							case -994333198:
								if (str1.equals("EXTRACT_CURRENT_DATE")) {
									value = extractDateFormat.format(managedFile.getCurrentDate());
									break;
								}
							case -974461446:
								if (str1.equals("EXTRACT_INCREMENT_FLAG")) {
									value = managedFile.getIncrementFlag();
									break;
								}
							case -967522598:
								if (str1.equals("EXTRACT_VERSION")) {
									value = managedFile.getVersion();
									break;
								}
							case -813226168:
								if (str1.equals("EXTRACT_START_TIME")) {
									value = timeFormat.format(managedFile.getStartTime());
									break;
								}
							case -483239441:
								if (str1.equals("EXTRACT_END_TIME")) {
									value = timeFormat.format(managedFile.getEndTime());
									break;
								}
							case 277067884:
								if (str1.equals("EXTRACT_DATA_FROM_DATE")) {
									value = extractDateFormat.format(managedFile.getDataFromDate());
									break;
								}
							case 594494111:
								if (str1.equals("EXTRACT_RECORD_COUNT")) {
									value = Integer.toString(managedFile.getRecordsCount());
									break;
								}
							case 748201026:
								if (str1.equals("EXTRACT_DAY_SEQUENCE")) {
									value = daySequenceNumberFormat.format(managedFile.getDaySequence());
									break;
								}
							case 1914614626:
								if (str1.equals("EXTRACT_AGENDA_CODE")) {
									value = managedFile.getAgendaCode();
									break;
								}
							default:
								throw new RuntimeException("Unexpected column " + column);
						}
						summaryFileWriter.print(value);
						firstItem = false;
						b++;
					}
					summaryFileWriter.print(this.lineSeparator);
				}
				summaryFileWriter.println(getEndOfFileMark());
			} finally {
				summaryFileWriter.close();
			}
		} catch (Exception e) {
			if (e instanceof RuntimeException)
				throw (RuntimeException)e;
			throw new RuntimeException(e);
		}
	}

	public synchronized void handleJobFailure() {
		cleanUp();
	}
}
