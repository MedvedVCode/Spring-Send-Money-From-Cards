package ru.netology.sendmoney.logger;

import java.io.*;
import java.util.Date;

public class LoggerTransaction {
    private static final LoggerTransaction logger = new LoggerTransaction();
    private String path = "src" + File.separator + "main" +
            File.separator + "resources" +
            File.separator + "sendmoney.log";

    private LoggerTransaction() {
    }

    public static LoggerTransaction getLogger(){
        return logger;
    }

    public void log(String msg){
        byte[] msgBytes = new StringBuilder()
                .append(new Date())
                .append(" -- ")
                .append(msg)
                .append("\n")
                .toString()
                .getBytes();
        try(FileOutputStream out = new FileOutputStream(path, true)){
            out.write(msgBytes,0, msgBytes.length);

        } catch (IOException e){
            System.out.println(e.getMessage());
        }
    }
}
