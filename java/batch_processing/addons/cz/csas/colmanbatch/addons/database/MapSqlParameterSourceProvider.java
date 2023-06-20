package cz.csas.colmanbatch.addons.database;

import java.util.Map;
import java.util.Map.Entry;

import org.springframework.batch.item.database.ItemSqlParameterSourceProvider;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

public class MapSqlParameterSourceProvider implements ItemSqlParameterSourceProvider<Map<String, Object>> {	
	/** {@inheritDoc} */
	@Override
	public SqlParameterSource createSqlParameterSource(Map<String, Object> item) {
		MapSqlParameterSource sps = new MapSqlParameterSource();
		for (Entry<String, Object> entry : item.entrySet()) {
			sps.addValue(entry.getKey(), entry.getValue());
		}
		return sps;
	}
}
