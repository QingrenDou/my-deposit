package com.yzc.common.deposit.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author DouQingRen
 * @since 2025-03-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("T_FNTDLog")
@ApiModel(value="TFntdlog对象", description="")
public class Log extends Model<Log> {

    private static final long serialVersionUID = 1L;

      @TableId("LogId")
    private String logId;

    @TableField("LogCode")
    private String logCode;

    @TableField("LogContent")
    private String logContent;

    @TableField("LogTime")
    private Date logTime;


    @Override
    public Serializable pkVal() {
        return this.logId;
    }

}
