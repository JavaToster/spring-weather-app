package spring.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import spring.app.DATABASE.GetInfo;
import spring.app.DATABASE.Updates;
import spring.app.models.Person;
import spring.app.util.PersonLoginningValidator;
import spring.app.util.PersonValidator;
import spring.app.weather.addWeatherToDatabase.AddInfoToDatabase;
import spring.app.weather.mainWeather.Weather;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;

@Controller
public class Controllers {
    private Weather weather;
    private AddInfoToDatabase addInfoToDatabase;
    private boolean thread = true;
    private Updates updates;
    private GetInfo getInfo;

    private PersonValidator personValidator;

    private PersonLoginningValidator personLoginningValidator;
    @Autowired
    public void setPersonValidator(PersonValidator personValidator) {
        this.personValidator = personValidator;
    }
    @Autowired
    public void setPersonLoginningValidator(PersonLoginningValidator personLoginningValidator){ this.personLoginningValidator = personLoginningValidator; }

    @Autowired
    public void setGetInfo(GetInfo getInfo) {
        this.getInfo = getInfo;
    }

    @Autowired
    public void setUpdates(Updates updates){this.updates = updates;}

    @Autowired
    public void setAddInfoToDatabase(AddInfoToDatabase addInfoToDatabase) {
        this.addInfoToDatabase = addInfoToDatabase;
    }

    @Autowired
    public void setWeatherFor10Days(Weather weatherFor10Days) {
        this.weather = weatherFor10Days;
    }

    @GetMapping("/weather")
    public String mainWindow(@RequestParam(value = "city", required = false) String city, Model model){
        startThread();

        city = city == null ? "Baltasi" : city;

        city = city.toLowerCase();

        weather.start(city);
        if(!(weather.getResponseState())){
            return "/html/main-error";
        }

        String[][] weatherArr = new String[3][4];
        String[][] tempArr = new String[3][4];
        String[] tempHours = new String[24];
        String[][] now = new String[2][2];
        try {
            if (getInfo.checkCity(city)) {
                getInfo.startGetInfoFromDB(city);
                weatherArr = getInfo.getWeather();
                tempArr = getInfo.getTemp();
                tempHours = getInfo.getTempForHours();
                now = getInfo.getWeatherAndAstro();
            } else {
                System.out.println(2);
                weatherArr[0] = weather.weatherToday();
                weatherArr[1] = weather.weatherTomorrow();
                weatherArr[2] = weather.weatherFor10Days();
                tempHours = weather.tempForHours();
                now[0] = weather.sunsetAndSunrise();
                now[1] = weather.tempAndWeatherNow();

                tempArr[0] = weather.tempToday();
                tempArr[1] = weather.tempTomorrow();
                tempArr[2] = weather.tempFor10Days();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

            model.addAttribute("weather", weatherArr[2]);
            model.addAttribute("temp", tempArr[2]);
            model.addAttribute("tempToday", tempArr[0]);
            model.addAttribute("tempTomorrow", tempArr[1]);
            model.addAttribute("tempForHours", tempHours);
            model.addAttribute("weatherToday", weatherArr[0]);
            model.addAttribute("weatherTomorrow", weatherArr[1]);
            model.addAttribute("city", city.substring(0,1).toUpperCase() + city.substring(1));
            model.addAttribute("astro", now[0]);
            model.addAttribute("now", now[1]);
            model.addAttribute("tempForHours", tempHours);

            model.addAttribute("days", weather.getDays());
            model.addAttribute("months", weather.getMonths());
            model.addAttribute("weekDays", weather.getWeekDays());

        return "/html/main";
    }

    @GetMapping("/login")
    public String loginWindow(Model model) {
        model.addAttribute("person", new Person());

        return "/html/login";
    }

    @GetMapping("/register")
    public String registerWindow(Model model){
        model.addAttribute("person", new Person());

        return "/html/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("person") @Valid Person person,
                           BindingResult bindingResult, HttpServletResponse response){

        personValidator.validate(person, bindingResult);

        if(bindingResult.hasErrors()){
            return "/html/register";
        }

        Cookie cookie = new Cookie("username", person.getUsername());

        cookie.setMaxAge(60*60*24);

        response.addCookie(cookie);

        return "redirect:/weather";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("person") @Valid Person person,
                        BindingResult bindingResult, HttpServletResponse response){

        personLoginningValidator.validate(person, bindingResult);

        if(bindingResult.hasErrors()){
            return "/html/login";
        }

        Cookie cookie = new Cookie("username", person.getUsername());

        cookie.setMaxAge(60*60*24);

        response.addCookie(cookie);

        return "redirect:/weather";
    }

    public void startThread(){
        if(thread){
            addInfoToDatabase.start();
            thread = !thread;
        }
    }
}
