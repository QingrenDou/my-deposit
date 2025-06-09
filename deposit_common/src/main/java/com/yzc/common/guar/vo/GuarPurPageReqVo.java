package com.yzc.common.guar.vo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

/**
 * 投标人端：分页查询入参
 */
@Data
public class GuarPurPageReqVo extends Page {
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
     * 投标人名称【模糊查询】
     */
    private String bidderNameLike;

    /**
     * 采购人端 电子保函查询状态【同枚举：GuarStatusQueryPurEnum】
     * 1正常 2已解保 3退保 4索赔
     */
    private Integer guarStatusPur;

}
