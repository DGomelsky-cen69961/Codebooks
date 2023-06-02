package cz.csas.colmanbatch.addons.util;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.FieldPosition;
import java.text.Format;
import java.text.MessageFormat;
import java.text.ParsePosition;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import org.springframework.beans.factory.InitializingBean;

public class CodeTableConvertor extends Format implements InitializingBean {

	protected String codeTable;
	protected String targetSystem;
	
	protected Map<String, String> mapping = new ConcurrentHashMap<String, String>(); 
	
    private DataSource dataSource;

    
	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public String getCodeTable() {
		return codeTable;
	}

	public void setCodeTable(String codeTable) {
		this.codeTable = codeTable;
	}

	public String getTargetSystem() {
		return targetSystem;
	}

	public void setTargetSystem(String targetSystem) {
		this.targetSystem = targetSystem;
	}

	public CodeTableConvertor() {
	}
	
	public CodeTableConvertor(String codeTable, String targetSystem, DataSource dataSource) {
		this.codeTable = codeTable;
		this.targetSystem = targetSystem;
		this.dataSource = dataSource;
	}

	public void afterPropertiesSet() throws Exception {
		PreparedStatement ps = dataSource.getConnection().prepareStatement("SELECT code_table_code, code_table_item_name, mapped_value FROM cms_code_table_map WHERE is_primary_out = 1 AND code_table_code = ? AND system = ?");
		ps.setString(1, codeTable);		
		ps.setString(2, targetSystem);
		ResultSet rs = ps.executeQuery();
		
		while (rs.next()) {
			String itemName = rs.getString(2);
			String mappedValue = rs.getString(3);
			if (itemName != null && mappedValue != null) {
				mapping.put(itemName, mappedValue);
			}
		}
	}

	@Override
	public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {

		String sourceKey = obj.toString();
		String target = mapping.get(sourceKey);
		if (target == null) {
			target = sourceKey;
			//throw new IllegalArgumentException(MessageFormat.format("Cannot convert {0} (code table {1}, target system {2}) - mapping not found", sourceKey, codeTable, targetSystem));
		}
		toAppendTo.append(target);
		return toAppendTo;
	}

	@Override
	public Object parseObject(String source, ParsePosition pos) {
		throw new UnsupportedOperationException("This operation is not supported");
	}

}
