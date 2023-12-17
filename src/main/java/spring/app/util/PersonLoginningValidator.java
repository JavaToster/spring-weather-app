package spring.app.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import spring.app.DATABASE.GetInfo;
import spring.app.models.Person;

import java.sql.SQLException;

@Component
public class PersonLoginningValidator implements Validator {
    private GetInfo getInfo;
    @Autowired
    public void setGetInfo(GetInfo getInfo){
        this.getInfo = getInfo;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Person.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Person person = (Person) o;

        try {
            if (!getInfo.checkPassword(person.getUsername(), person.getPassword())) {
                errors.rejectValue("password", "", "username or password not is true");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
