package contexts;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PackageScanner {
    public static List<Class<?>> scan(String packageName) throws Exception{
        List<Class<?>> classes = new ArrayList<>();
        
        String path = packageName.replace('.', '/');
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL resource = classLoader.getResource(path);

        if(resource == null){
            return classes;
        }

        File directory = new File(resource.getFile());
        if(directory.exists()){
            for(File file:directory.listFiles()){
                if(file.getName().endsWith(".class")){
                    String className = packageName + "." + file.getName().replace(".class", "");
                    classes.add(Class.forName(className));
                }
            }
        }
        return classes;
    }
}
