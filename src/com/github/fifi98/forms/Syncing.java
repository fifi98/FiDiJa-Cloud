package com.github.fifi98.forms;

import com.github.fifi98.Main;
import com.github.fifi98.Sync;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Syncing extends JFrame {

    public Syncing(){

        setTitle("FiDiJa Cloud");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400,350);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
        getContentPane().setBackground(Color.darkGray);

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

        //Syncing label
        Main.status_label = new JLabel("Waiting for changes in sync directory . . .", SwingConstants.CENTER);
        Main.status_label.setBorder(new EmptyBorder(0, -1, -22, 0));
        Main.status_label.setForeground(Color.white);
        login_form.add(Main.status_label);

        //Footer for login button
        JPanel footer = new JPanel();
        footer.setLayout(new GridLayout(0, 1));
        footer.setBackground(Color.darkGray);

        //Settings button
        JButton settings_button=new JButton("Settings");
        settings_button.setPreferredSize(new Dimension(200,40));
        settings_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Settings();

            }
        });
        footer.add(settings_button);

        //Combine header, login form and footer
        add(header, BorderLayout.NORTH);
        login_form.setBorder(new EmptyBorder(0, 60, 30, 60));
        add(login_form, BorderLayout.CENTER);
        footer.setBorder(new EmptyBorder(10, 60, 30, 60));
        add(footer, BorderLayout.SOUTH);

        setVisible(true);

        //Start syncing
        new Sync(Main.folder_to_sync);
    }
}
