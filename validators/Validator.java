package validators;

import java.lang.reflect.Field;
import annotations.*;

public class Validator {
    public static void validate(Object object) throws IllegalAccessException{
        for(Field field:object.getClass().getDeclaredFields()){
            NotEmpty notEmpty = field.getAnnotation(NotEmpty.class);
            if(notEmpty != null){
                field.setAccessible(true);
                Object value = field.get(object);
                if(value == null || value.toString().trim().isEmpty()){
                    throw new RuntimeException(notEmpty.message());
                }
            }
        }
    }
}
