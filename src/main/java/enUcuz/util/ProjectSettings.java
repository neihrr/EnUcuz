package enUcuz.util;


import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;

public class ProjectSettings{
    private static Map<String, Object> data;
    private static ProjectSettings instance = new ProjectSettings();
    public static boolean DEBUG = false;

    private ProjectSettings(){
        try {
            InputStream inputStream = new FileInputStream(new File("src/main/resources/application.yml"));

            Yaml yaml = new Yaml();
            this.data = yaml.load(inputStream);
            this.DEBUG = ((String)this.data.get("mode")).equals("debug");
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
        }
    }

    public static ProjectSettings getInstance(){
        if(instance == null)
            instance = new ProjectSettings();
        return instance;
    }

    public Object Get(String field){
        return this.data.get(field);
    }
}

