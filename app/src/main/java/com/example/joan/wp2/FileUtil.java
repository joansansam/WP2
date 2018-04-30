package com.example.joan.wp2;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by joan.sansa.melsion on 18/04/2018.
 */

public class FileUtil {

    private static FileOutputStream stream;

    public static void createFile(Context context){
        if(!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()))
            return;

        //Save file to ...\Phone\Android\data\com.example.joan.wp2\files
        File file = new File(context.getExternalFilesDir(null)
                ,"pressure_values.csv");

        if(file.exists())
            file.delete();

        try {
            stream = new FileOutputStream(file);
            String sequence= "timestamp(sec) fromBarometre baroAvg fromWindoo \n";
            stream.write(sequence.getBytes());
            //stream.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("FileUtil", "PROBLEMS SAVING TO FILE");
            Log.e("FileUtil", e.getMessage());
        }
    }

    public static void saveToFile(double fromBarometer, double baroAvg, double fromWindoo){
        try {
            long timestamp = System.currentTimeMillis()/1000;

            String sequence= timestamp+" "+fromBarometer+" "+baroAvg+" "+ fromWindoo +"\n";
            stream.write(sequence.replace(".",",").getBytes());
            //stream.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("FileUtil", "PROBLEMS SAVING TO FILE");
            Log.e("FileUtil", e.getMessage());
        }
    }

    public static void closeOutputStream(){
        try {
            if(stream != null){
                stream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("FileUtil", "PROBLEMS SAVING TO FILE");
            Log.e("FileUtil", e.getMessage());
        }
    }

}
