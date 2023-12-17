package spring.app.weather.mainWeather;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.HttpEntityMethodProcessor;
import spring.app.DATABASE.GetInfo;
import spring.app.DATABASE.Updates;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class Weather {
    private JSONObject object;
    private Updates updates;

    private final String KEY = "6abb9c55bb754dc6846182628232311";
    private final Map<String, String> months = new HashMap<>();

    private JSONArray arr;

    private boolean responseState;

    private int arrLen;

    @Autowired
    public void setUpdates(Updates updates){
        this.updates = updates;
    }



    {
        months.put("1", "January");
        months.put("2", "February");
        months.put("3", "March");
        months.put("4", "April");
        months.put("5", "May");
        months.put("6", "June");
        months.put("7", "July");
        months.put("8", "August");
        months.put("9", "September");
        months.put("10", "October");
        months.put("11", "November");
        months.put("12", "December");
    }

    public synchronized void start(String city){
        if(toRequest(city) == 200){
            updates.addCity(city);
            this.responseState = true;
        }else{
            this.responseState = false;
        }

        this.arr = this.object.getJSONObject("forecast").getJSONArray("forecastday");
        this.arrLen = this.arr.length();
    }

    public synchronized String[] tempFor10Days() {
        String[] temp = new String[9];

        for (int i = 0; i < 9; i++) {
            if(i<arrLen) {
                temp[i] = arr.getJSONObject(i).getJSONObject("day").getDouble("maxtemp_c") + "";
                continue;
            }
            temp[i] = "info";
        }

        return temp;
    }

    public synchronized String[] weatherFor10Days(){
        String[] weather = new String[9];
        for(int i = 0; i<9; i++){
            if(i<arrLen) {
                weather[i] = arr.getJSONObject(i).getJSONObject("day").getJSONObject("condition").getString("text");
                continue;
            }

            weather[i] = ":<";

        }

        return weather;
    }

    private int toRequest(String city) {
            StringBuilder builder = new StringBuilder();

            try {
                URL url = new URL("http://api.weatherapi.com/v1/forecast.json?key=" + KEY + "&q=" + city + "&days=10&aqi=no&alerts=no");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String line;

                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }

                reader.close();

                this.object = new JSONObject(builder.toString());

                return 200;
            }catch (IOException e){

                return 400;
            }
        }




    public synchronized String[] weatherToday(){
        JSONArray array = arr.getJSONObject(0).getJSONArray("hour");

        String[] weatherFor4Time = new String[4];

        int index = 0;
        for(int i = 0; i<array.length(); i++){
            String time = array.getJSONObject(i).getString("time").split(" ")[1];
            if(!(index < 4)){
                break;
            }
            if(time.equals("02:00") || time.equals("06:00") || time.equals("12:00") || time.equals("18:00")){
                weatherFor4Time[index++] = array.getJSONObject(i).getJSONObject("condition").getString("text");
            }

        }
        return weatherFor4Time;
    }

    public synchronized String[] weatherTomorrow(){
        JSONArray array = arr.getJSONObject(1).getJSONArray("hour");

        String[] weatherFor4Time = new String[4];

        int index = 0;
        for(int i = 0; i<array.length(); i++){
            String time = array.getJSONObject(i).getString("time").split(" ")[1];
            if(!(index < 4)){
                break;
            }
            if(time.equals("02:00") || time.equals("06:00") || time.equals("12:00") || time.equals("18:00")){
                weatherFor4Time[index++] = array.getJSONObject(i).getJSONObject("condition").getString("text");
            }

        }
        return weatherFor4Time;
    }

    public synchronized String[] tempToday(){
        JSONArray array = arr.getJSONObject(0).getJSONArray("hour");

        String[] tempFor4Time = new String[4];

        int index = 0;
        for(int i = 0; i<array.length(); i++){
            String time = array.getJSONObject(i).getString("time").split(" ")[1];
            if(!(index < 4)){
                break;
            }
            if(time.equals("02:00") || time.equals("06:00") || time.equals("12:00") || time.equals("18:00")){
                tempFor4Time[index++] = array.getJSONObject(i).getDouble("temp_c") + "";
            }

        }
        return tempFor4Time;
    }
    public synchronized String[] tempTomorrow(){
        JSONArray array = arr.getJSONObject(1).getJSONArray("hour");

        String[] tempFor4Time = new String[4];

        int index = 0;
        for(int i = 0; i<array.length(); i++){
            String time = array.getJSONObject(i).getString("time").split(" ")[1];
            if(!(index < 4)){
                break;
            }
            if(time.equals("02:00") || time.equals("06:00") || time.equals("12:00") || time.equals("18:00")){
                tempFor4Time[index++] = array.getJSONObject(i).getDouble("temp_c") + "";
            }

        }
        return tempFor4Time;
    }

    public synchronized String[] tempForHours(){
        String[] hoursTemp = new String[24];

        for (int i = 0; i < hoursTemp.length; i++) {
            hoursTemp[i] = arr.getJSONObject(0).getJSONArray("hour").getJSONObject(i).getDouble("temp_c")+"";
        }

        return hoursTemp;
    }

    public synchronized String[] getDays(){
        String[] days = new String[10];

        for(int i = 0; i<10; i++){
            if(i<arrLen) {
                days[i] = arr.getJSONObject(i).getString("date").split("-")[2];
                continue;
            }
            days[i] = "";
        }

        return days;
    }

    public synchronized String[] getMonths(){
        String[] months = new String[10];

        for(int i = 0; i<10; i++){
            if(i<arrLen) {
                months[i] = this.months.get(arr.getJSONObject(i).getString("date").split("-")[1]);
                continue;
            }
            months[i] = "No aviable";
        }



        return months;
    }

    public synchronized String[] getWeekDays(){
        String[] daysOfWeek = new String[9];
        LocalDate date = LocalDate.now();

        for(int i = 0; i<daysOfWeek.length; i++){
            String weekDay = date.getDayOfWeek().toString().toLowerCase();
            daysOfWeek[i] = weekDay.substring(0, 1).toUpperCase() + weekDay.substring(1);
            date = date.plusDays(i+1);
        }

        return daysOfWeek;
    }

    public synchronized String[] tempAndWeatherNow(){
        Date date = new Date();

        String[] tempAndWeatherNow = new String[2];

        for(int i = 0; i < 24; i++){
            if(date.getHours() == i){
                tempAndWeatherNow[0] = arr.getJSONObject(0).getJSONArray("hour").getJSONObject(i).getDouble("temp_c")+"";
                tempAndWeatherNow[1] = arr.getJSONObject(0).getJSONArray("hour").getJSONObject(i).getJSONObject("condition").getString("text");
            }
        }

        return tempAndWeatherNow;
    }

    public synchronized String[] sunsetAndSunrise(){
        String[] sunsetAndSunrise = new String[2];

        sunsetAndSunrise[0] = arr.getJSONObject(0).getJSONObject("astro").getString("sunrise");
        sunsetAndSunrise[1] = arr.getJSONObject(0).getJSONObject("astro").getString("sunset");

        return sunsetAndSunrise;
    }

    public boolean getResponseState(){
        return this.responseState;
    }
}
