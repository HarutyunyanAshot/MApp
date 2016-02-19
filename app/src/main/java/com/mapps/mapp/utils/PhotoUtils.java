package com.mapps.mapp.utils;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Mariam on 2/19/16.
 */
public class PhotoUtils {
    public static void saveBitmapToDir(String folderName, String fileName, Bitmap image, Context context, Bitmap.CompressFormat compressFormat) {
        File saveToDir = new File(folderName);
        if(!saveToDir.exists()){
            saveToDir.mkdir();
        }
        File file = null;

        if (saveToDir.exists() && saveToDir.canWrite()) {
            file = new File(saveToDir, fileName);
            try {
                file.createNewFile();
            } catch (IOException e) {
            }
            if (file.exists() && file.canWrite()) {
                compressBitmapDefault(compressFormat, 90, file, image, context);
            }
        }

    }
    private static boolean compressBitmapDefault(Bitmap.CompressFormat compressFormat, int quality, File file, Bitmap bitmap, Context context) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            bitmap.compress(compressFormat, quality, fos);
            return true;
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
            return false;
        } finally {
            if (fos != null) {
                try {
                    fos.flush();
                    fos.close();
                } catch (IOException ex) {
                }
            }
        }
    }
}
