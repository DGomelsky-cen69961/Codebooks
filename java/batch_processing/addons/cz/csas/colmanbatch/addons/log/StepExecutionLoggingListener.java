package cz.csas.colmanbatch.addons.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

public class StepExecutionLoggingListener implements StepExecutionListener {

	private final static Logger LOGGER = LoggerFactory.getLogger(StepExecutionLoggingListener.class);

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		LOGGER.info("Step " + stepExecution.getStepName() + " finished");
		return null;
	}

	@Override
	public void beforeStep(StepExecution stepExecution) {
		LOGGER.info("Step " + stepExecution.getStepName() + " started");

	}

}
