package com.example.saveapp.util;

import android.os.Environment;
import android.util.Log;

import com.blankj.utilcode.util.PathUtils;

import java.io.File;

public class FileManager {

    public static final String TAG = "FileManager";

    private static final FileManager INSTANCE = new FileManager();

    private String rootApp = File.separator+"promotion2";

    private String rootFile;

    private String rootCache;

    private static String picturePath = File.separator+"img";

    private static String filePath = File.separator+"file";

    private FileManager() {
        rootFile = Environment.getExternalStorageDirectory().getAbsolutePath();
        rootCache = PathUtils.getInternalAppDataPath();
    }

    public static FileManager getInstance() {
        return INSTANCE;
    }

    public String getImageFilePath() {
        createAppRootFile();
        String path = rootFile + rootApp + picturePath;
        File file = new File(path);
        if(!file.exists()) {
            boolean result = file.mkdirs();
            if(!result) {
                Log.i(TAG, "getImageFilePath mkdirs is failed!");
            }
        }
        return path;
    }

    public String getFilePath() {
        createAppRootFile();
        String path = rootFile + rootApp + filePath;
        File file = new File(path);
        if(!file.exists()) {
            boolean result = file.mkdirs();
            if(!result) {
                Log.i(TAG, "getFilePath mkdirs is failed!");
            }
        }
        return path;
    }

    public String getCacheFilePath() {
        createCacheRootFile();
        String path = rootCache + rootApp + filePath;
        File file = new File(path);
        if(!file.exists()) {
            boolean result = file.mkdirs();
            if(!result) {
                Log.i(TAG, "getFilePath mkdirs is failed!");
            }
        }
        return path;
    }

    private void createAppRootFile() {
        File file = new File(rootFile+rootApp);
        if(file.exists()) {
            return;
        } else {
            boolean result = file.mkdirs();
            if(!result) {
                Log.i(TAG, "mkdir dirs failed! result=" + result);
            }
        }
    }

    private void createCacheRootFile() {
        File file = new File(rootCache+rootApp);
        if(file.exists()) {
            return;
        } else {
            boolean result = file.mkdirs();
            if(!result) {
                Log.i(TAG, "mkdir dirs failed! result=" + result);
            }
        }
    }

}
