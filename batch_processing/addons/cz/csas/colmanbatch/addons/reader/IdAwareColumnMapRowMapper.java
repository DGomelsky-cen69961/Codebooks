package cz.csas.colmanbatch.addons.reader;

import org.springframework.jdbc.core.ColumnMapRowMapper;

import java.util.Map;

public class IdAwareColumnMapRowMapper extends ColumnMapRowMapper {

    private String[] idFields;

    public String[] getIdFields() {
        return idFields;
    }

    public void setIdFields(String[] idFields) {
        this.idFields = idFields;
    }

    protected Map<String, Object> createColumnMap(int columnCount) {
        Map<String, Object> originalMap = super.createColumnMap(columnCount);
        if (idFields  == null || idFields.length == 0 ) {
            return originalMap;
        }

        return new IdAwareMap<String, Object>(originalMap, idFields);
    }

}
