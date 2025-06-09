package com.yzc.common.guar.vo;

import lombok.Data;

/**
 * 采购人查询保函信息返回值
 */
@Data
public class GuarInfoPurRespVo extends GuarBaseInfoVo{

    //详细信息 见父类

    /*******************自定义内容*************************************************/
    /**
     * 保函状态汇总，用于前端显示(仅用于采购人保函页)
     */
    private String purGuarStatusStr;

    /**
     * 是否可以发起索赔
     */
    private Integer canApplyCompensate;

    /**
     * 可查看索赔信息[已发起过索赔，就可以查看]
     */
    private Integer canViewCompensate;

    /**
     * 判断索赔条件描述
     */
    private String canApplyCompensateDesc;
}
