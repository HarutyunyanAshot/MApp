package com.mapps.mapp.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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

    public static void copyDirectoryOneLocationToAnotherLocation(File sourceLocation, File targetLocation)
            throws IOException {

        if (sourceLocation.isDirectory()) {
            if (!targetLocation.exists()) {
                targetLocation.mkdir();
            }

            String[] children = sourceLocation.list();
            for (int i = 0; i < sourceLocation.listFiles().length; i++) {

                copyDirectoryOneLocationToAnotherLocation(new File(sourceLocation, children[i]),
                        new File(targetLocation, children[i]));
            }
        } else {

            InputStream in = new FileInputStream(sourceLocation);

            OutputStream out = new FileOutputStream(targetLocation);

            File path = Environment.getDataDirectory();
            android.os.StatFs stat = new android.os.StatFs(path.getPath());
            long blockSize;
            if (Build.VERSION.SDK_INT >= 18) {
                blockSize = stat.getBlockSizeLong();
            } else {
                blockSize = stat.getBlockSize();
            }

            // Copy the bits from instream to outstream
            byte[] buf = new byte[(int)(2*blockSize)];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        }

    }
}
