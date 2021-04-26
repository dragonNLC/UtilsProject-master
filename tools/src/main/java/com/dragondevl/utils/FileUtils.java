package com.dragondevl.utils;

import com.dragondevl.clog.CLog;
import com.dragondevl.listener.FileCtlProcessListener;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

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

    //////////////////////////////文件复制///////////////////////////////////
    public static boolean copyDirectory(String src, String dest, FileCtlProcessListener fileCtlProcessListener) {
        return copyDirectory(new File(src), new File(dest), fileCtlProcessListener);
    }

    public static boolean copyDirectory(File srcDirectory, File destDirectory, FileCtlProcessListener fileCtlProcessListener) {
        if (srcDirectory.exists() && srcDirectory.isDirectory()) {
            File[] children = srcDirectory.listFiles();
            if (children != null && children.length > 0) {
                for (File f :
                        children) {
                    if (Thread.currentThread().isInterrupted()) {//中断线程
                        return false;
                    }
                    if (f.isDirectory()) {
                        createDirectory(destDirectory + File.separator + f.getName());//需要提前创建文件夹
                        copyDirectory(f.getAbsolutePath(), destDirectory.getAbsolutePath() + File.separator + f.getName(), fileCtlProcessListener);
                    } else {
                        String destPath = destDirectory.getAbsolutePath() + File.separator + f.getName();//复制之前先看看本地有没有
                        File destFile = new File(destPath);
                        int result = -1;
                        if (destFile.exists()) {//本地文件存在的，就不用复制了，之所以不用再判断了，是因为之前删除的时候，就已经去掉所有不相同的文件了。
                            result = 0;
                        } else {
                            if (copyFile(f.getAbsolutePath(), destPath, fileCtlProcessListener)) {
                                result = 1;
                            }
                        }
                        if (result == 1) {//复制完成，需要更新数据

                        }
                    }
                }
            }
        }
        return true;
    }

    public static boolean copyFile(String inputPath, String outputPath) {
        return copyFile(new File(inputPath), new File(outputPath));
    }

    public static boolean copyFile(File inputFile, File outputFile) {
        File tempFile = new File(outputFile.getAbsolutePath() + ".temp");
        if (!inputFile.exists()) {
            return false;
        } else {
            if (tempFile.exists()) {//如果缓存文件存在，则删除
                tempFile.delete();
            }
            if (!tempFile.getParentFile().exists()) {
                tempFile.getParentFile().mkdirs();
            }
            FileInputStream fis = null;
            FileOutputStream fos = null;
            byte[] buffer = new byte[1024];
            int len = 0;
            try {
                fis = new FileInputStream(inputFile);
                fos = new FileOutputStream(tempFile);
                while ((len = fis.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                }
                fos.flush();
                fos.close();
                if (outputFile.exists()) {
                    outputFile.delete();
                }
                return tempFile.renameTo(outputFile);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            } finally {
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

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



    public static boolean doZip(String inputPath, String outputPath) {
        System.out.println("开始压缩");
        ZipOutputStream zos = null;
        try {
            zos = new ZipOutputStream(new FileOutputStream(outputPath));
            File inputFile = new File(inputPath);
            try {
                return compressZip(zos, inputFile, inputFile.getName());
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("压缩完成！");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (zos != null) {
                try {
                    zos.close();
                } catch (IOException ignore) {
                }
            }
        }
        return false;
    }

    public static boolean compressZip(ZipOutputStream zos, File inputFile, String baseFile) throws IOException {
        if (inputFile.isDirectory()) {
            File[] children = inputFile.listFiles();
            if (children != null && children.length > 0) {
                for (File child : children) {
                    if (!compressZip(zos, child, baseFile + File.separator + child.getName())) {
                        return false;
                    }
                }
            } else {
                zos.putNextEntry(new ZipEntry(baseFile));
            }
        } else {
            zos.putNextEntry(new ZipEntry(baseFile));
            FileInputStream fis = new FileInputStream(inputFile);
            BufferedInputStream bis = new BufferedInputStream(fis);
            int len = 0;
            byte[] buffer = new byte[1024];
            while ((len = bis.read(buffer)) != -1) {
                zos.write(buffer, 0, len);
            }
            bis.close();
            fis.close();
        }
        return true;
    }

    public static boolean doUnZip(String inputPath, String outputPath, String outputBaseDirPath) {
        FileUtils.deleteDirectory(outputPath, null);
        CLog.e("开始解压缩");
        ZipFile zipFile = null;
        try {
            File file = new File(inputPath);
            zipFile = new ZipFile(file);
            Enumeration entry = zipFile.entries();
            while (entry.hasMoreElements()) {
                ZipEntry zipEntry = (ZipEntry) entry.nextElement();
                if (zipEntry.isDirectory()) {
                    String path = outputBaseDirPath + File.separator + zipEntry.getName();
//                    CLog.e("dirName = " + path);
                    FileUtils.createDirectory(path);
                } else {
                    File outFile = new File(outputBaseDirPath + File.separator + zipEntry.getName());
//                    CLog.e("fileName = " + outFile.getAbsolutePath());
                    FileUtils.createDirectory(outFile.getParentFile());
                    InputStream is = null;
                    FileOutputStream fos = null;
                    try {
                        is = zipFile.getInputStream(zipEntry);
                        fos = new FileOutputStream(outFile);
                        int len = 0;
                        byte[] buffer = new byte[1024];
                        while ((len = is.read(buffer)) != -1) {
                            fos.write(buffer, 0, len);
                        }
                    } finally {
                        IOUtils.closeIO(is);
                        IOUtils.closeIO(fos);
                    }
                }
            }
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (zipFile != null) {
                try {
                    zipFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * Check file folder is exists or parent folder exists
     *
     * @param filePath check file path
     * @return result
     */
    public static boolean checkFile(String filePath) {
        File file = new File(filePath);
        if (file.isDirectory()) {
            return file.exists() || file.mkdirs();
        } else {
            return file.getParentFile().exists() || file.getParentFile().mkdirs();
        }
    }

    /**
     * Check file folder is exists or parent folder exists
     *
     * @param filePath check file path
     * @return result
     */
    public static boolean checkFolder(String filePath) {
        File file = new File(filePath);
        return file.exists() || file.mkdirs();
    }

    /**
     * Check file folder is exists or parent folder exists
     *
     * @param file check file
     * @return result
     */
    public static boolean checkFile(File file) {
        if (file == null) return false;
        if (file.isDirectory()) {
            if (!file.exists()) {
                return file.mkdirs();
            }
            return true;
        } else {
            if (!file.getParentFile().exists()) {
                return file.getParentFile().mkdirs();
            }
            return true;
        }
    }

}
