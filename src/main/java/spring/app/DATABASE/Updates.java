package spring.app.DATABASE;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import spring.app.weather.mainWeather.Weather;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Component
public class Updates {
    private Connection connection;
    private PreparedStatement preparedStatement;

    private JdbcTemplate jdbcTemplate;

    private GetInfo getInfo;
    private Weather weather;

    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Autowired
    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    @Autowired
    public void setGetInfo(GetInfo getInfo) {
        this.getInfo = getInfo;
    }

    @Autowired
    public void setWeather(Weather weather) {
        this.weather = weather;
    }

    public void singUp(String username, String password) throws SQLException {
        if (!(getInfo.check(username))) {
            jdbcTemplate.update("INSERT INTO users(username, password) VALUES (?, ?)", username, password);
        }
    }

    public void addCity(String city) {
        try {
            if (!(getInfo.checkCity(city)) && weather.getResponseState()) {
                jdbcTemplate.update("INSERT INTO cities(city) VALUES (?)", city);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

    }

    public void addInformationAboutCities(String city, String weatherToday, String weatherTomorrow, String weatherFor10Days,
                                          String tempToday, String tempTomorrow, String tempFor10Days, String tempForHours, String tempAndWeatherNow, String sunsetAndSunrise) {
        jdbcTemplate.update("UPDATE cities SET " +
                "weatherToday = ?, tempToday = ?, weatherTomorrow = ?, tempTomorrow = ?, weatherForTenDays = ?, tempForTenDays = ?, tempForHours = ?, tempAndWeatherNow = ?, sunsetAndSunrise = ? WHERE city = ?", weatherToday, tempToday, weatherTomorrow, tempTomorrow, weatherFor10Days, tempFor10Days, tempForHours, tempAndWeatherNow, sunsetAndSunrise, city);
    }
}