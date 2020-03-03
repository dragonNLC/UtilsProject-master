package com.dragondevl.algorithms;

import java.math.BigInteger;

/****
 * @author lb
 * @version 1.0.0
 */
public class HashAlgorithms {

    public static final int MASK = 0x8765fed1;

    /**
     * 加法hash
     *
     * @param key   字符串
     * @param prime 一个质数
     * @return hash结果
     */
    public static int additiveHash(String key, int prime) {
        int hash = key.length();
        int length = key.length();
        for (int i = 0; i < length; i++) {
            hash += key.charAt(i);
        }
        return (hash % prime);
    }

    /**
     * 旋转hash
     *
     * @param key   输入字符串
     * @param prime 质数
     * @return 返回hash结果
     */
    public static int rotatingHash(String key, int prime) {
        int hash = key.length();
        int length = key.length();
        for (int i = 0; i < length; i++) {
            hash = (hash << 4) ^ (hash >> 28) ^ key.charAt(i);
        }
        return (hash % prime);
    }

    public static int oneByOneHash(String key) {
        int hash = key.length();
        int length = key.length();
        for (int i = 0; i < length; i++) {
            hash += key.charAt(i);
            hash += (hash << 10);
            hash ^= (hash >> 6);
        }
        hash += (hash << 3);
        hash ^= (hash >> 11);
        hash += (hash << 15);
        return hash;
    }

    public static int bernsteinHash(String key) {
        int hash = 0;
        int length = key.length();
        for (int i = 0; i < length; i++) {
            hash = 33 * hash + key.charAt(i);
        }
        return hash;
    }

    public static int universal(char[] key, int mask, int[] tab) {
        int hash = key.length;
        int length = key.length << 3;
        for (int i = 0; i < length; i++) {
            char k = key[i >> 3];
            if ((k & 0x01) == 0) {
                hash ^= tab[i];
            }
            if ((k & 0x02) == 0) {
                hash ^= tab[i + 1];
            }
            if ((k & 0x04) == 0) {
                hash ^= tab[i + 2];
            }
            if ((k & 0x08) == 0) {
                hash ^= tab[i + 3];
            }
            if ((k & 0x10) == 0) {
                hash ^= tab[i + 4];
            }
            if ((k & 0x20) == 0) {
                hash ^= tab[i + 5];
            }
            if ((k & 0x40) == 0) {
                hash ^= tab[i + 6];
            }
            if ((k & 0x80) == 0) {
                hash ^= tab[i + 7];
            }
        }
        return (hash & mask);
    }

    public static int zobrist(char[] key, int mask, int[][] tab) {
        int hash = key.length;
        int length = key.length;
        for (int i = 0; i < length; i++) {
            hash ^= tab[i][key[i]];
        }
        return (hash & mask);
    }

    //LOCKUP3
    //见Bob Jenkins
    //32位FNV算法
    public static final int M_SHIFT = 0;

    public static int FNVHash(byte[] data) {
        int hash = (int) 2166136261L;
        for (byte b :
                data) {
            hash = (hash * 1677619) ^ b;
        }
        if (M_SHIFT == 0) {
            return hash;
        }
        return (hash ^ (hash >> M_SHIFT)) & MASK;
    }

    //改进的32位FNV算法1
    public static int FNVHash1a(byte[] data) {
        final int p = 16777619;
        int hash = (int) 2166136261L;
        for (byte b :
                data) {
            hash = (hash ^ b) * p;
        }
        hash += hash << 13;
        hash ^= hash >> 7;
        hash += hash << 3;
        hash ^= hash >> 17;
        hash += hash << 5;
        return hash;
    }

    //同上
    public static int FNVHash1a(String data) {
        final int p = 16777619;
        int hash = (int) 2166136261L;
        int length = data.length();
        for (int i = 0; i < length; i++) {
            hash = (hash ^ data.charAt(i)) * p;
        }
        hash += hash << 13;
        hash ^= hash >> 7;
        hash += hash << 3;
        hash ^= hash >> 17;
        hash += hash << 5;
        return hash;
    }

    //改进的32位FNV算法1
    public static int FNVHash1(byte[] data) {
        final int p = 16777619;
        int hash = (int) 2166136261L;
        for (byte b :
                data) {
            hash = (hash * p) ^ b;
        }
        hash += hash << 13;
        hash ^= hash >> 7;
        hash += hash << 3;
        hash ^= hash >> 17;
        hash += hash << 5;
        return hash;
    }

    //同上
    public static int FNVHash1(String data) {
        final int p = 16777619;
        int hash = (int) 2166136261L;
        int length = data.length();
        for (int i = 0; i < length; i++) {
            hash = (hash * p) ^ data.charAt(i);
            //hash = (hash ^ data.charAt(i)) * p;
        }
        hash += hash << 13;
        hash ^= hash >> 7;
        hash += hash << 3;
        hash ^= hash >> 17;
        hash += hash << 5;
        return hash;
    }

    //Thomas Wang的算法，整数hash
    public static int intHash(int key) {
        key += ~(key << 15);
        key ^= (key >>> 10);
        key += (key << 3);
        key ^= (key >>> 6);
        key += (~key << 11);
        key ^= (key >>> 16);
        return key;
    }

