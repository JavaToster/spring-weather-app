package spring.app.models;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class Person {
    @NotEmpty(message = "username should be not empty")
    @Size(min = 3, max = 30, message="username should be between 3 and 30 characters")
    private String username;

    @NotEmpty(message = "username should be not empty")
    @Size(min = 4, max = 16, message = "password should be between 4 and 16 characters")
    private String password;

    public Person(){}

    public Person(String username, String password){
        this.username = username;
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername(){
        return username;
    }

    public String getPassword(){
        return password;
    }
}
