package com.github.fifi98.misc;

import com.github.fifi98.Main;
import com.github.fifi98.Sync;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQLConnection {

    static Connection connection = null;

    public MySQLConnection(){
        getConnection();
    }

    private void getConnection() {

        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://"+ Properties.get("mysql.host")+":"+Properties.get("mysql.port")+"/"+Properties.get("mysql.database"), Properties.get("mysql.user"), Properties.get("mysql.password"));
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public boolean file_uptodate(String file_path, String hash, String file_name) throws IOException, NoSuchAlgorithmException {
        PreparedStatement statement = null;

        try {
            String sql = "SELECT * FROM files WHERE user_id = ? AND file_name = ?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, Main.logged_as);
            statement.setString(2, file_path);

            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                if(hash.equals(rs.getString("file_hash"))) return true;

                //This file exists, but we have to upload the new version
                PreparedStatement statement1 = null;
                try {
                    statement1 = connection.prepareStatement("UPDATE files SET file_hash=?, file_location=? WHERE file_name=?");
                    statement1.setString(1, Sync.getHash(file_path));
                    statement1.setString(2, file_name);
                    statement1.setString(3, file_path);
                    statement1.execute();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                } finally {
                    if (statement1 != null) statement1.close();
                }
            }else{
                //This file does not exist on the server, we have to upload it
                PreparedStatement statement1 = null;
                try {
                    statement1 = connection.prepareStatement("INSERT INTO files(file_name, file_hash, user_id, file_location) VALUES(?,?,?,?)");
                    statement1.setString(1, file_path);
                    statement1.setString(2, Sync.getHash(file_path));
                    statement1.setInt(3, Main.logged_as);
                    statement1.setString(4, file_name);
                    statement1.execute();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                } finally {
                    if (statement != null) statement.close();
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (statement != null) statement.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        return false;
    }

    public boolean compare_password(String user, String password){

        PreparedStatement statement = null;
        try {
            String sql = "SELECT * FROM users WHERE user_address = ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, user);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                if(password.equals(rs.getString("user_password"))) {
                    Main.logged_as=rs.getInt("user_id");
                    return true;
                }
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (statement != null) statement.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        return false;
    }
}
