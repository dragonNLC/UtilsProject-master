package com.dragondevl.utils;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * @ClassName IOUtils
 * @Description TODO
 * @SysUser Administrator
 * @Author dragon
 * @Date 2020-05-21 14:35
 * @Version 1.0
 */
public class IOUtils {

    public static void closeIO(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException ignore) {}
        }
    }

    public static void closeIO(Socket socket) {
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException ignore) {
            }
        }
    }

}
