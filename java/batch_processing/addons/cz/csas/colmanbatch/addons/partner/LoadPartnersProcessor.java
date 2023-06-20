package cz.csas.colmanbatch.addons.partner;

import hu.appello.webdp.api.businessmethod.MethodExecutionContext;
import hu.appello.webdp.util.EJBHelper;

import java.math.BigDecimal;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

import cz.csas.colman.model.partner.Partner;
import cz.csas.colmanapi.partner.PartnerManager;
import cz.csas.colmanapi.thread.MethodExecutionContextTreadLocal;
import cz.csas.colmanbatch.processor.FaultTolerantItemProcessor;
import cz.csas.colmanbatch.util.BatchUtil;

public class LoadPartnersProcessor extends FaultTolerantItemProcessor<Map<String, Object>, Partner> {	

	// shared entities
	protected MethodExecutionContext methodExecutionContext;
	
	protected PartnerManager partnerManager = (PartnerManager)EJBHelper.getInstance().getEjb((Class)PartnerManager.class);
	
	protected Logger logger = LoggerFactory.getLogger(LoadPartnersProcessor.class);

	// injected resources
	protected Long waitTimeMsecs;

	protected String idFieldName = "ID";

	protected String loadPartnerTagRegex ;

	protected String partnerLoadedTag; 
	
	protected boolean ignoreServiceErrors = true;

	public boolean isIgnoreServiceErrors() {
		return ignoreServiceErrors;
	}

	public void setIgnoreServiceErrors(boolean ignoreServiceErrors) {
		this.ignoreServiceErrors = ignoreServiceErrors;
	}

	public Long getWaitTimeMsecs() {
		return waitTimeMsecs;
	}

	public void setWaitTimeMsecs(Long waitTimeMsecs) {
		this.waitTimeMsecs = waitTimeMsecs;
	}

	public String getLoadPartnerTagRegex() {
		return loadPartnerTagRegex;
	}

	public void setLoadPartnerTagRegex(String forceRefreshPartnerRegex) {
		this.loadPartnerTagRegex = forceRefreshPartnerRegex;
	}

	
    public String getPartnerLoadedTag() {
		return partnerLoadedTag;
	}

	public void setPartnerLoadedTag(String partnerRefreshedTag) {
		this.partnerLoadedTag = partnerRefreshedTag;
	}

	public String getIdFieldName() {
		return idFieldName;
	}

	public void setIdFieldName(String idFieldName) {
		this.idFieldName = idFieldName;
	}

	public LoadPartnersProcessor() {
		methodExecutionContext = (MethodExecutionContext)BatchUtil.createExectionContext();
		Map<String, String> params = Maps.newConcurrentMap();
		params.put("forceClientLoad", "true");
		methodExecutionContext.setMethodExecutionParameters(params);
	}

    public String getErrorText(Map<String, Object> item) {
    	Object partnerId = item.get(idFieldName);
        return "Failed to process partner with id: " + partnerId;
    }

    public Partner processItem(Map<String, Object> item) throws Exception {
        MethodExecutionContextTreadLocal.set(methodExecutionContext);
        
        if (waitTimeMsecs != null) {
        	logger.debug("Waiting " + waitTimeMsecs + " msec before next client load");
        	Thread.sleep(waitTimeMsecs);
        }

        BigDecimal partnerId = (BigDecimal) item.get(idFieldName);
    	logger.info("Loading partner with partner.id=" + partnerId);

    	Partner partner = partnerManager.findPartnerById(partnerId.longValue());
    	if (partner == null) {
    		throw new RuntimeException("Cannot find client with id="+ partnerId);
    	}
    	
    	boolean clientLoaded = false;
    	try {
    		partner = partnerManager.loadClientData(partner, methodExecutionContext);
    		clientLoaded = true;
    	} catch (cz.csas.integration.CSValidationException e) {
    		if (isIgnoreServiceErrors()) {
		        logger.warn("Cannot load partner with partner.id=" + partner.getId() + ", partner.cluid=" + partner.getCluid() +" - a service call failed with error: " + e.getMessage());
		    	logger.debug("Service call exception details", e);
    		} else {
    			throw e;
    		}
	    }

		
    	if (clientLoaded) {
	    	if (!StringUtils.isEmpty(loadPartnerTagRegex)) {
		    	String processingFlags = partner.getProcessingFlags();
		    	processingFlags = Pattern.compile(loadPartnerTagRegex).matcher(processingFlags).replaceAll(partnerLoadedTag);
		    	logger.debug("For partner with partner.id=" + partnerId + " - setting new processingFlags="+ processingFlags);

		    	partner.setProcessingFlags(processingFlags);
	    	}
    	}

    	logger.debug("Loading partner with partner.id=" + partnerId + " finished");

    	return partner;
    }
}
