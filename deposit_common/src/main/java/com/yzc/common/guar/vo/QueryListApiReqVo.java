package com.yzc.common.guar.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 对外接口-查询保函列表请求参数
 */
@Data
public class QueryListApiReqVo {
    /**
     * @desc 项目id集合【必填】
     */
    @NotNull(message = "项目id集合不能为空")
    private List<String> projectIdList;
    /**
     * 投标企业id【选填，可指定查询单企业数据】
     */
    private String bidderId;
    /**
     * 开函状态（2-通过，3-未通过）【选填，可指定状态查询】
     */
    private Integer openStatus;

    /**
     * 解保状态【1已发起解保】【选填，可指定状态查询，默认查询所有】
     * 解保：项目正常结束时，解除非中标人的保函有效性
     */
    private Integer releaseStatus;

    /**
     * 退保状态[1已退 0未退]【选填，可指定状态查询，默认查询所有】
     * 退保：项目异常或取消等，需要退回投标人的购买金额
     */
    private Integer closeStatus;
}
