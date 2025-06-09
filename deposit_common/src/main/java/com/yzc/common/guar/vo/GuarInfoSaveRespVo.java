package com.yzc.common.guar.vo;

import lombok.Data;

/**
 * 保函数据提交返回对象
 */
@Data
public class GuarInfoSaveRespVo {
    /**
     * 50 电子保函
     */
    private Integer chargesType;

    /**
     * 优质采订单号
     */
    private String orderNo;
}
