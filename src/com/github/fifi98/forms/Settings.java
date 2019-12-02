package com.github.fifi98.forms;

import com.github.fifi98.Main;
import com.github.fifi98.Sync;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Settings extends JFrame {

    JTextArea chosen_directory;
    String new_directory=null;

    public Settings(){
        setTitle("Settings");
        setSize(400,300);
        setResizable(false);
        setLocationRelativeTo(null);

        //Instructions label
        JLabel instructions = new JLabel("Please select a directory you would like to be synced");
        instructions.setForeground(Color.white);
        instructions.setBorder(new EmptyBorder(20, 0, 0, 0));

        //Header panel
        JPanel header = new JPanel();
        header.setBackground(Color.darkGray);
        header.add(instructions);

        //Main panel
        JPanel main = new JPanel();
        main.setLayout(new GridLayout(0, 1));
        main.setBackground(Color.darkGray);
        main.setBorder(new EmptyBorder(30, 60, 30, 60));

        //Browse button
        JButton browse_button = new JButton("Browse directory");
        browse_button.setPreferredSize(new Dimension(200,40));
        browse_button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                JFileChooser chooser = new JFileChooser(); //Downloads Directory as default
                chooser.setDialogTitle("Select sync folder");
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                chooser.setAcceptAllFileFilterUsed(false);

                if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION)
                {
                    new_directory=chooser.getSelectedFile().getAbsolutePath();
                    chosen_directory.setText("New directory: " + new_directory);
                }

            }
        });
        main.add(browse_button);

        //Label for showing chosen directory
        chosen_directory = new JTextArea("Current directory: " + Main.folder_to_sync);
        chosen_directory.setBorder(new EmptyBorder(20, 0, 0, 0));
        chosen_directory.setLineWrap(true);
        chosen_directory.setWrapStyleWord(true);
        chosen_directory.setOpaque(false);
        chosen_directory.setEditable(false);
        chosen_directory.setForeground(Color.white);

        main.add(chosen_directory);

        //Footer panel
        JPanel footer = new JPanel();
        footer.setBackground(Color.darkGray);
        footer.setLayout(new FlowLayout(FlowLayout.CENTER));

        //Log out button
        JButton logout_button=new JButton("Logout");
        logout_button.setPreferredSize(new Dimension(140,35));
        logout_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                Main.syncing.setVisible(false);
                new Login();
                //Save to config file
                java.util.Properties props = new java.util.Properties();
                FileOutputStream out = null;
                try {
                    out = new FileOutputStream("config.properties");
                    props.setProperty("userID", "0");
                    props.setProperty("installed", "false");
                    props.setProperty("folder_to_sync", "");
                    props.store(out, null);
                    out.close();
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                //Stop syncing
                Sync.running=false;
            }
        });
        footer.add(logout_button);

        //Save button
        JButton save_button=new JButton("Save");
        save_button.setPreferredSize(new Dimension(140,35));
        save_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Stop sync
                Sync.running=false;
                //Save the new sync directory
                Main.folder_to_sync=new_directory;
                java.util.Properties props = new java.util.Properties();
                FileOutputStream out = null;
                try {
                    out = new FileOutputStream("config.properties");
                    props.setProperty("folder_to_sync", Main.folder_to_sync);
                    props.store(out, null);
                    out.close();
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                //Start sync
                new Sync(Main.folder_to_sync);
                //
                setVisible(false);

            }
        });

        footer.add(save_button);

        //Combine header, main and footer
        add(header, BorderLayout.NORTH);
        add(main, BorderLayout.CENTER);
        add(footer, BorderLayout.SOUTH);
        footer.setBorder(new EmptyBorder(0, 0, 20, 0));

        setVisible(true);
    }

}
