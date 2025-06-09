package com.yzc.deposit.controller.deposit.job;


import cn.hutool.core.date.DateUtil;
import com.yzc.deposit.service.business.ICWHbkyBusinessService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 淮北矿业财务系统：定时任务
 */
@Component
@Slf4j
public class CWHbkyBankJob  {

    @Resource
    private Environment env;

    @Resource
    private ICWHbkyBusinessService cwHbkyBusinessService;

    /**
     * 定时刷新来款数据
     */
    @Scheduled(cron = "0 0/10 * * * ?") // 每10分钟执行一次
    public void syncInAccDataTodayTask() {
        String logCode = "(hbky)刷新来款数据(当日)任务";
        log.info("{} 开始执行======>start",logCode);


        //当前部署版本
        String profiles = env.getProperty("spring.profiles.active");
        if (StringUtils.isBlank(profiles) || !"hbky".equals(profiles)) {
            log.info("{}===>非hbky版本,无需执行,profiles={}", logCode, profiles);
            return;
        }

        String dateNow = DateUtil.format(DateUtil.date(), "yyyy-MM-dd");
        boolean syncRst = cwHbkyBusinessService.syncInAccData(dateNow,dateNow);

        log.info("{} 执行结束======>end,状态={}",logCode,syncRst);
    }

    /**
     * 定时刷新来款数据(前几天)
     */
    @Scheduled(cron = "0 0/30 * * * ?") //计划：每天凌晨执行一次
    public void syncInAccDataBeforeTask() {
        String logCode = "(hbky)刷新来款数据(前一周)任务";
        log.info("{} 开始执行======>start",logCode);


        //当前部署版本
        String profiles = env.getProperty("spring.profiles.active");
        if (StringUtils.isBlank(profiles) || !"hbky".equals(profiles)) {
            log.info("{}===>非hbky版本,无需执行,profiles={}", logCode, profiles);
            return;
        }

        //3天前日期
        String dateStart = DateUtil.format(DateUtil.offsetDay(DateUtil.date(), -3), "yyyy-MM-dd");
        String dateEnd = DateUtil.format(DateUtil.offsetDay(DateUtil.date(), -1), "yyyy-MM-dd");
        boolean syncRst = cwHbkyBusinessService.syncInAccData(dateStart,dateEnd);

        log.info("{} 执行结束======>end,状态={}",logCode,syncRst);
    }
}
