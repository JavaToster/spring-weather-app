package spring.app.DATABASE;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

@Component
public class GetInfo {

    private JdbcTemplate jdbcTemplate;

    private  Connection connection;
    private ResultSet resultSet;

    private final int days = 9;

    @Autowired
    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean check(String username) throws SQLException{
        return jdbcTemplate.query("SELECT COUNT(*) AS answer FROM users WHERE username = ?", new Object[]{username}, new CheckMapper()).stream().findAny().orElse(false);
    }

    public boolean checkPassword(String username, String password) throws SQLException{
        return jdbcTemplate.query("SELECT COUNT(*) AS answer FROM users WHERE username=? AND password=?", new Object[]{username, password}, new CheckMapper()).stream().findAny().orElse(false);
    }

    public boolean checkCity(String city) throws SQLException{
        return jdbcTemplate.query("SELECT COUNT(*) AS answer FROM cities WHERE city = ?", new Object[]{city}, new CheckMapper()).stream().findAny().orElse(false);
    }

    public void startGetInfoFromDB(String city) throws SQLException{
        PreparedStatement preparedStatement = this.connection.prepareStatement("SELECT * FROM cities WHERE city = ?");

        preparedStatement.setString(1, city);

        this.resultSet = preparedStatement.executeQuery();

        this.resultSet.next();
    }

    public String[][] getWeather() throws SQLException{
        String[][] weather = new String[3][4];

        String[] columnLabel = {"weathertoday", "weathertomorrow", "weatherfortendays"};

        for(int i = 0; i<3; i++){
            weather[i] = this.resultSet.getString(columnLabel[i]).split("%");
        }

        return weather;
    }

    public String[][] getTemp() throws SQLException{
        String[][] temp = new String[3][4];

        String[] columnLabel = {"temptoday", "temptomorrow", "tempfortendays"};

        for(int i = 0; i<3  ; i++){
            temp[i] = this.resultSet.getString(columnLabel[i]).split("%");
        }

        return temp;
    }

    public String[][] getWeatherAndAstro() throws SQLException{
        String[][] astroAndWeatherNow = new String[2][2];

        System.out.println(resultSet.getString("sunsetAndSunrise"));

        astroAndWeatherNow[0] = resultSet.getString("sunsetAndSunrise").split("%");
        astroAndWeatherNow[1] = resultSet.getString("tempAndWeatherNow").split("%");

        return astroAndWeatherNow;
    }

    public String[] getTempForHours() throws SQLException{
        return resultSet.getString("tempForHours").split("%");
    }
}
