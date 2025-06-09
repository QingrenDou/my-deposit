package com.yzc.deposit.controller.deposit.business;

import com.yzc.common.api.PageResult;
import com.yzc.common.api.Result;
import com.yzc.common.deposit.vo.ProjectSubAccPageReqVo;
import com.yzc.common.deposit.vo.ProjectSubAccPageRespVo;
import com.yzc.deposit.service.deposit.IProjectSubAccService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 保证金项目
 */
@RestController
@RequestMapping(value ="projectSubAcc")
public class ProjectSubAccController {

    @Resource
    private IProjectSubAccService projectSubAccService;

    /**
     * 【TD-Web-001】保证金项目列表分页查询
     * @param reqVo 请求参数
     * @return 分页查询结果过
     */
    @PostMapping(value = "pageList")
    public Result<PageResult<ProjectSubAccPageRespVo>> pageList(@RequestBody ProjectSubAccPageReqVo reqVo) {
        return Result.success(projectSubAccService.pageList(reqVo));
    }
}
