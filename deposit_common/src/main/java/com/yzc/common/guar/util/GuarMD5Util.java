package com.yzc.common.guar.util;

import org.apache.commons.lang.StringUtils;

import java.security.MessageDigest;

public class GuarMD5Util {

    public static String encryption(String value) {
        if (StringUtils.isBlank(value)) {
            return "";
        }
        try {
            // 1,获取MD5摘要算法的MessageDigest对象
            MessageDigest instance = MessageDigest.getInstance("MD5");
            // 2,对字符串加密,返回字节数组
            byte[] digest = instance.digest(value.getBytes());
            StringBuffer sb = new StringBuffer();
            for (byte b : digest) {
                // 3,获取字节的低八位有效值
                int i = b & 0xff;
                // 4,将整数转为16进制
                String hexString = Integer.toHexString(i);
                // 5,如果是1位的话,补0
                if (hexString.length() < 2) {
                    hexString = "0" + hexString;
                }
                // 6,把密文添加到缓存中
                sb.append(hexString);
            }

            return sb.toString();

        } catch (Exception e) {
            //如果产生错误则抛出异常
            return "";
        }
    }
}
