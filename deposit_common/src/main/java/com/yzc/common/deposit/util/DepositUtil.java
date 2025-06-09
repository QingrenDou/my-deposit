package com.yzc.common.deposit.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.MD5;

import java.util.Date;

/**
 * 保证金系统 常用工具类
 */
public class DepositUtil {

    /**
     * 根据项目编号和业务类型生成项目唯一编号
     * @param projectCode 项目编号
     * @param businessType 业务类型
     * @return 项目唯一编号
     */
    public static String getProjectUniqueCode(String projectCode, Integer businessType) {
        return MD5.create().digestHex16(businessType + "_" + projectCode);
    }

    /**
     * 生成随机流水号[格式：时间戳yyyyMMddHHmmssfff + 4位随机码]
     * @return
     */
    public static String getSeqNo() {
        return DateUtil.format(new Date(), "yyyyMMddHHmmssfff") + RandomUtil.randomNumbers(4);
    }
}
