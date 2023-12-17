package spring.app.DATABASE;


import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CheckMapper implements RowMapper<Boolean> {
    @Override
    public Boolean mapRow(ResultSet resultSet, int i) throws SQLException {
        return resultSet.getInt("answer") != 0;
    }


}
