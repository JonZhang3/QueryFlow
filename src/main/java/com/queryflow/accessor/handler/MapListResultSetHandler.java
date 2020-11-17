package com.queryflow.accessor.handler;

import com.queryflow.common.ResultMap;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 将返回结果封装成 Map List
 *
 * @author Jon
 * @since 1.0.0
 */
public class MapListResultSetHandler implements ResultSetHandler<List<ResultMap>> {

    @Override
    public List<ResultMap> handle(ResultSet rs) throws SQLException {
        List<ResultMap> result = new LinkedList<>();
        while (rs.next()) {
            ResultMap map = new ResultMap();
            MapResultSetHandler.fillMap(rs, rs.getMetaData(), map);
            result.add(map);
        }
        return result;
    }

}
