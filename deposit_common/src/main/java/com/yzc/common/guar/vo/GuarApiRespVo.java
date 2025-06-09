package com.yzc.common.guar.vo;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * 刷新保函数据返回值
 */
@Data
public class GuarApiRespVo extends GuarBaseInfoVo{

    //详细信息 见父类

    /*******************自定义内容*************************************************/

    /**
     * @desc 保函状态显示
     * 未申请、审核中、已出函、未通过、已解保、已退保、理赔
     */
    public String getGuarStatusShowStr() {
        Integer applyStatus = getApplyStatus();
        Integer openStatus = getOpenStatus();
        Integer closeStatus = getCloseStatus();
        Integer releaseStatus = getReleaseStatus();
        String payStatus = getPayStatus();

        //未提交申请 或 未支付
        if (applyStatus == null || applyStatus != 1 || StringUtils.isEmpty(payStatus) || !StringUtils.equals(payStatus, "00")) {
            return "未申请";
        }

        //已支付 且 未返回开函状态
        if (StringUtils.equals(payStatus, "00") && openStatus == null) {
            return "审核中";
        }

        if (closeStatus != null && closeStatus == 1) {
            return "已退保";
        }

        if (releaseStatus != null && releaseStatus == 1) {
            return "已解保";
        }

        if (openStatus != null && openStatus == 3) {
            return "未通过";
        }

        if (openStatus != null && openStatus == 2) {
            return "已出函";
        }

        //TODO 索赔
        return "";
    }
}
