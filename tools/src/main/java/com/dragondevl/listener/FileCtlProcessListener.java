package com.dragondevl.listener;

/**
 * @ClassName FileCopyProcessListener
 * @Description 文件操作返回接口
 * @SysUser Administrator
 * @Author dragon
 * @Date 2020-06-15 16:29
 * @Version 1.0
 */
public interface FileCtlProcessListener {

    void onStartDetect();

    void onStartDelete(long max);

    void onDeleteProcess(String path, long process, long max);

    void onDeleteCompleted();

    void onClearEmptyDirectory();

    void onStartDetectCopy();

    void onStartCopy(long max);

    void onCopyProcess(String path, long process, long max);

    void onCopyCompleted(String path, long lastModifier, long externalLastModifier, long size);

    void onCopyCompleted();

    void onFail(String failInfo);
}
