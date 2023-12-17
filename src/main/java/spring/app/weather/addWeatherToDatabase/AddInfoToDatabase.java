package spring.app.weather.addWeatherToDatabase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spring.app.DATABASE.Updates;
import spring.app.weather.mainWeather.Weather;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


@Component
public class AddInfoToDatabase extends Thread{
    private Connection connection;

    private PreparedStatement preparedStatement;

    private ResultSet resultSet;

    private Weather weather;
    private Updates updates;

    @Autowired
    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    @Autowired
    public void setUpdates(Updates updates) {
        this.updates = updates;
    }

    @Autowired
    public void setWeather(Weather weather) {
        this.weather = weather;
    }

    @Override
    public void run() {

        while (true) {
            try {
                Thread.sleep(1000*3);

                preparedStatement = connection.prepareStatement("SELECT city FROM cities");

                resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    String city = resultSet.getString("city");
                    setCity(city);
                    updates.addInformationAboutCities(city, getWeatherToday(), getWeatherTomorrow(), getWeatherFor10Days(), getTempToday(), getTempTomorrow(), getTempFor10Days(), getTempForHours(), getWeatherNow(), getSunsetAndSunrise());
                }

                Thread.sleep(1000*3);

            }catch (Exception e){
                e.printStackTrace();
            }


        }
    }

    public void setCity(String city){
        weather.start(city);
    }

    public String getTempToday(){
        return convertArrayToString(weather.tempToday());
    }

    public String getTempTomorrow(){
        return convertArrayToString(weather.tempTomorrow());
    }

    public String getWeatherToday(){
        return convertArrayToString(weather.weatherToday());

    }

    public String getWeatherTomorrow(){
        return convertArrayToString(weather.weatherTomorrow());
    }

    public String getWeatherFor10Days(){
        return convertArrayToString(weather.weatherFor10Days());
    }

    public String getTempFor10Days(){
        return convertArrayToString(weather.tempFor10Days());
    }

    public String getTempForHours(){
        return convertArrayToString(weather.tempForHours());
    }

    public String getWeatherNow(){
        return convertArrayToString(weather.tempAndWeatherNow());
    }

    public String getSunsetAndSunrise(){
        return convertArrayToString(weather.sunsetAndSunrise());
    }

    private String convertArrayToString(String[] array){
        StringBuilder answer = new StringBuilder();

        for(int i = 0; i<array.length; i++){
            if(i+1 == array.length){
                answer.append(array[i]);
                continue;
            }
            answer.append(array[i]).append(":");
        }

        return answer.toString();
    }
}
