package com.yzc.deposit.service.business;

/**
 * 聚合业务-淮矿财务系统对接接口
 */
public interface ICWHbkyBusinessService {

    /**
     * 同步财务系统来款数据
     *
     * @param sDate 开始日期
     * @param eDate 截止日期
     * @return true:成功，false:失败
     */
    boolean syncInAccData(String sDate, String eDate);

    /**
     * 根据子账号获取财务系统来款数据
     * @param subAcc 子账号
     * @param sDate 开始日期
     * @param eDate 截止日期
     * @return true:成功，false:失败
     */
    boolean getInAccDataBySubAcc(String subAcc,String sDate,String eDate);
}
