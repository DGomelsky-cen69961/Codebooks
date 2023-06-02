package cz.csas.colmanbatch.addons.functionalunit;


import org.apache.commons.io.FileUtils;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.io.File;

public class PrepareDirectoryTasklet implements Tasklet {

    private String directory;

    /**
     * This tasklet creates or deletes the contents of the given directory
     */
    public RepeatStatus execute(StepContribution contribution,
                                ChunkContext chunkContext) throws Exception {

        File dir = new File(directory);
        FileUtils.forceMkdir(dir); // Makes a directory, including any necessary but nonexistent parent directories
        FileUtils.cleanDirectory(dir); // Cleans a directory without deleting it.

        return RepeatStatus.FINISHED;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }
}
