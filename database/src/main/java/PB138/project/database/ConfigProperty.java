package PB138.project.database;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Michal on 15.5.2015.
 */
public class ConfigProperty extends Properties {

    public ConfigProperty(){
        String propFileName = "config.properties";

        InputStream inputStream = DBUtils.class.getClassLoader().getResourceAsStream(propFileName);

        if (inputStream != null) {
            try {
                load(inputStream);
            } catch (IOException ex) {
                throw new PropertyException("Cannot load property from input stream.");
            }
        } else {
            throw new PropertyException("property file '" + propFileName + "' not found in the classpath");
        }
    }
}
