package com.github.fifi98.misc;

import com.github.fifi98.Main;

import java.io.*;

public class Properties {

    public static String get(String key){


        java.util.Properties prop = null;
        try (InputStream input = Main.class.getResourceAsStream("/resources/config.properties")) {
            prop = new java.util.Properties();
            prop.load(input);
            input.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return prop.getProperty(key);

    }

    public static void set(String key, String value){

        File configFile = new File("config.properties");

        try {
            java.util.Properties props = new java.util.Properties();
            props.setProperty(key, value);
            FileWriter writer = new FileWriter(configFile);
            props.store(writer, "host settings");
            writer.close();
        } catch (FileNotFoundException ex) {
            // file does not exist
        } catch (IOException ex) {
            // I/O error
        }

        /*
        java.util.Properties prop = null;
        try (InputStream input = Main.class.getResourceAsStream("/resources/config.properties")) {
            prop = new java.util.Properties();
            prop.load(input);
            prop.setProperty(property, value);

            try {
                prop.store(new FileOutputStream("src/resources/config.properties"), null);
            } catch (IOException ex) {
                System.out.println(ex);
            }
            input.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

         */

        /*
        FileOutputStream out = null;
        try {
            java.util.Properties props = new java.util.Properties();
            out = new FileOutputStream("src/resources/config.properties");
            props.setProperty(property, value);
            props.store(out, null);
            out.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
*/

    }

}