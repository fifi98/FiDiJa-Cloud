package com.github.fifi98;

import com.github.fifi98.misc.Properties;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

public class Sync {

    public static boolean running=true;

    public Sync(String folder_path){
        new Thread(new Runnable() {
            public void run() {
                running=true;
                while(running){
                    try {
                        getFiles(folder_path);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                    //Do the check every 2 seconds
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("Sync stop");
            }
        }).start();
    }

    //Nabavi sve fileove u odabranom folderu za syncanje
    void getFiles(String folder_path) throws IOException, NoSuchAlgorithmException {

        File directory = new File(folder_path);

        //Get all files from a directory.
        File[] fList = directory.listFiles();
        if(fList != null)
            for (File file : fList) {
                if (file.isFile()) {

                    String file_path=file.getAbsolutePath();
                    String file_hash=getHash(file.getAbsolutePath());

                    //Ako hashevi nisu isti, ili file ne postoji na serveru, uploadaj ga
                    if(!Main.conn.file_uptodate(file_path,file_hash)){

                        Main.status_label.setText("Syncing . . .");

                        FTPClient client = new FTPClient();
                        FileInputStream fis = null;

                        try {
                            //Connect to FTP
                            client.connect(Properties.get("ftp.host"));
                            client.login(Properties.get("ftp.user"), Properties.get("ftp.password"));
                            client.setFileType(FTP.BINARY_FILE_TYPE, FTP.BINARY_FILE_TYPE);
                            client.enterLocalPassiveMode();

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

                            //Get into user's directory
                            client.changeWorkingDirectory(String.valueOf(Main.logged_as));
                            //Upload the file
                            client.storeFile(file_name, fis);
                            client.logout();

                            Main.status_label.setText("Waiting for changes in sync directory . . .");
                            System.out.println("Uploaded: "+ file.getAbsolutePath());

                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            if (fis != null) fis.close();
                            client.disconnect();
                        }
                    }
                } else if (file.isDirectory()){
                    //If it is a directory, look for files inside
                    getFiles(file.getAbsolutePath());
                }
            }
    }

    //Get file SHA-256 hash
    public static String getHash(String file_path) throws IOException, NoSuchAlgorithmException {

        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
        FileInputStream fis = new FileInputStream(file_path);

        byte[] data = new byte[1024];
        int read = 0;
        while ((read = fis.read(data)) != -1) sha256.update(data, 0, read);

        byte[] hashBytes = sha256.digest();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < hashBytes.length; i++) sb.append(Integer.toString((hashBytes[i] & 0xff) + 0x100, 16).substring(1));

        return sb.toString();
    }
}
