package cz.csas.colmanbatch.addons.propertycertificate;

import cz.csas.colman.model.collateralsandassets.realestate.PropertyCertificate;
import cz.csas.colmanapi.propertycertificate.PropertyCertificateManager;
import cz.csas.colmanbatch.processor.FaultTolerantItemProcessor;
import cz.csas.colmanbatch.util.BatchUtil;
import hu.appello.webdp.util.EJBHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Map;


public class DeleteOutdatedPropertyCertificateProcessor extends FaultTolerantItemProcessor<Map<String, Object>, PropertyCertificate> {
    private final PropertyCertificateManager pcManager = (PropertyCertificateManager) EJBHelper.getInstance().getEjb(PropertyCertificateManager.class);

    protected Logger logger = LoggerFactory.getLogger(DeleteOutdatedPropertyCertificateProcessor.class);

    protected String idFieldName = "ID";

    public DeleteOutdatedPropertyCertificateProcessor() {
    }

    public String getErrorText(Map<String, Object> item) {
        Object partnerId = item.get(idFieldName);
        return "Failed to process PropertyCertificate with id: " + partnerId;
    }

    public PropertyCertificate processItem(Map<String, Object> item) throws Exception {
        BigDecimal pcId = (BigDecimal) item.get(idFieldName);
        logger.info("Processing PropertyCertificate with id: " + pcId);
        long start = System.currentTimeMillis();

        this.pcManager.deletePC(pcId.longValue(), BatchUtil.createExectionContext(), true);

        long end = System.currentTimeMillis();
        logger.debug("Processing PropertyCertificate with id: " + pcId + " finished, it took " + (end - start) + " ms");

        return null;
    }
}


