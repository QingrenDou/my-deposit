package com.yzc.common.guar.vo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

/**
 * 投标人端：分页查询入参
 */
@Data
public class GuarBidderPageReqVo extends Page {
    /**
     * 项目编号、名称【模糊查询】
     */
    private String projectNoLike;
    /**
     * 电子保函编号【模糊查询】
     */
    private String guaranteeNumberLike;
    /**
     * 电子保函代码【模糊查询】
     */
    private String guaranteeCodeLike;

    /**
     * 投标人端 电子保函查询状态【同枚举：GuarStatusBidderEnum】
     * 1,"审核中";2,"已出函",3,"未通过",4,"已退保",5,"退保审核中"
     */
    private Integer guarStatusBidder;

    /**
     * 电子保函类型【同枚举：GuarTypeCodeEnum】
     * 1.瀚华 2.兴泰 3.国控
     */
    private Integer guarTypeCode;


}
