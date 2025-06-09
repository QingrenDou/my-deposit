package com.yzc.common.common.util;

import cn.hutool.core.util.StrUtil;
import com.aspose.words.License;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author liuzhongxiang
 * @version 1.0
 * @title CommonUtil
 * @description
 * @create 2024/5/27 10:00
 */
public class CommonUtil {

    private static final String[] pArrayNum = {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};
    private static final String[] pArrayDigit = {"", "十", "百", "千"};
    private static final String[] pArrayUnits = {"", "万", "亿", "万亿"};

    private static final char[] CHINESE_DIGITS = {'零', '壹', '贰', '叁', '肆', '伍', '陆', '柒', '捌', '玖'};

    private static final char[] CHINESE_UNITS = {'元', '拾', '佰', '仟', '万', '拾', '佰', '仟', '亿'};

    private static final char[] CHINESE_DECIMAL_UNITS = {'角', '分'};

    /**
     * 文件名过滤指定字符
     * @param str 文件名
     * @return 过滤后的文件名
     */
    public static String getFilterCharFileName(String str) {
        String fileName = StrUtil.EMPTY;
        if (StrUtil.isNotBlank(str)) {
            fileName = str.replaceAll("[/\\\\:*?\"<>|\t]", "");
        }
        return fileName;
    }

    /**
     *
     * @param str
     * @return
     */
    public static String numToChinese(String str) {
        StringBuilder pStrReturnValue = new StringBuilder(); // 返回值
        int finger = 0; // 字符位置指针
        int pIntM = str.length() % 4; // 取模
        int pIntK;
        if (pIntM > 0)
            pIntK = str.length() / 4 + 1;
        else
            pIntK = str.length() / 4;

        // 外层循环,四位一组,每组最后加上单位: ",万亿,",",亿,",",万,"
        for (int i = pIntK; i > 0; i--) {
            int pIntL = 4;
            if (i == pIntK && pIntM != 0)
                pIntL = pIntM;
            // 得到一组四位数
            String four = str.substring(finger, finger + pIntL);
            int P_int_l = four.length();
            // 内层循环在该组中的每一位数上循环
            for (int j = 0; j < P_int_l; j++) {
                // 处理组中的每一位数加上所在的位
                int n = Integer.parseInt(four.substring(j, j + 1));
                if (n == 0) {
                    if (j < P_int_l - 1 && Integer.parseInt(four.substring(j + 1, j + 2)) > 0
                            && (!pStrReturnValue.toString().endsWith(pArrayNum[n]))) {
                        pStrReturnValue.append(pArrayNum[n]);
                    }
                } else {
                    if (!(n == 1 && (pStrReturnValue.toString().endsWith(pArrayNum[0]) || pStrReturnValue.length() == 0)
                            && j == P_int_l - 2)) {
                        pStrReturnValue.append(pArrayNum[n]);
                    }
                    pStrReturnValue.append(pArrayDigit[P_int_l - j - 1]);
                }
            }
            finger += pIntL;
            // 每组最后加上一个单位:",万,",",亿," 等
            if (i < pIntK) // 如果不是最高位的一组
            {
                if (Integer.parseInt(four) != 0)
                    // 如果所有4位不全是0则加上单位",万,",",亿,"等
                    pStrReturnValue.append(pArrayUnits[i - 1]);
            } else {
                // 处理最高位的一组,最后必须加上单位
                pStrReturnValue.append(pArrayUnits[i - 1]);
            }
        }
        return pStrReturnValue.toString();
    }


    /**
     * 验证证书
     * @return boolean
     */
    public static boolean getAsposeLicense() {
        boolean result = true;
        InputStream is = null;
        try {
            ClassPathResource resource = new ClassPathResource("template/license.xml");
            is = resource.getInputStream();
            License asposeLic = new License();
            asposeLic.setLicense(is);
            result = false;
        } catch (Exception var12) {
            var12.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException var11) {
                    var11.printStackTrace();
                }
            }

        }
        return result;
    }

    public static String moneyAmounttoChinese(String moneyAmount) {
        if (StringUtils.isEmpty(moneyAmount)) {
            return "--";
        }

        // 分离整数部分和小数部分
        int decimalIndex = moneyAmount.indexOf('.');
        String integerPart = "";
        String decimalPart = "";
        if(decimalIndex > -1){
            integerPart = moneyAmount.substring(0, decimalIndex);
            decimalPart = moneyAmount.substring(decimalIndex + 1);
        }
        else{
            integerPart = moneyAmount;
        }

        // 转换整数部分
        StringBuilder chineseInteger = StringUtils.isNotEmpty(integerPart) ? convertIntegerPart(integerPart) : new StringBuilder("");

        // 转换小数部分
        StringBuilder chineseDecimal = StringUtils.isNotEmpty(decimalPart) ? convertDecimalPart(decimalPart) : new StringBuilder("零角零分");

        // 合并整数部分和小数部分
        StringBuilder result = new StringBuilder(chineseInteger);
        if (!chineseDecimal.toString().equals("零角零分") && !chineseDecimal.toString().equals("零角")) {
            if(!result.toString().contains("元")){
                result.append("元").append(chineseDecimal);
            }
            else{
                result.append(chineseDecimal);
            }
        } else {
            if(!result.toString().contains("元")){
                result.append("元整");
            }
            else{
                result.append("整");
            }
        }

        if("元整".equals(result.toString())){
            return "零";
        }

        return result.toString();
    }

    private static StringBuilder convertIntegerPart(String integerPart) {
        StringBuilder result = new StringBuilder();
        int length = integerPart.length();
        boolean hasZero = false;

        for (int i = 0; i < length; i++) {
            int digit = Character.getNumericValue(integerPart.charAt(i));
            int unitIndex = length - i - 1;

            if (digit == 0) {
                if (!hasZero && result.length() > 0 && result.charAt(result.length() - 1) != '零') {
                    result.append(CHINESE_DIGITS[digit]);
                    hasZero = true;
                }
            } else {
                hasZero = false;
                result.append(CHINESE_DIGITS[digit]).append(CHINESE_UNITS[unitIndex]);
            }
        }

        // 去除多余的“零”
        while (result.length() > 1 && result.charAt(result.length() - 1) == '零') {
            result.deleteCharAt(result.length() - 1);
        }

        return result;
    }

    private static StringBuilder convertDecimalPart(String decimalPart) {
        StringBuilder result = new StringBuilder();
        int length = decimalPart.length();

        for (int i = 0; i < Math.min(length, 2); i++) {
            int digit = Character.getNumericValue(decimalPart.charAt(i));
            result.append(CHINESE_DIGITS[digit]).append(CHINESE_DECIMAL_UNITS[i]);
        }

        return result;
    }

}
