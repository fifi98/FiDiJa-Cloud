package com.github.fifi98.forms;

import com.github.fifi98.Main;
import com.github.fifi98.Sync;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class Setup extends JFrame {
    JPanel panel;
    JLabel label;

    // constructor
    public Setup()
    {
        setTitle("Setup");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400,300);
        setResizable(false);
        setLocationRelativeTo(null);

        JLabel instructions = new JLabel("Please select a directory you w");

        label = new JLabel("");  // construct a JLabel
        label.setForeground(Color.white);
        //add( label );                        // add the label to the JFrame

        JPanel header = new JPanel();
        header.setBackground(Color.darkGray);
        header.add(label);

        JPanel main = new JPanel();
        main.setBackground(Color.darkGray);

        JButton login_b = new JButton("Browse");
        login_b.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                JFileChooser chooser = new JFileChooser(new File(System.getProperty("user.home") + "\\Downloads")); //Downloads Directory as default
                chooser.setDialogTitle("Select Location");
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                chooser.setAcceptAllFileFilterUsed(false);

                if (chooser.showSaveDialog(panel) == JFileChooser.APPROVE_OPTION)
                {
                    Main.folder_to_sync=chooser.getSelectedFile().getAbsolutePath();
                    label.setText(chooser.getSelectedFile().getAbsolutePath());

                    Sync a=new Sync(chooser.getSelectedFile().getAbsolutePath());

                }

            }
        });
        main.add(login_b);

        JPanel footer = new JPanel();
        footer.setBackground(Color.darkGray);

        footer.setLayout(new FlowLayout(FlowLayout.CENTER));
        footer.add(new JButton("Finish"));

        add(header, BorderLayout.NORTH);
        add(main, BorderLayout.CENTER);
        add(footer, BorderLayout.SOUTH);

        JButton finish = new JButton("Finish setup");
        //add(finish);

        getContentPane().setBackground(Color.darkGray);
    }

}
