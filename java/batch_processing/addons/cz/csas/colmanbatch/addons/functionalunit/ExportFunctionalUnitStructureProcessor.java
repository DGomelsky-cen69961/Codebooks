package cz.csas.colmanbatch.addons.functionalunit;


import cz.csas.colmanbatch.processor.FaultTolerantItemProcessor;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.*;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.Random;

import static cz.csas.colmanbatch.addons.functionalunit.MW_RECollateralEvaluationService_getREAssetData_v02_02Request.GET_RE_ASSET_DATA_REQUEST;

public class ExportFunctionalUnitStructureProcessor extends FaultTolerantItemProcessor<Map<String, Object>, String> {

    protected Logger logger = LoggerFactory.getLogger(ExportFunctionalUnitStructureProcessor.class);

    protected String idFieldName = "asset_id";

    protected String destinationURL;
    protected String trnsrc;
    protected String trnuser;
    protected String trnsrcname;
    protected String username;
    protected String password;

    protected String directory;

    public ExportFunctionalUnitStructureProcessor() {
//        try {
//            SSLContext sc = SSLContext.getInstance("SSL");
//            sc.init(null, trustAllCerts, new java.security.SecureRandom());
//            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
//        } catch (NoSuchAlgorithmException | KeyManagementException ignored) {
//        }
    }

    public String getErrorText(Map<String, Object> item) {
        Object functionalUnitId = item.get(idFieldName);
        return "Failed to process Functional unit with id: " + functionalUnitId;
    }

    public String processItem(Map<String, Object> item) throws Exception {
        BigDecimal fuId = new BigDecimal((String) item.get(idFieldName));

        logger.info("Processing Functional unit with id: " + fuId);
        long start = System.currentTimeMillis();

        HttpsURLConnection con = prepareConnection();
        String request = prepareRequest(fuId);
        logger.debug("Request :\n" + request);
        proceedCommunication(con, request, fuId);

        long end = System.currentTimeMillis();
        logger.debug("Processing Functional with id: " + fuId + " finished, it took " + (end - start) + " ms");

        return null;
    }

    /**
     * It prepares the connection to the destination service URL
     * It prepares all the headers like http method, basic authorization, etc.
     * @return preprade HttpsURLConnection object
     * @throws IOException
     */
    private HttpsURLConnection prepareConnection() throws IOException {
        // Create a neat value object to hold the URL
        URL url = new URL(destinationURL);

        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();

        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "text/xml");
        con.setRequestProperty("Accept", "text/xml");
        con.setDoOutput(true);

        // authorization
        String auth = username + ":" + password;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
        con.setRequestProperty("Authorization", "Basic " + encodedAuth);

        return con;
    }

    /**
     * It prepares the request for the given functional unit
     *
     * @param fuId id of the functional unit for which the service should be called
     * @return prepared request in string
     */
    private String prepareRequest(BigDecimal fuId) {
        String messageID = generateRandomMessageId();
        String messageDate = getMessageDate();

        return String.format(GET_RE_ASSET_DATA_REQUEST, messageID, trnsrc, trnsrcname, messageID, trnuser, messageDate, fuId);
    }

    /**
     * It calls the service with the given request. After successful call, the response is saved into the xml file
     *
     * @param con prepared connection object
     * @param request request in the string
     * @param fuId id of the functional unit
     * @throws IOException
     */
    private void proceedCommunication(HttpsURLConnection con, String request, BigDecimal fuId) throws IOException {
        // prepare file
        String filePathAndName = directory + fuId.toString() + ".xml";
        File file = new File(filePathAndName);

        // request
        try (OutputStream os = con.getOutputStream()) {
            byte[] input = request.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        OutputStream outStream = new FileOutputStream(file);
        byte[] buffer = new byte[8 * 1024];
        int bytesRead;
        // response
        try (InputStream is = con.getInputStream()) {
            while ((bytesRead = is.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }
        } catch (Exception e) {
            // delete current file on error, because even if nothing was written, the file was already created
            IOUtils.closeQuietly(outStream);
            file.delete();
            throw e;
        }
        IOUtils.closeQuietly(outStream);
    }

    private String generateRandomMessageId() {
        Random r = new Random();

        long n = Math.abs(r.nextLong() % 10000000000L);
        return String.valueOf(n);
    }

    private String getMessageDate() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        return formatter.format(date);
    }

    // Create a trust manager that does not validate certificate chains
//    TrustManager[] trustAllCerts = new TrustManager[]{
//            new X509TrustManager() {
//                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
//                    return new X509Certificate[0];
//                }
//
//                public void checkClientTrusted(
//                        java.security.cert.X509Certificate[] certs, String authType) {
//                }
//
//                public void checkServerTrusted(
//                        java.security.cert.X509Certificate[] certs, String authType) {
//                }
//            }
//    };

    public void setDestinationURL(String destinationURL) {
        this.destinationURL = destinationURL;
    }

    public void setTrnsrc(String trnsrc) {
        this.trnsrc = trnsrc;
    }

    public void setTrnuser(String trnuser) {
        this.trnuser = trnuser;
    }

    public void setTrnsrcname(String trnsrcname) {
        this.trnsrcname = trnsrcname;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }
}
