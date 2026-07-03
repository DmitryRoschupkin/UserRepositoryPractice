package contexts;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import annotations.Component;
import annotations.ComponentScan;
import annotations.Configuration;
import annotations.Inject;
import annotations.PostConstruct;
import annotations.Bean;

public class ApplicationContext {
    private final Map<Class<?>, Object> instances = new HashMap<>();

    public ApplicationContext(Class<?> mainClass) throws Exception{
        List<Class<?>> classesToProcess = new ArrayList<>();

        classesToProcess.addAll(PackageScanner.scan("repository"));
        classesToProcess.addAll(PackageScanner.scan("services"));
        classesToProcess.addAll(PackageScanner.scan("menu"));
        classesToProcess.addAll(PackageScanner.scan("validators"));
        classesToProcess.addAll(PackageScanner.scan("models"));

        if(mainClass.isAnnotationPresent(ComponentScan.class)){
            ComponentScan componentScan = mainClass.getAnnotation(ComponentScan.class);
            String packageToScan = componentScan.value();

            classesToProcess.addAll(PackageScanner.scan(packageToScan));
        }

        for(Class<?> clazz:classesToProcess){
            if(clazz.isAnnotationPresent(Component.class)){
                Constructor<?> constructor = findInjectConstructor(clazz);
                Class<?>[] parameterTypes = constructor.getParameterTypes();
                Object[] dependencies = new Object[parameterTypes.length];

                for(int i = 0; i<parameterTypes.length; i++){
                    dependencies[i] = findDependency(parameterTypes[i]);
                }

                Object instance = constructor.newInstance(dependencies);
                instances.put(clazz, instance);

                invokePostConstruct(instance);
            }
            
            if(clazz.isAnnotationPresent(Configuration.class)){
                Object configInstance = clazz.getDeclaredConstructor().newInstance();

                for(Method method : clazz.getDeclaredMethods()){
                    if(method.isAnnotationPresent(Bean.class)){
                        Object beanInstance = method.invoke(configInstance);
                        instances.put(method.getReturnType(), beanInstance);
                    }
                }

                invokePostConstruct(configInstance);
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

    private static void invokePostConstruct(Object instance) throws Exception{
        for(Method method:instance.getClass().getDeclaredMethods()){
            if(method.isAnnotationPresent(PostConstruct.class)){
                method.setAccessible(true);
                System.out.println("Log of DI container: calling @PostConstruct for "+method.getName()+
                        " method in "+instance.getClass().getSimpleName());
                method.invoke(instance);
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
