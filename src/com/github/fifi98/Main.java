package com.github.fifi98;

import com.github.fifi98.forms.Login;
import com.github.fifi98.forms.Syncing;
import com.github.fifi98.misc.MySQLConnection;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Main {

    public static MySQLConnection conn;
    public static int logged_as=0;
    public static String folder_to_sync=null;
    public static JLabel status_label;

    public static void main(String[] args) throws IOException {

        //Check if config file exists
        File config = new File("config.properties");
        if(!config.exists()) config.createNewFile();

        //Check if user is logged in
        FileInputStream in = new FileInputStream("config.properties");
        java.util.Properties props = new java.util.Properties();
        props.load(in);

        boolean installed=false;
        if(props.getProperty("installed")!=null)
            if(props.getProperty("installed").equals("true")) installed=true;

        //Show corresponding window
        if(installed){
            logged_as=Integer.parseInt(props.getProperty("userID"));
            folder_to_sync=props.getProperty("folder_to_sync");
            conn=new MySQLConnection();
            new Syncing();
        }else{
            new Login();
        }
        in.close();

    }
}
