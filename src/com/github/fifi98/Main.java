package com.github.fifi98;

import com.github.fifi98.forms.Setup;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {

    public static JFrame frame;
    public static MySQLConnection conn;
    //
    public static int logged_as=0;
    public static String folder_to_sync=null;

    static void Login(String email, String password){
        conn=new MySQLConnection();
        if(conn.compare_password(email, AES.encrypt(password, "olafbossolafboss"))){
            new Setup().setVisible(true);
            frame.setVisible(false);
        } else {
            JOptionPane.showMessageDialog(null, "Wrong email or password!", "Error", JOptionPane.PLAIN_MESSAGE, null);
        }
    }

    public static void main(String[] args) {

        //Window
        frame = new JFrame("Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400,450);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        //Header for the logo
        JPanel header = new JPanel();
        header.setBackground(Color.darkGray);

        //Logo
        JLabel logo = new JLabel();
        ImageIcon imageIcon = new ImageIcon(new ImageIcon("/Users/filip/IdeaProjects/FiDiJa Cloud/icon.png").getImage().getScaledInstance(148, 148, Image.SCALE_SMOOTH));
        logo.setIcon(imageIcon);
        logo.setBorder(new EmptyBorder(0, 0, -0, 0));
        header.add(logo);

        //Create login panel
        JPanel login_form = new JPanel();
        login_form.setBackground(Color.darkGray);
        login_form.setLayout(new GridLayout(0, 1));
        login_form.setBorder(new EmptyBorder(-50, 0, 0, 0));

        //Email label
        JLabel email_label = new JLabel(" Email");
        email_label.setBorder(new EmptyBorder(0, -1, -22, 0));
        email_label.setForeground(Color.white);
        login_form.add(email_label);

        //Email input
        JTextField email_input = new JTextField(8);
        login_form.add(email_input);

        //Password label
        JLabel password_label = new JLabel(" Password");
        password_label.setBorder(new EmptyBorder(0, -1, -22, 0));
        password_label.setForeground(Color.white);
        login_form.add(password_label);

        //Password input
        JPasswordField password_input = new JPasswordField(8);
        login_form.add(password_input);

        //Footer for login button
        JPanel footer = new JPanel();
        footer.setLayout(new GridLayout(0, 1));
        footer.setBackground(Color.darkGray);

        //Login button
        JButton login_button=new JButton("Login");
        login_button.setPreferredSize(new Dimension(200,40));
        login_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Login(email_input.getText(), password_input.getText());
            }
        });
        footer.add(login_button);

        //Combine header, login form and footer
        frame.add(header, BorderLayout.NORTH);
        login_form.setBorder(new EmptyBorder(0, 60, 30, 60));
        frame.add(login_form, BorderLayout.CENTER);
        footer.setBorder(new EmptyBorder(10, 60, 30, 60));
        frame.add(footer, BorderLayout.SOUTH);

        frame.setVisible(true);
    }
}
