package com.example.joan.wp2;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.text.format.DateFormat;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by joan.sansa.melsion on 18/04/2018.
 */

public class FileUtil {

    private static FileOutputStream stream;
    private static double baroAvgAux;
    private static double fromWindooAux;

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
            String sequence= "date temperature baroAvg fromWindoo \n";
            stream.write(sequence.getBytes());
            //stream.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("FileUtil", "PROBLEMS SAVING TO FILE");
            Log.e("FileUtil", e.getMessage());
        }
    }

    public static void saveToFile(double temperature, double baroAvg, double fromWindoo){
        try {
            if(baroAvg==0){
                fromWindooAux=fromWindoo;
            } else if(fromWindoo==0) {
                baroAvgAux = baroAvg;
            }
            if(fromWindooAux!=0 && baroAvgAux!=0) {
                String date = DateFormat.format("dd/MM/yyyy-HH:mm:ss", new java.util.Date()).toString();

                String sequence = date + " " + temperature + " " + baroAvgAux + " " + fromWindooAux + "\n";
                stream.write(sequence.replace(".", ",").getBytes());
                //stream.close();
                fromWindooAux=0;
                baroAvgAux=0;
            }
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