    //RS算法hash
    public static int RSHash(String str) {
        int b = 378551;
        int a = 63689;
        int hash = 0;
        int length = str.length();
        for (int i = 0; i < length; i++) {
            hash = hash * a + str.charAt(i);
            a = a * b;
        }
        return (hash & 0x7FFFFFFF);
    }

    //JS算法
    public static int JSHash(String str) {
        int hash = 1315423911;
        int length = str.length();
        for (int i = 0; i < length; i++) {
            hash ^= ((hash << 5) + str.charAt(i) + (hash >> 2));
        }
        return (hash & 0x7FFFFFF);
    }

    //PJW算法
    public static int PJWHash(String str) {
        int bitsInUnsignedInt = 32;
        int threeQuarters = (bitsInUnsignedInt * 3) / 4;
        int oneEighth = bitsInUnsignedInt / 8;
        int highBits = 0xFFFFFFFF << (bitsInUnsignedInt - oneEighth);
        int hash = 0;
        int test = 0;
        int length = str.length();
        for (int i = 0; i < length; i++) {
            hash = (hash << oneEighth) + str.charAt(i);
            if ((test = hash & highBits) != 0) {
                hash = ((hash ^ (test >> threeQuarters)) & (~highBits));
            }
        }
        return (hash & 0x7FFFFFFF);
    }

    //ELF算法
    public static int ELFHash(String str) {
        int hash = 0;
        int x = 0;
        int length = str.length();
        for (int i = 0; i < length; i++) {
            hash = (hash << 4) + str.charAt(i);
            if ((x = (int) (hash & 0xF0000000L)) != 0) {
                hash ^= (x >> 24);
                hash &= ~x;
            }
        }
        return (hash & 0x7FFFFFFF);
    }

    //BKDR算法
    public static int BKDRHash(String str) {
        int seed = 131;
        int hash = 0;
        int length = str.length();
        for (int i = 0; i < length; i++) {
            hash = (hash * seed) + str.charAt(i);
        }
        return (hash & 0x7FFFFFFF);
    }

    //SDBM算法
    public static int SDBMHash(String str) {
        int hash = 0;
        int length = str.length();
        for (int i = 0; i < length; i++) {
            hash = str.charAt(i) + (hash << 6) + (hash << 16) - hash;
        }
        return (hash & 0x7FFFFFFF);
    }

    //DJB算法
    public static int DJBHash(String str) {
        int hash = 5381;
        int length = str.length();
        for (int i = 0; i < length; i++) {
            hash = ((hash << 5) + 5) + str.charAt(i);
        }
        return (hash & 0x7FFFFFFF);
    }

    //DEK算法
    public static int DEKHash(String str) {
        int hash = str.length();
        int length = str.length();
        for (int i = 0; i < length; i++) {
            hash = ((hash << 5) ^ (hash >> 27)) ^ str.charAt(i);
        }
        return (hash & 0x7FFFFFFF);
    }

    //AP算法
    public static int APHash(String str) {
        int hash = 0;
        int length = str.length();
        for (int i = 0; i < length; i++) {
            hash ^= ((i & 1) == 0) ? ((hash << 7) ^ str.charAt(i) ^ (hash >> 3))
                    : (~((hash << 11) ^ str.charAt(i) ^ (hash >> 5)));
        }
        return hash;
    }

    public static int javaHash(String str) {
        int h = 0;
        int off = 0;
        int length = str.length();
        for (int i = 0; i < length; i++) {
            h = 31 * h + str.charAt(off++);
        }
        return h;
    }

    //混合hash算法,输出64位
    public static long mixHash(String str) {
        long hash = str.hashCode();
        hash <<= 32;
        hash |= FNVHash1(str);
        return hash;
    }

    public static void main(String[] args) {
        /*System.out.println(printHashStr(mixHash("10")));
        System.out.println(printHashStr(javaHash("10")));
        System.out.println(printHashStr(APHash("10")));
        System.out.println(printHashStr(DEKHash("10")));
        System.out.println(printHashStr(DJBHash("10")));
        System.out.println(printHashStr(SDBMHash("10")));
        System.out.println(printHashStr(BKDRHash("10")));
        System.out.println(printHashStr(ELFHash("10")));
        System.out.println(printHashStr(PJWHash("10")));
        System.out.println(printHashStr(JSHash("10")));
        System.out.println(printHashStr(RSHash("10")));
        System.out.println(printHashStr(intHash(10)));
        System.out.println(printHashStr(FNVHash1("10")));
        System.out.println(printHashStr(FNVHash("10".getBytes())));
        System.out.println(printHashStr(bernsteinHash("10")));
        System.out.println(printHashStr(oneByOneHash("10")));
        System.out.println(printHashStr(rotatingHash("10", MASK)));
        System.out.println(printHashStr(additiveHash("10", MASK)));*/
        System.out.println(printHashStr(FNVHash1("123456")));
        System.out.println(printHashStr(FNVHash1a("123456")));
    }

    public static String printHashStr(long data) {
        BigInteger bi = BigInteger.valueOf(data).abs();
        return bi.toString(16);
    }

}
