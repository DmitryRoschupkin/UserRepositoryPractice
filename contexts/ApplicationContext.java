package contexts;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import annotations.Component;
import annotations.Configuration;
import annotations.Inject;
import annotations.Bean;

public class ApplicationContext {
    private final Map<Class<?>, Object> instances = new HashMap<>();

    public ApplicationContext(Class<?> ... classes) throws Exception{
        for(Class<?> clazz:classes){
            if(clazz.isAnnotationPresent(Component.class)){
                Constructor<?> constructor = findInjectConstructor(clazz);
                Class<?>[] parameterTypes = constructor.getParameterTypes();
                Object[] dependencies = new Object[parameterTypes.length];

                for(int i = 0; i<parameterTypes.length; i++){
                    dependencies[i] = findDependency(parameterTypes[i]);
                }

                Object instance = constructor.newInstance(dependencies);
                instances.put(clazz, instance);
            }
            
            if(clazz.isAnnotationPresent(Configuration.class)){
                Object configInstance = clazz.getDeclaredConstructor().newInstance();

                for(Method method : clazz.getDeclaredMethods()){
                    if(method.isAnnotationPresent(Bean.class)){
                        Object beanInstance = method.invoke(configInstance);
                        instances.put(method.getReturnType(), beanInstance);
                    }
                }
            }

        }

        for(Object instance:instances.values()){
            Field[] fields = instance.getClass().getDeclaredFields();
            for(Field field:fields){
                if(field.isAnnotationPresent(Inject.class)){
                    Object dependency = findDependency(field.getType());

                    if(dependency != null){
                        field.setAccessible(true);
                        field.set(instance, dependency);
                    }else{
                        throw new RuntimeException("Dependency is not found");
                    }
                }
            }
        }
    }

    private static Constructor<?> findInjectConstructor(Class<?> clazz){
        return Arrays.stream(clazz.getDeclaredConstructors())
                                .filter(c -> c.isAnnotationPresent(Inject.class))
                                .findFirst()
                                .orElseGet(() -> clazz.getDeclaredConstructors()[0]);
    }

    @SuppressWarnings("unchecked")
    public <T> T getComponent(Class<T> clazz){
        return (T) findDependency(clazz);
    }

    private Object findDependency(Class<?> type){
        if(instances.containsKey(type)){
            return instances.get(type);
        }
        for(Object instance:instances.values()){
            if(type.isAssignableFrom(instance.getClass())){
                return instance;
            }
        }
        return null;
    }
}
