package com.dragondevl.utils;

import android.Manifest;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.RequiresPermission;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/11/27.
 */

public class NetUtil {

    public static boolean isIP(String address) {
        if (address.length() < 7 || address.length() > 15 || "".equals(address)) {
            return false;
        }
        /**
         * 判断IP格式和范围
         */
        String rexp = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
        Pattern pat = Pattern.compile(rexp);
        Matcher mat = pat.matcher(address);
        return mat.find();
    }

    public static boolean isPort(String port) {
        if (!NumberUtil.isNumeric(port)) {
            return false;
        }
        int intPort = Integer.valueOf(port);
        if (intPort < 0 || intPort > 65536) {
            return false;
        }
        return true;
    }

    public static final int NETWORK_NONE = -1;

    public static final int NETWORK_MOBILE = 0;

    public static final int NETWORK_WIFI = 1;

    public static final int NETWORK_ETHERNET = 2;

    @RequiresPermission(Manifest.permission.CHANGE_NETWORK_STATE)
    public static int getNetworkState(Context mContext) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connectivityManager != null) {
            networkInfo = connectivityManager.getActiveNetworkInfo();
        } else {
            return NETWORK_NONE;
        }

        if (networkInfo != null && networkInfo.isConnected()) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                return NETWORK_WIFI;
            } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                return NETWORK_MOBILE;
            } else if (networkInfo.getType() == ConnectivityManager.TYPE_ETHERNET) {
                return NETWORK_ETHERNET;
            }
        } else {
            return NETWORK_NONE;
        }
        return NETWORK_NONE;
    }

    /**
     * @return
     * @author suncat
     * @category 判断是否有外网连接（普通方法不能判断外网的网络是否连接，比如连接上局域网）
     */
    public static final boolean ping() {

        String result = null;
        try {
            String ip = "www.baidu.com";// ping 的地址，可以换成任何一种可靠的外网
            Process p = Runtime.getRuntime().exec("ping -c 3 -w 20 " + ip);// ping网址3次
            // 读取ping的内容，可以不加
            InputStream input = p.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(input));
            StringBuilder stringBuffer = new StringBuilder();
            String content = "";
            while ((content = in.readLine()) != null) {
                stringBuffer.append(content);
            }
            Log.d("------ping-----", "result content : " + stringBuffer.toString());
            // ping的状态
            int status = p.waitFor();
            if (status == 0) {
                result = "success";
                return true;
            } else {
                result = "failed";
            }
        } catch (IOException e) {
            result = "IOException";
        } catch (InterruptedException e) {
            result = "InterruptedException";
        } finally {
            Log.d("----result---", "result = " + result);
            Log.e("----result---", "result = " + result);
        }
        return false;
    }

    private static String getMobileAddress() {
        try {
            Enumeration<NetworkInterface> infoSet = NetworkInterface.getNetworkInterfaces();
            while (infoSet.hasMoreElements()) {
                NetworkInterface i = infoSet.nextElement();
                Enumeration<InetAddress> enumIPAddress = i.getInetAddresses();
                while (enumIPAddress.hasMoreElements()) {
                    InetAddress ipa = enumIPAddress.nextElement();
                    if (ipa instanceof Inet4Address && ipa.isLoopbackAddress() && !"127.0.0.1".equals(ipa.getHostAddress())) {
                        return ipa.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getInternetIPAddress() {

        return "";
    }

}
