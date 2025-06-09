package com.yzc.common.deposit.dto.deposit;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class ProjectSubAccPageReqDto extends Page {

    public String projectNameLike; //项目编号、名称【模糊查询】
    public String openBidTimeStart; //开标时间【开始时间】
    public String openBidTimeEnd; //开标时间【结束时间】
    public String companyId; //当前公司id

    /*****************查询权限相关**************************/

    /**
     * 查询权限类型：同枚举：QueryPermissionTypeEnum
     */
    private Integer queryPermissionType;

    /**
     * 当前用户id(必传字段)
     */
    private String curUserId;

    /**
     * 部门权限：所有管辖部门下 所有用户id集合
     */
    private List<String> deptUserList;
}
