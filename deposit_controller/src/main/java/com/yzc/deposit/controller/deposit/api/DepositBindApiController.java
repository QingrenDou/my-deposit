package com.yzc.deposit.controller.deposit.api;

import com.yzc.common.api.Result;
import com.yzc.common.deposit.dto.api.BindProjectReqDto;
import com.yzc.common.deposit.dto.api.InAccRecordQuery4BindReqDto;
import com.yzc.common.deposit.dto.api.InAccRecordQuery4BindRespDto;
import com.yzc.deposit.service.deposit.IInAccRecordService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 保证金绑定 相关接口
 */
@RestController
@RequestMapping(value = "api/depositBindApi")
public class DepositBindApiController {

    @Resource
    private IInAccRecordService inAccRecordService;

    /**
     * 业务接口2：获取待绑定来款列表
     * @param reqDto 请求参数
     * @return 待绑定数据集合
     */
    @PostMapping("queryList4Bind")
    public Result<List<InAccRecordQuery4BindRespDto>> queryList4Bind(@RequestBody @Validated InAccRecordQuery4BindReqDto reqDto) {
        List<InAccRecordQuery4BindRespDto> respDtoList = inAccRecordService.queryList4Bind(reqDto);
        return Result.success(respDtoList);
    }

    /**
     * 业务接口3：来款绑定提交
     * @param reqDto 请求参数
     * @return 无
     */
    @PostMapping("bindProject")
    public Result<String> bindProject(@RequestBody @Validated BindProjectReqDto reqDto) {
        return inAccRecordService.bindProject(reqDto);
    }
}
