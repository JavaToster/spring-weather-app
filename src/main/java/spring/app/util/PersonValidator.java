package spring.app.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import spring.app.DATABASE.GetInfo;
import spring.app.models.Person;

import java.sql.SQLException;
@Component
public class PersonValidator implements Validator {

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

        try{
            if(getInfo.check(person.getUsername())){
                errors.rejectValue("username", "", "Username is taken");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
