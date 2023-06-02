package cz.csas.colmanbatch.addons.util;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by randras on 2014.03.21..
 */
public class CSOPSFile implements Serializable {

	private final static Logger logger = LoggerFactory.getLogger(CSOPSFile.class);

	public static enum Suffix {
		TEMP, SEND, ARCH, PROC
	}

	public final static SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMdd");

	private String pathToFile;

	private String fileName;

	private String agenda;

	private String sourceNodeName;

	private String targetNodeName;

	private String fileCode;

	private Date date;

	private String daySequence;

	private Suffix suffix;

	public CSOPSFile() {
	}

	public CSOPSFile(String fullPath) {
		this(new File(fullPath).getParent(), new File(fullPath).getName());
	}

	public CSOPSFile(String pathToFile, String fileName) {
		this.pathToFile = pathToFile;
		this.fileName = fileName;
		String[] parts = fileName.split("\\.");
		agenda = parts[0];
		sourceNodeName = parts[1];
		targetNodeName = parts[2];
		fileCode = parts[3].substring(0, 2);
		try {
			date = dateFormat.parse(parts[3].substring(2));
		} catch (ParseException e) {
			logger.error("Cant parse data of csops file", e);
			date = null;
		}
		daySequence = parts[4];
		suffix = Suffix.valueOf(parts[5]);
	}

	public String getFileName() {
		if (fileName != null) {
			return fileName;
		}
		return String.format("%s.%s.%s.%s%s.%s.%s", agenda, sourceNodeName, targetNodeName, fileCode, dateFormat.format(date), daySequence,
				suffix.name());
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getPathToFile() {
		return pathToFile;
	}

	public void setPathToFile(String pathToFile) {
		this.pathToFile = pathToFile;
	}

	public String getAgenda() {
		return agenda;
	}

	public void setAgenda(String agenda) {
		this.agenda = agenda;
	}

	public String getSourceNodeName() {
		return sourceNodeName;
	}

	public void setSourceNodeName(String sourceNodeName) {
		this.sourceNodeName = sourceNodeName;
	}

	public String getTargetNodeName() {
		return targetNodeName;
	}

	public void setTargetNodeName(String targetNodeName) {
		this.targetNodeName = targetNodeName;
	}

	public String getFileCode() {
		return fileCode;
	}

	public void setFileCode(String fileCode) {
		this.fileCode = fileCode;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getDaySequence() {
		return daySequence;
	}

	public void setDaySequence(String daySequence) {
		this.daySequence = daySequence;
	}

	public Suffix getSuffix() {
		return suffix;
	}

	public void setSuffix(Suffix suffix) {
		this.suffix = suffix;
	}

	public boolean createIfNotExists() {
		return createIfNotExists(this.pathToFile);
	}

	public boolean createIfNotExists(String pathToFile) {
		File directory = new File(pathToFile);
		if (!directory.exists()) {
			directory.mkdir();
		}

		File file = new File(pathToFile + getFileName());
		if (!file.exists()) {
			try {
				return file.createNewFile();
			} catch (IOException e) {
				logger.error("cant create file");
			}
		}
		return false;
	}
}
