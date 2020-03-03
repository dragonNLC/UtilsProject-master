package com.dragondevl.algorithms;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.dragondevl.utils.StringUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CipherUtils {

    @NonNull
    public static String getMd5(String data) {
        if (TextUtils.isEmpty(data)) {
            return "";
        }
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] buffer = data.getBytes();
            md.update(buffer);
            return StringUtils.bytes2HexStr(md.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

}
