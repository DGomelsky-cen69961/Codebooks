package cz.csas.colmanbatch.addons.processor;

import hu.appello.webdp.ejb.core.CrudManager;
import hu.appello.webdp.util.EJBHelper;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.InitializingBean;

import cz.csas.colmanejb.batch.logging.DbLogProcessor;

public abstract class StoredProcedureItemProcessor<I> implements ItemProcessor<I, Object>, InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(StoredProcedureItemProcessor.class);
    private CrudManager crudManager = (CrudManager)EJBHelper.getInstance().getEjb(CrudManager.class);
    private String sql;

    /**
     * Name of the processor - is used in logging
     */
    private String processorName; 
    
	public String getProcessorName() {
		return processorName;
	}

	public void setProcessorName(String processorName) {
		this.processorName = processorName;
	}

	public void afterPropertiesSet() throws Exception {
        if (this.sql == null) {
            throw new IllegalArgumentException("sql statement is required!");
        }
    }

    public String getSql() {
        return this.sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

	@Override
	public Object process(I item) throws Exception {
        EntityManager em = this.crudManager.getEntityManager();
        final Session session = (Session)em.unwrap(Session.class);

        logProcessing(item);
        
		// we use final local variable to be able to pass item to the doWork(...) block
		final I itemLocal = item;
        session.doWork((Work)new Work(){
            public void execute(Connection connection) throws SQLException {
                CallableStatement stmt = null;
                DbLogProcessor dbLogProcessor = null;
                try {
                    dbLogProcessor = new DbLogProcessor(connection, session, logger);
                    dbLogProcessor.beforeCall();
                    logger.debug("Stored procedure started:" + sql);
                    stmt = connection.prepareCall(sql);
                    bindParameters(stmt, itemLocal);
                    stmt.execute();
                    logger.debug("Stored procedure finished:" + sql);
                }
                catch (Exception exc) {
                    throw new SQLException(exc);
                }
                finally {
                	if (dbLogProcessor != null) {
                		dbLogProcessor.afterCall();
                	}
                	
                	if(stmt != null) {
                		stmt.close();
                	}
                }
            }
        });

		return null;
	}
	
	

	public abstract void bindParameters(CallableStatement stmt, I item) throws SQLException;
	
	public void logProcessing(I item) {
		if (processorName != null) {
			logger.info("Executing" + processorName);
		}
	}
}

