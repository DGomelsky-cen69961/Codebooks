package cz.csas.colmanbatch.addons.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.batch.item.ExecutionContext;

/**
 * This helper class is used to simplify manipulation with scopes 
 */
public class ScopeHelper {
	/**
	 * This TL represents a step-scope storage, it replaces the executionContext which is supposed to do that job, but it somehow
	 * does not work for parallel flows
	 * TODO add clean-up procedure of TL
	 */
	private static ThreadLocal<Map<String, Object>> currentStepManagedFileTL = new ThreadLocal<Map<String, Object>>() {
	    protected Map<String, Object> initialValue() {
	        return new ConcurrentHashMap<String, Object>();
	    }
	};
	
	public static Object getCurrentStepProperty(ExecutionContext executionContext, String key) {
		// the executionContext parameter is currently not used, because it seems that it does not work with parallel flows (stange NPE occurs)
		return currentStepManagedFileTL.get().get(key);
	}

	public static void setCurrentStepProperty(ExecutionContext executionContext, String key, Object value) {
		// the executionContext parameter is currently not used, because it seems that it does not work with parallel flows (stange NPE occurs)
		currentStepManagedFileTL.get().put(key, value);
	}
}
