package cz.csas.colmanbatch.addons.functionalunit;

import cz.csas.colmanbatch.addons.csops.CSOPSExportManager;
import cz.csas.colmanbatch.addons.csops.CSOPSExportManager.ManagedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.io.*;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ArchiveDirectoryTasklet implements Tasklet {

    /**
     * A constants for buffer size used to read/write data
     */
    private static final int BUFFER_SIZE = 4096;
    private static final String EXTRACT_NAME = "FU";
    private static final String EXTRACT_VERSION = "1.0";
    private static final String INCREMENT_FLAG = "F";
    private static final String SOURCE_SYSTEM = "COLMAN";

    protected Logger logger = LoggerFactory.getLogger(ArchiveDirectoryTasklet.class);

    private String directoryToZip;

    private CSOPSExportManager manager;

    /**
     * This tasklet zips the contents of the given directory into the CSOPSExportManager registered file
     * The created zip is then sended to CSOPS
     */
    public RepeatStatus execute(StepContribution contribution,
                                ChunkContext chunkContext) throws Exception {

        zip();

        return RepeatStatus.FINISHED;
    }

    /**
     * Compresses a list of files to a destination zip file
     */
    private void zip() throws IOException {
        ManagedFile managedFile = manager.registerExportFile(EXTRACT_NAME, EXTRACT_VERSION, INCREMENT_FLAG, SOURCE_SYSTEM, new Date());

        logger.info("Zipping directory: " + directoryToZip + " to file: {}", managedFile.getFullFileName());

        File dir = new File(directoryToZip);
        File[] filesToZip = dir.listFiles();

        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(managedFile.getFullFileName()));
        for (File file : filesToZip) {
            zipFile(file, zos);
        }
        zos.flush();
        zos.close();
    }

    /**
     * Adds a file to the current zip output stream
     *
     * @param file the file to be added
     * @param zos  the current zip output stream
     */
    private void zipFile(File file, ZipOutputStream zos)
            throws FileNotFoundException, IOException {
        zos.putNextEntry(new ZipEntry(file.getName()));
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(
                file));
        long bytesRead = 0;
        byte[] bytesIn = new byte[BUFFER_SIZE];
        int read = 0;
        while ((read = bis.read(bytesIn)) != -1) {
            zos.write(bytesIn, 0, read);
            bytesRead += read;
        }
        zos.closeEntry();
    }

    public void setDirectoryToZip(String directoryToZip) {
        this.directoryToZip = directoryToZip;
    }

    public void setManager(CSOPSExportManager manager) {
        this.manager = manager;
    }
}
