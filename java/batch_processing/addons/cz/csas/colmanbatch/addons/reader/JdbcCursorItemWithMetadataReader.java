package cz.csas.colmanbatch.addons.reader;

import java.io.Serializable;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.database.JdbcCursorItemReader;

import cz.csas.colmanbatch.addons.reader.ItemStreamMetadata;
import cz.csas.colmanbatch.addons.reader.ItemStreamMetadataProvider;

public class JdbcCursorItemWithMetadataReader<T> extends JdbcCursorItemReader<T> implements ItemStreamMetadataProvider{

	public static class RsItemStreamMetadata implements Serializable, ItemStreamMetadata {
		String[] fieldNames;
		
		public String[] getFieldNames() {
			return fieldNames;
		}

		public RsItemStreamMetadata(String[] fieldNames) {
			super();
			this.fieldNames = fieldNames;
		}
	};
	
	public ItemStreamMetadata getItemStreamMetadata() {
		try {
			ResultSetMetaData mData = rs.getMetaData();
			
			final String[] columnNames = new String[mData.getColumnCount()];
			for (int i = 0; i < columnNames.length;i++) {
				columnNames[i] = mData.getColumnName(i + 1);
			}
			
			return new RsItemStreamMetadata(columnNames);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public synchronized void close() throws ItemStreamException {
		super.close();
	}

	@Override
	public synchronized void open(ExecutionContext executionContext) throws ItemStreamException {
		super.open(executionContext);
	}

	@Override
	public synchronized T read() throws Exception, UnexpectedInputException, ParseException {
		return super.read();
	}

	@Override
	public synchronized void update(ExecutionContext executionContext) throws ItemStreamException {
		super.update(executionContext);
	}
	
}
