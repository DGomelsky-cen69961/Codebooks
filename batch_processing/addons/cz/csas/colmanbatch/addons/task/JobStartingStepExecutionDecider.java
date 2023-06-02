package cz.csas.colmanbatch.addons.task;

import java.security.InvalidParameterException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;

public class JobStartingStepExecutionDecider implements JobExecutionDecider {
    private static final Logger logger = LoggerFactory.getLogger((Class)JobStartingStepExecutionDecider.class);
    public static final String START_PREFIX = "START:";
    public static final String COMPLETED_PREFIX = "COMPLETED:";
    public static final String END_STEP = "END";
    public static String DEFAULT_STEP = "DEFAULT_START";
    public static String START_STEP_PARAM = "startStep";
    public static String RUN_STEP_PARAM = "runStep";

    public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
        String runStep = jobExecution.getJobParameters().getString(RUN_STEP_PARAM);
        String startStep = jobExecution.getJobParameters().getString(START_STEP_PARAM);
        
        logger.debug("Input parameters: runStep: " + runStep + ", startStep: " + startStep);

        String resultStatus = null;

        decideResultStatus:
        do {
	        if (runStep != null && startStep != null) {
	            throw new InvalidParameterException("Only one of  parameters " + RUN_STEP_PARAM + " and " + START_STEP_PARAM + " can be specified");
	        }
	        if (runStep != null) {
	            if (stepExecution != null) {
	                logger.info("One step executed, exiting");
	                resultStatus = "END";
	                break decideResultStatus;
	            }
	            logger.info("Running only step: " + runStep);
	            resultStatus = "START:" + startStep;
                break decideResultStatus;
	        }
	        if (stepExecution == null) {
	            if (startStep != null) {
	                logger.info("Defined job starting step: " + startStep);
	                resultStatus = "START:" + startStep;
	                break decideResultStatus;
	            }
	            logger.debug("Job started from the default step");
	            resultStatus = DEFAULT_STEP;
                break decideResultStatus;
	        }
	        
	        resultStatus="COMPLETED:" + stepExecution.getStepName();
            break decideResultStatus;
        } while(false);
        
        logger.debug("Returned status:" + resultStatus);
        return new FlowExecutionStatus(resultStatus);
    }
}

