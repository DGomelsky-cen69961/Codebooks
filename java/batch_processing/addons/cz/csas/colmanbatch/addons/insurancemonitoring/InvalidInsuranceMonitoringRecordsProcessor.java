package cz.csas.colmanbatch.addons.insurancemonitoring;

import cz.csas.colman.model.collateralsandassets.realestate.PropertyCertificate;
import cz.csas.colmanbatch.processor.FaultTolerantItemProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;


public class InvalidInsuranceMonitoringRecordsProcessor extends FaultTolerantItemProcessor<Map<String, Object>, Object> {

    protected Logger logger = LoggerFactory.getLogger(cz.csas.colmanbatch.addons.insurancemonitoring.InvalidInsuranceMonitoringRecordsProcessor.class);

    protected String checkIdFieldName = "CHECK_ID";
    protected String completionDateFieldName = "COMPLETION_DATE";
    protected String lastResolverFieldName = "LAST_RESOLVER";

    public InvalidInsuranceMonitoringRecordsProcessor() {
    }

    @Override
    public String getErrorText(Map<String, Object> item) {
        String checkId = item.get(checkIdFieldName).toString();
        String completionDate = item.get(completionDateFieldName).toString();
        String lastResolver = item.get(lastResolverFieldName).toString();

        return "Insurance monitoring was not processed, because the INSURANCE_ID field is null. Parameters: CHECK_ID: "
                + checkId + ", COMPLETION_DATE: " + completionDate + ", LAST_RESOLVER:" + lastResolver;
    }

    public Object processItem(Map<String, Object> item) throws Exception {
        String checkId = item.get(checkIdFieldName).toString();
        String completionDate = item.get(completionDateFieldName).toString();
        String lastResolver = item.get(lastResolverFieldName).toString();

        logger.warn("Insurance monitoring was not processed, because the INSURANCE_ID field is null. Parameters: CHECK_ID: "
                + checkId + ", COMPLETION_DATE: " + completionDate + ", LAST_RESOLVER:" + lastResolver);

        return null;
    }
}


