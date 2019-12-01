package com.github.fifi98;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class Sync {

    public Sync(String folder_path){


        new Thread(new Runnable() {
            public void run() {

                while(true){
                    try {
                        getFiles(folder_path);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                    //Svakih 5 sec provjeravaj
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();


    }

    //Nabavi sve fileove u odabranom folderu za syncanje
    void getFiles(String folder_path) throws IOException, NoSuchAlgorithmException {

        File directory = new File(folder_path);

        // Get all files from a directory.
        File[] fList = directory.listFiles();
        if(fList != null)
            for (File file : fList) {
                if (file.isFile()) {

                    String file_path=file.getAbsolutePath();
                    String file_hash=getHash(file.getAbsolutePath());

                    //Ako hashevi nisu isti, ili file ne postoji na serveru, uploadaj ga
                    if(!Main.conn.file_uptodate(file_path,file_hash)){

                        FTPClient client = new FTPClient();

                        FileInputStream fis = null;

                        try {

                            InputStream input = Main.class.getResourceAsStream("/resources/config.properties");
                            Properties prop = new Properties();
                            prop.load(input);

                            client.connect(prop.getProperty("ftp.host"));
                            client.login(prop.getProperty("ftp.user"), prop.getProperty("ftp.password"));
                            client.setFileType(FTP.BINARY_FILE_TYPE, FTP.BINARY_FILE_TYPE);
                            client.enterLocalPassiveMode();

                            //
                            fis = new FileInputStream(file_path);

                            //Get filename
                            String file_name=file_path.replace(Main.folder_to_sync + "/", "");
                            //Check if the file is in a subdirectory
                            List<String> subfolders = Arrays.asList(file_name.split("/"));
                            if(subfolders.size()>0){
                                //If yes, create those directories
                                for(int i=0; i<subfolders.size()-1; i++){
                                    //Create directory
                                    client.makeDirectory(subfolders.get(i));
                                    //Enter the directory
                                    client.changeWorkingDirectory(subfolders.get(i));
                                }
                                //Set filename to the last substring after /
                                file_name=subfolders.get(subfolders.size()-1);
                            }


                            client.storeFile(file_name, fis);
                            client.logout();
                            //Store file hash in db


                            System.out.println("Uploaded: "+ file.getAbsolutePath());

                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                if (fis != null) {
                                    fis.close();
                                }
                                client.disconnect();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                    }



                } else if (file.isDirectory()) {
                    getFiles(file.getAbsolutePath());
                }
            }

    }

    //Nabavi SHA-256 hash filea
    public static String getHash(String file_path) throws IOException, NoSuchAlgorithmException {
        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
        FileInputStream fis = new FileInputStream(file_path);

        byte[] data = new byte[1024];
        int read = 0;
        while ((read = fis.read(data)) != -1) {
            sha256.update(data, 0, read);
        };
        byte[] hashBytes = sha256.digest();

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < hashBytes.length; i++) {
            sb.append(Integer.toString((hashBytes[i] & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
    }
}
