package com.chii.antforest.util;

import android.os.Environment;

import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class FileUtils {
    private static final String TAG = FileUtils.class.getCanonicalName();
    private static File androidDirectory;
    private static File configDirectory;
    private static File configFile;
    private static File friendIdMapFile;
    private static File cooperationIdMapFile;
    private static File statisticsFile;
    private static File exportedStatisticsFile;
    private static File forestLogFile;
    private static File farmLogFile;
    private static File otherLogFile;
    private static File simpleLogFile;
    private static File runtimeLogFile;

    private static File getAndroidDirectoryFile() {
        if (androidDirectory == null) {
            androidDirectory = new File(Environment.getExternalStorageDirectory(), "Android");
        }
        return androidDirectory;
    }

    private static File getConfigDirectoryFile() {
        if (configDirectory == null) {
            configDirectory = new File(getAndroidDirectoryFile(), "data/com.chii.antforest");
        }
        if (!configDirectory.exists()) {
            configDirectory.mkdirs();
        }
        return configDirectory;

    }

    public static File getConfigFile() {
        if (configFile == null) {
            configFile = new File(getConfigDirectoryFile(), "config.json");
            checkFileStatus(configFile);
        }
        return configFile;
    }

    public static File getFriendIdMapFile() {
        if (friendIdMapFile == null) {
            friendIdMapFile = new File(getConfigDirectoryFile(), "friendId.list");
            checkFileStatus(friendIdMapFile);
        }
        return friendIdMapFile;
    }

    public static File getCooperationIdMapFile() {
        if (cooperationIdMapFile == null) {
            cooperationIdMapFile = new File(getConfigDirectoryFile(), "cooperationId.list");
            checkFileStatus(cooperationIdMapFile);
        }
        return cooperationIdMapFile;
    }

    public static File getStatisticsFile() {
        if (statisticsFile == null) {
            statisticsFile = new File(getConfigDirectoryFile(), "statistics.json");
            checkFileStatus(statisticsFile);
        }
        return statisticsFile;
    }

    public static File getExportedStatisticsFile() {
        if (exportedStatisticsFile == null) {
            exportedStatisticsFile = new File(getAndroidDirectoryFile(), "statistics.json");
            checkFileStatus(exportedStatisticsFile);

        }
        return exportedStatisticsFile;
    }

    public static File getForestLogFile() {
        if (forestLogFile == null) {
            forestLogFile = new File(getConfigDirectoryFile(), "forest.log");
            checkFileStatus(forestLogFile);
            if (!forestLogFile.exists()) {
                try {
                    forestLogFile.createNewFile();
                } catch (Throwable t) {
                }
            }
        }
        return forestLogFile;
    }

    public static File getFarmLogFile() {
        if (farmLogFile == null) {
            farmLogFile = new File(getConfigDirectoryFile(), "farm.log");
            checkFileStatus(farmLogFile);
            if (!farmLogFile.exists()) {
                try {
                    farmLogFile.createNewFile();
                } catch (Throwable t) {
                }
            }
        }
        return farmLogFile;
    }

    public static File getOtherLogFile() {
        if (otherLogFile == null) {
            otherLogFile = new File(getConfigDirectoryFile(), "other.log");
            checkFileStatus(otherLogFile);
            if (!otherLogFile.exists()) {
                try {
                    otherLogFile.createNewFile();
                } catch (Throwable t) {
                }
            }
        }
        return otherLogFile;
    }

    public static File getSimpleLogFile() {
        if (simpleLogFile == null) {
            simpleLogFile = new File(getConfigDirectoryFile(), "simple.log");
            checkFileStatus(simpleLogFile);
        }
        return simpleLogFile;
    }

    public static File getRuntimeLogFile() {
        if (runtimeLogFile == null) {
            runtimeLogFile = new File(getConfigDirectoryFile(), "runtime.log");
            checkFileStatus(runtimeLogFile);
        }
        return runtimeLogFile;
    }

    public static File getBackupFile(File f) {
        return new File(f.getAbsolutePath() + ".bak");
    }

    public static String readFromFile(File f) {
        StringBuilder result = new StringBuilder();
        FileReader fr = null;
        try {
            fr = new FileReader(f);
            char[] chs = new char[1024];
            int len = 0;
            while ((len = fr.read(chs)) >= 0) {
                result.append(chs, 0, len);
            }
        } catch (Throwable t) {
            Log.printStackTrace(TAG, t);
        }
        close(fr, f);
        return result.toString();
    }

    public static boolean append2SimpleLogFile(String s) {
        if (getSimpleLogFile().length() > 31_457_280) // 30MB
        {
            getSimpleLogFile().delete();
        }
        return append2File(Log.getFormatDateTime() + "  " + s + "\n", getSimpleLogFile());
    }

    public static boolean append2RuntimeLogFile(String s) {
        if (getRuntimeLogFile().length() > 31_457_280) // 30MB
        {
            getRuntimeLogFile().delete();
        }
        return append2File(Log.getFormatDateTime() + "  " + s + "\n", getRuntimeLogFile());
    }

    public static boolean write2File(String s, File f) {
        boolean success = false;
        FileWriter fw = null;
        try {
            fw = new FileWriter(f);
            fw.write(s);
            fw.flush();
            success = true;
        } catch (Throwable t) {
            if (!f.equals(getRuntimeLogFile())) {
                Log.printStackTrace(TAG, t);
            }
        }
        close(fw, f);
        return success;
    }

    public static boolean append2File(String s, File f) {
        boolean success = false;
        FileWriter fw = null;
        try {
            fw = new FileWriter(f, true);
            fw.append(s);
            fw.flush();
            success = true;
        } catch (Throwable t) {
            if (!f.equals(getRuntimeLogFile())) {
                Log.printStackTrace(TAG, t);
            }
        }
        close(fw, f);
        return success;
    }

    public static boolean copyTo(File f1, File f2) {
        return write2File(readFromFile(f1), f2);
    }

    public static void close(Closeable c, File f) {
        try {
            if (c != null) {
                c.close();
            }
        } catch (Throwable t) {
            if (!f.equals(getRuntimeLogFile())) {
                Log.printStackTrace(TAG, t);
            }
        }
    }

    //判断文件不能为文件夹
    private static void checkFileStatus(File file) {
        if (file.exists() && file.isDirectory()) {
            file.delete();
        }
    }
}
