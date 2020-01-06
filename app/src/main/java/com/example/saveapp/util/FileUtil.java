package com.example.saveapp.util;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileUtil {
    public static String generateFileName(String suffix) {
        return FileManager.getInstance().getImageFilePath()+ File.separator+ System.currentTimeMillis()+"."+suffix;
    }



    public static String generateFileName(String rootPath, String suffix) {
        return rootPath+ File.separator+ System.currentTimeMillis()+"."+suffix;
    }

    public static String copyAssets(Context mContext, String assetPath, String targetPath){
        String resultPath = "";
        AssetManager assetManager = mContext.getResources().getAssets();
        final File mWorkingPath = new File(targetPath);
        // 如果文件路径不存在
        if (!mWorkingPath.exists()) {
            mWorkingPath.mkdirs();
        }

        try {
            // 获得每个文件的名字
            String fileName = "app_login.mp4";
            File outFile = new File(mWorkingPath + "/" + fileName);
            resultPath = outFile.getAbsolutePath();
            // 判断文件是否存在
            if (!outFile.exists()) {
                outFile.createNewFile();
                FileOutputStream out = new FileOutputStream(outFile);
                InputStream in = assetManager.open(fileName);
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                out.close();
            } else {
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultPath;
    }
    /**
     * 读取文件内容，作为字符串返回
     */
    public static String readFileAsString(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException(filePath);
        }

        if (file.length() > 1024 * 1024 * 1024) {
            throw new IOException("File is too large");
        }

        StringBuilder sb = new StringBuilder((int) (file.length()));
        // 创建字节输入流
        FileInputStream fis = new FileInputStream(filePath);
        // 创建一个长度为10240的Buffer
        byte[] bbuf = new byte[10240];
        // 用于保存实际读取的字节数
        int hasRead = 0;
        while ( (hasRead = fis.read(bbuf)) > 0 ) {
            sb.append(new String(bbuf, 0, hasRead));
        }
        fis.close();
        return sb.toString();
    }

    /**
     * 根据文件路径读取byte[] 数组
     */
    public static byte[] readFileByBytes(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException(filePath);
        } else {
            ByteArrayOutputStream bos = new ByteArrayOutputStream((int) file.length());
            BufferedInputStream in = null;

            try {
                in = new BufferedInputStream(new FileInputStream(file));
                short bufSize = 1024;
                byte[] buffer = new byte[bufSize];
                int len1;
                while (-1 != (len1 = in.read(buffer, 0, bufSize))) {
                    bos.write(buffer, 0, len1);
                }

                byte[] var7 = bos.toByteArray();
                return var7;
            } finally {
                try {
                    if (in != null) {
                        in.close();
                    }
                } catch (IOException var14) {
                    var14.printStackTrace();
                }

                bos.close();
            }
        }
    }

}
