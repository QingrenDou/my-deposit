package com.yzc.common.guar.util;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import com.yzc.common.common.enums.CommonStatusEnum;
import com.yzc.common.guar.dto.common.DecGuarSingleReqDto;
import com.yzc.common.guar.entity.GuarInfo;
import com.yzc.common.guar.enums.*;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 电子保函-工具类
 */
public class GuarUtil {

    /**
     * @desc 生成瀚华保函编号
     * @return 日期+9位随机流水号
     * @author douqr 2025-04-08
     */
    public static String genLgNo(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

        return sdf.format(new Date()) + RandomUtil.randomNumbers(8);
    }

    /**
     * 计算保函支付金额
     * @param amount 担保金额
     * @return 需要支付金额
     */
    public static BigDecimal getPayMoney(BigDecimal amount) {
        BigDecimal rateMoney = amount.multiply(new BigDecimal("0.005"));
        //低于300 按300算
        if(rateMoney.compareTo(new BigDecimal("300")) < 0){
            return new BigDecimal("300");
        }

        //取小数点后两位，四舍五入
        return rateMoney.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * 获取保函状态（用于前端显示）
     * 说明：保函状态从最后流程倒序往前判断,状态内容跟产品设定名称保持一致
     * @param guarInfo 保函完整信息
     * @return 显示状态
     */
    public static String getBidderGuarStatusStr(GuarInfo guarInfo){
        if(ObjectUtil.isNull(guarInfo)){
            return "";
        }
        //判断退保状态
        if(ObjectUtil.equals(guarInfo.getCloseStatus(), GuarCloseStatusEnum.SUCCESS.getCode())){
            return "已退保";
        }

        //判断退保状态
        if(ObjectUtil.equals(guarInfo.getCloseStatus(), GuarCloseStatusEnum.ING.getCode())){
            return "退保审核中";
        }

        //判断开函状态
        if(ObjectUtil.equals(guarInfo.getOpenStatus(), GuarOpenStatusEnum.Success.getCode())){
            return "已出函";
        }

        //判断开函失败
        if(ObjectUtil.equals(guarInfo.getOpenStatus(), GuarOpenStatusEnum.Fail.getCode())){
            return "未通过";
        }

        //已支付&开函状态为空，则为：审核中
        if(StringUtils.equals(guarInfo.getPayStatus(), GuarPayStatusEnum.Success.getCode())
                && (ObjectUtil.isNull(guarInfo.getOpenStatus()) || ObjectUtil.equals(guarInfo.getOpenStatus(), GuarOpenStatusEnum.Auditing.getCode()))){
            return "审核中";
        }

        //未支付成功：待申请
        if(StringUtils.isBlank(guarInfo.getPayStatus())
                || StringUtils.equals(guarInfo.getPayStatus(), GuarPayStatusEnum.Fail.getCode())){
            return "待申请";
        }

        return "";
    }

    /**
     * 获取采购人端保函状态（用于前端显示）
     * @param guarInfo 保函完整信息
     * @return 采购人端状态
     */
    public static String getPurGuarStatusStr(GuarInfo guarInfo){
        if(ObjectUtil.isNull(guarInfo)){
            return "";
        }

        if (ObjectUtil.equal(guarInfo.getOpenStatus(),GuarOpenStatusEnum.Success.getCode())
                && ObjectUtil.notEqual(guarInfo.getReleaseStatus(), CommonStatusEnum.Yes.getCode())
                && ObjectUtil.notEqual(guarInfo.getCloseStatus(), CommonStatusEnum.Yes.getCode())
                && (ObjectUtil.equal(guarInfo.getCompensateStatus(), GuarCompensateStatusEnum.NOT_START.getCode())
                    || ObjectUtil.equal(guarInfo.getCompensateStatus(), GuarCompensateStatusEnum.FAIL.getCode()) //索赔失败：当成已出函
                    || ObjectUtil.isNull(guarInfo.getCompensateStatus()))
        ) {
            return GuarStatusQueryPurEnum.Normal.getDesc();
        }

        //已解保
        if(ObjectUtil.equal(guarInfo.getReleaseStatus(), CommonStatusEnum.Yes.getCode())){
            return "已解保";
        }

        //已退保
        if(ObjectUtil.equal(guarInfo.getCloseStatus(), GuarCloseStatusEnum.SUCCESS.getCode())){
            return GuarStatusQueryPurEnum.Close.getDesc();
        }

        //索赔中
        if(ObjectUtil.equal(guarInfo.getCompensateStatus(), GuarCompensateStatusEnum.START.getCode())){
            return GuarStatusQueryPurEnum.Complaining.getDesc();
        }

        //索赔失败
        if(ObjectUtil.equal(guarInfo.getCompensateStatus(), GuarCompensateStatusEnum.SUCCESS.getCode())){
            return GuarStatusQueryPurEnum.Complained.getDesc();
        }

        //以下状态为特殊处理---理论上不会到这里
        if (ObjectUtil.equal(guarInfo.getOpenStatus(),GuarOpenStatusEnum.Success.getCode())){
            return "已开函";
        }
        if (ObjectUtil.equal(guarInfo.getPayStatus(),GuarPayStatusEnum.Success.getCode())){
            return "已支付";
        }

        return "未出函";
    }

    /**
     * 根据保函类型获取财务机构ID
     * @param guarTypeCode 保函类型
     * @return 财务机构id
     */
    public static Integer getFinancialIdByGuarType(Integer guarTypeCode){
        if(ObjectUtil.equals(guarTypeCode, GuarTypeCodeEnum.HanHua.getCode())){
            return 1;
        }
        if(ObjectUtil.equals(guarTypeCode, GuarTypeCodeEnum.XingTai.getCode())){
            return 4;
        }
        if(ObjectUtil.equals(guarTypeCode, GuarTypeCodeEnum.GuoKong.getCode())){
            return 5;
        }
        return null;
    }

    /**
     * 将保函信息转换为单笔解密入参
     * @param guarInfo 保函信息
     * @return 单笔解密入参
     */
    public static DecGuarSingleReqDto changeToDecSingleDto(GuarInfo guarInfo){
        if(ObjectUtil.isNull(guarInfo)){
            return null;
        }

        //复制相同字段
        DecGuarSingleReqDto reqDto = BeanUtil.copyProperties(guarInfo, DecGuarSingleReqDto.class);

        //处理不同字段
        reqDto.setBusinessType(guarInfo.getHanhuaBusiType());
        reqDto.setOpenBidTime(DateUtil.format(guarInfo.getOpenBidTime(), "yyyy-MM-dd HH:mm:ss"));
        reqDto.setEnterpriseName(guarInfo.getBidderName());
        reqDto.setBidNo(guarInfo.getProjectNo());
        reqDto.setBidName(guarInfo.getProjectName());

        return reqDto;
    }

    public static void main(String[] args) {
        System.out.println(getPayMoney(new BigDecimal("15021.555")));
    }
}
