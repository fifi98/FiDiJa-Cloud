package com.github.fifi98.forms;

import com.github.fifi98.Main;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Settings extends JFrame {

    JTextArea chosen_directory;

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
                    Main.folder_to_sync=chooser.getSelectedFile().getAbsolutePath();
                    chosen_directory.setText("New directory: " + Main.folder_to_sync);
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
        footer.add(logout_button);

        //Finish button
        JButton finish_button=new JButton("Save");
        finish_button.setPreferredSize(new Dimension(140,35));
        footer.add(finish_button);

        //Combine header, main and footer
        add(header, BorderLayout.NORTH);
        add(main, BorderLayout.CENTER);
        add(footer, BorderLayout.SOUTH);
        footer.setBorder(new EmptyBorder(0, 0, 20, 0));

        setVisible(true);
    }

}
