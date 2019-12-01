package com.github.fifi98;

import com.github.fifi98.forms.Login;

public class Main {

    public static MySQLConnection conn;
    public static int logged_as=0;
    public static String folder_to_sync=null;

    public static void main(String[] args) {
        new Login();
    }
}
