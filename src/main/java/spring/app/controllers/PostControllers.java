package spring.app.controllers;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import spring.app.DATABASE.GetInfo;
import spring.app.DATABASE.Updates;

import java.sql.SQLException;

@RestController
public class PostControllers {
    GetInfo getInfo;
    Updates updates;

    @Autowired
    public void setGetInfo(GetInfo getInfo) {
        this.getInfo = getInfo;
    }

    @Autowired
    public void setUpdates(Updates updates) {
        this.updates = updates;
    }

    @PostMapping("/login")
    public String login(@RequestBody String json){
        JSONObject object = new JSONObject(json);
        String username = object.getString("username");
        String password = object.getString("password");
        object.clear();
        object.put("status", 200);
        try {
            object.put("username", getInfo.checkPassword(username, password));
        }catch (SQLException e){
            e.printStackTrace();
        }
        return object.toString();
    }

    @PostMapping("/register")
    public String register(@RequestBody String json){
        JSONObject object = new JSONObject(json);
        String username = object.getString("username");
        String password = object.getString("password");
        object.clear();
        object.put("state", 200);
        try {
            if (!(getInfo.check(username))) {
                object.put("isRegister", true);
                updates.singUp(username, password);
            } else {
                object.put("isRegister", false);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return object.toString();
    }
}
