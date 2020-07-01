package com.dragondevl.utils;

import com.dragondevl.clog.CLog;
import com.dragondevl.listener.FileCtlProcessListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;

/**
 * @ClassName FileUtils
 * @Description 文件操作
 * @SysUser Administrator
 * @Author dragon
 * @Date 2020-07-01 10:49
 * @Version 1.0
 */
public class FileUtils {

    //////////////////////////////文件夹创建///////////////////////////////////
    public static boolean createDirectory(String directory) {
        return createDirectory(new File(directory));
    }

    public static boolean createDirectory(File directory) {
        if (!directory.exists()) {
            return directory.mkdirs();
        }
        return true;
    }
    //////////////////////////////文件夹创建///////////////////////////////////

    //////////////////////////////文件创建///////////////////////////////////
    public static boolean createFile(String file) throws IOException {
        return createFile(new File(file));
    }

    public static boolean createFile(File file) throws IOException {
        return file.exists() ? file.delete() && file.createNewFile() : file.createNewFile();
    }
    //////////////////////////////文件创建///////////////////////////////////

    //////////////////////////////空文件夹识别///////////////////////////////////
    public static boolean detectDirectoryWithNotEmptyFile(String path) {
        return detectDirectoryWithNotEmptyFile(new File(path));
    }

    public static boolean detectDirectoryWithNotEmptyFile(File directory) {
        return directory.exists() && directory.isDirectory() && directory.listFiles() != null && directory.listFiles().length > 0;
    }
    //////////////////////////////空文件夹识别///////////////////////////////////

    //////////////////////////////清除所有空文件夹///////////////////////////////////
    public static boolean clearEmptyChildrenDirectory(String path) {
        return clearEmptyChildrenDirectory(new File(path));
    }

    public static boolean clearEmptyChildrenDirectory(File directory) {
        if (directory.exists() && directory.isDirectory()) {
            File[] children = directory.listFiles();
            for (File f :
                    children) {
                if (Thread.currentThread().isInterrupted()) {//中断线程
                    return false;
                }
                if (f.exists() && f.isDirectory()) {
                    clearEmptyChildrenDirectory(f);
                    if (!detectDirectoryWithNotEmptyFile(f)) {//里面没有文件
                        f.delete();
                    }
                }
            }
        }
        return false;
    }
    //////////////////////////////清除所有空文件夹///////////////////////////////////

    //////////////////////////////文件夹删除，递归删除里面的所有文件以及文件夹///////////////////////////////////
    public static boolean deleteDirectory(String path, FileCtlProcessListener fileCtlListener) {
        return deleteDirectory(new File(path), fileCtlListener);
    }

    public static boolean deleteDirectory(File folder, FileCtlProcessListener fileCtlListener) {
        if (folder.exists() && folder.isDirectory()) {
            File[] children = folder.listFiles();
            for (File child :
                    children) {
                if (Thread.currentThread().isInterrupted()) {//中断线程
                    return false;
                }
                if (child.exists()) {
                    long size = 0;
                    if (child.isDirectory()) {
                        deleteDirectory(child, fileCtlListener);
                        child.delete();//最后把文件夹也删掉
                    } else if (child.isFile()) {
                        size = child.length();
                        deleteFile(child);
                    }
                    if (fileCtlListener != null) {
                        fileCtlListener.onDeleteProcess(child.getAbsolutePath(), size, -1);
                    }
                }
            }
        } else if (folder.exists() && folder.isFile()) {
            return deleteFile(folder);
        }
        return true;
    }
    //////////////////////////////文件夹删除，递归删除里面的所有文件以及文件夹///////////////////////////////////

    //////////////////////////////文件删除///////////////////////////////////
    public static boolean deleteFile(String path) {
        return deleteFile(new File(path));
    }

    public static boolean deleteFile(File file) {
        /*return true;*/
        if (file.exists()) {
            return file.delete();
        }
        return false;
    }
    //////////////////////////////文件删除///////////////////////////////////

    public static boolean copyFile(String src, String dest, FileCtlProcessListener fileCtlProcessListener) {
        return copyFile(new File(src), new File(dest), fileCtlProcessListener);
    }

    public static boolean copyFile(File src, File dest, FileCtlProcessListener fileCtlProcessListener) {
        if (!src.exists()) {
            CLog.e("File is not exists, path = " + src.getAbsolutePath());
            return false;
        }
        FileInputStream fis;
        try {
            fis = new FileInputStream(src);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        File parentFile = new File(dest.getParent());
        if (!createDirectory(parentFile)) {
            CLog.e("File create fail = " + dest.getAbsolutePath());
            return false;
        }
        try {
            if (!createFile(dest)) {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(dest);
            byte[] data = new byte[8 * 1024];
            int length;
            while ((length = fis.read(data)) != -1) {
                if (Thread.currentThread().isInterrupted()) {//中断线程
                    return false;
                }
                if (fileCtlProcessListener != null) {
                    fileCtlProcessListener.onCopyProcess(src.getAbsolutePath(), length, -1);
                }
                fos.write(data, 0, length);
            }
            return true;
        } catch (FileNotFoundException foe) {
            foe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            IOUtils.closeIO(fos);
            IOUtils.closeIO(fis);
        }
        return false;
    }
    //////////////////////////////文件复制///////////////////////////////////

    //////////////////////////////文件统计///////////////////////////////////
    public static long fileSize(String src) {
        return fileSize(new File(src));
    }

    public static long fileSize(File src) {
        long result = 0;
        if (src.exists() && src.isDirectory()) {
            File[] children = src.listFiles();
            if (children != null && children.length > 0) {
                for (File f :
                        children) {
                    if (Thread.currentThread().isInterrupted()) {//中断线程
                        return result;
                    }
                    result += f.length();
                    if (f.isDirectory()) {
                        result += fileSize(f);
                    }
                }
            }
        }
        return result;
    }
    //////////////////////////////文件统计///////////////////////////////////

    public static final float UNIT = 1024f;
    public static final String GB = "GB";
    public static final String MB = "MB";
    public static final String KB = "KB";
    public static final String FORMAT_2_POINT = "%.2f";

    //将bytes长度转换为对应的大小
    public static String covertByte2UpUnit(long bytes) {
        StringBuilder result = new StringBuilder();
        double kBit = bytes / UNIT;//拿到byte
        if (kBit >= UNIT) {
            double mBit = kBit / UNIT;
            if (mBit > UNIT) {
                double gBit = mBit / UNIT;//拿到G
                result.append(String.format(Locale.CHINESE, FORMAT_2_POINT, gBit));
                result.append(GB);
            } else {
                result.append(String.format(Locale.CHINESE, FORMAT_2_POINT, mBit));
                result.append(MB);
            }
        } else {
            result.append(String.format(Locale.CHINESE, FORMAT_2_POINT, kBit));
            result.append(KB);
        }
        return result.toString();
    }

    public static int covertProcess(long max, long process) {
        return (int) ((process * 100) / max);
    }

    public static boolean checkDirectoryChildren(String path) {
        File file = new File(path);
        if (file.exists() && file.isDirectory()) {
            File[] children = file.listFiles();
            if (children != null && children.length > 0) {
                return true;
            }
        }
        return false;
    }

}
