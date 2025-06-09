package com.yzc.common.guar.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

/**
 * 电子保函 分页查询入参
 */
@Data
public class GuarInfoPageReqDto extends Page {

    /**
     * 采购人id
     */
    private String companyId;
    /**
     * 投标人id
     */
    private String bidderId;

    /**
     * 投标人名称【模糊查询】
     */
    private String bidderNameLike;
    /**
     * 项目编号、名称【模糊查询】
     */
    private String projectNoLike;
    /**
     * 电子保函编号、保函编号【模糊查询】
     */
    private String guaranteeNumberLike;
    /**
     * 电子保函代码【模糊查询】
     */
    private String guaranteeCodeLike;

    /**
     * 投标人端 电子保函查询状态【同枚举：GuarStatusBidderEnum】
     */
    private Integer guarStatusBidder;

    /**
     * 电子保函类型【同枚举：GuarTypeCodeEnum】
     * 1.瀚华 2.兴泰 3.国控
     */
    private Integer guarTypeCode;

    /**
     * 采购人端 电子保函查询状态【同枚举：GuarStatusQueryPurEnum】
     * 1正常 2已解保 3退保 4索赔
     */
    private Integer guarStatusPur;

    /**
     * 开函状态(采购端要求 只展示开函成功的数据）
     */
    private Integer openStatus;
}
