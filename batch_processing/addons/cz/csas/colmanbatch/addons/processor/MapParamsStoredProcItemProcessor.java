package cz.csas.colmanbatch.addons.processor;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MapParamsStoredProcItemProcessor extends StoredProcedureItemProcessor<Map<String, Object>> {
    private static final Logger logger = LoggerFactory.getLogger(MapParamsStoredProcItemProcessor.class);
	    
    protected Map<String, String> paramsMapping;

    public Map<String, String> getParamsMapping() {
		return paramsMapping;
	}

	public void setParamsMapping(Map<String, String> paramsMapping) {
		this.paramsMapping = paramsMapping;
	}

	@Override
	public void bindParameters(CallableStatement stmt , Map<String, Object> item) throws SQLException {
        if (paramsMapping != null) {
            for (Map.Entry<String, String> entry : paramsMapping.entrySet()) {
                String paramName = entry.getKey(); 
                String itemKey = entry.getValue(); 
                Object paramValue = item.get(itemKey);
                logger.debug("Bind " + paramName + " parameter with key " + itemKey + ": " + paramValue);
                stmt.setObject(paramName, paramValue);
            }
        }		
	}
    
	public void logProcessing(Map<String, Object> item) {
		if (getProcessorName() != null) {
			StringBuilder parametersDesc = new StringBuilder();
            for (Map.Entry<String, String> entry : paramsMapping.entrySet()) {
                String paramName = entry.getKey(); 
                String itemKey = entry.getValue(); 
                Object paramValue = item.get(itemKey);
                if (parametersDesc.length() > 0) {
                    parametersDesc.append(", ");
                }
                parametersDesc.append(paramName + ": " + paramValue);
            }

			logger.info("Executing " + getProcessorName() + " with parameters (" + parametersDesc.toString() + ")");
		}
	}
    

}
