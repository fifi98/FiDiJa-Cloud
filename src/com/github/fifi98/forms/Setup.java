package com.github.fifi98.forms;

import com.github.fifi98.Main;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Setup extends JFrame {

    JLabel chosen_directory;

    public Setup()
    {
        setTitle("Setup");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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

                JFileChooser chooser = new JFileChooser();
                chooser.setDialogTitle("Select sync folder");
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                chooser.setAcceptAllFileFilterUsed(false);

                if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION)
                {
                    Main.folder_to_sync=chooser.getSelectedFile().getAbsolutePath();
                    chosen_directory.setText(Main.folder_to_sync);
                }

            }
        });
        main.add(browse_button);

        //Label for showing chosen directory
        chosen_directory = new JLabel("", SwingConstants.CENTER);
        chosen_directory.setForeground(Color.white);
        main.add(chosen_directory);

        //Footer panel
        JPanel footer = new JPanel();
        footer.setBackground(Color.darkGray);
        footer.setLayout(new FlowLayout(FlowLayout.CENTER));

        //Finish button
        JButton finish_button=new JButton("Finish setup");
        finish_button.setPreferredSize(new Dimension(340,35));
        finish_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(chosen_directory.getText().equals("")){
                    JOptionPane.showMessageDialog(null, "Please choose a directory to sync", "Error", JOptionPane.PLAIN_MESSAGE, null);
                    return;
                }

                //Save to config file
                java.util.Properties props = new java.util.Properties();
                FileOutputStream out = null;
                try {
                    out = new FileOutputStream("config.properties");
                    props.setProperty("userID", String.valueOf(Main.logged_as));
                    props.setProperty("installed", "true");
                    props.setProperty("folder_to_sync", Main.folder_to_sync);
                    props.store(out, null);
                    out.close();
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                //Show main syncing form
                Main.syncing=new Syncing();
                setVisible(false);

            }
        });
        footer.add(finish_button);

        //Combine header, main and footer
        add(header, BorderLayout.NORTH);
        add(main, BorderLayout.CENTER);
        add(footer, BorderLayout.SOUTH);
        footer.setBorder(new EmptyBorder(0, 0, 20, 0));
    }
}