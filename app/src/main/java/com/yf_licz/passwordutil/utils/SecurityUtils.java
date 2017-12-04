package com.yf_licz.passwordutil.utils;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author yfzx-sh-licz
 * @date 2017/11/16
 */

public class SecurityUtils {


    /**
     * 默认编码格式
     */
    public static final String INPUT_CHARSET = "UTF-8";

    /**
     * 加密算法
     */
    static final String KEY_ALGORITHM = "AES";

    static final String CIPHER_ALGORITHM_CBC = "AES/CBC/PKCS5Padding";


    public static byte[] ivBytes = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};

    /**
     * AES加密
     *
     * @param str
     * @param key
     * @return
     * @throws Exception
     */
    public static String AES_Encode(String str, String key) throws Exception {
        //补齐16位
        key = key + key + "1234";
        byte[] textBytes = str.getBytes(INPUT_CHARSET);
        AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
        SecretKeySpec newKey = new SecretKeySpec(key.getBytes(INPUT_CHARSET), KEY_ALGORITHM);
        Cipher cipher = null;
        cipher = Cipher.getInstance(CIPHER_ALGORITHM_CBC);
        cipher.init(Cipher.ENCRYPT_MODE, newKey, ivSpec);
        return bytesToHex(cipher.doFinal(textBytes));
    }

    /**
     * AES解密
     *
     * @param str
     * @param key
     * @return
     * @throws Exception
     */
    public static String AES_Decode(String str, String key) throws Exception {
        //补齐16位
        key = key + key + "1234";
        byte[] textBytes = hexToBytes(str);
        AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
        SecretKeySpec newKey = new SecretKeySpec(key.getBytes(INPUT_CHARSET), KEY_ALGORITHM);
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM_CBC);
        cipher.init(Cipher.DECRYPT_MODE, newKey, ivSpec);
        return new String(cipher.doFinal(textBytes), INPUT_CHARSET);
    }

    /**
     * bytes数组转换为十六进制字符串
     *
     * @param bytes
     * @return
     */
    private static String bytesToHex(byte[] bytes) {
        //    final protected static
        char[] hexArray = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    /**
     * 十六进制字符串转换为bytes数组
     *
     * @param hexString
     * @return
     */
    private static byte[] hexToBytes(String hexString) {

        if (hexString == null || hexString.trim().length() == 0) {
            return null;
        }

        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] bytes = new byte[length];
        String hexDigits = "0123456789ABCDEF";
        for (int i = 0; i < length; i++) {
            int pos = i * 2; // 两个字符对应一个byte
            int h = hexDigits.indexOf(hexChars[pos]) << 4; // 注1
            int l = hexDigits.indexOf(hexChars[pos + 1]); // 注2
            if (h == -1 || l == -1) { // 非16进制字符
                return null;
            }
            bytes[i] = (byte) (h | l);
        }
        return bytes;
    }

    /**
     * MD5加密字符串
     *
     * @param string
     * @return
     */
    public static String MD5_Encode(String string) {
        //加盐
        string = string + "cjmlcz";
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(string.getBytes());
            String result = "";
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result += temp;
            }
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

}
