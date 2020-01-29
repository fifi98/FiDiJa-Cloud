package com.github.fifi98.misc;

import com.github.fifi98.Main;

import java.io.*;

public class Properties {

    public static String get(String key){

        java.util.Properties prop = null;
        try (InputStream input = Main.class.getResourceAsStream("/resources/config.properties")) {
            prop = new java.util.Properties();
            prop.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return prop.getProperty(key);

    }

    public static void set(String key, String value) throws IOException {

        File configFile = new File("config.properties");

        try {
            java.util.Properties props = new java.util.Properties();
            props.setProperty(key, value);
            FileWriter writer = new FileWriter(configFile);
            props.store(writer, "host settings");
            writer.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }

    }

}
