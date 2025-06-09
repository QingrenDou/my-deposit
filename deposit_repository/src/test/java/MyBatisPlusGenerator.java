
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.converts.SqlServerTypeConvert;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.IColumnType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.util.HashMap;
import java.util.Map;

/**
 * mybatis-plus代码生成器(用于生成entity)<br>
 * @author liuzhongxiang
 */
public class MyBatisPlusGenerator {

    public static void main(String[] args) {
        AutoGenerator mpg = new AutoGenerator();

        String targetPath = "E:\\TestCode\\yzc_deposit";

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        gc.setOutputDir(targetPath);//这里写你自己的java目录
        gc.setSwagger2(true); //实体属性 Swagger2 注解
        gc.setFileOverride(true);//是否覆盖
        gc.setActiveRecord(true);//开启 ActiveRecord 模式
        gc.setEnableCache(false);// XML 二级缓存
        gc.setBaseResultMap(true);// XML ResultMap
        gc.setBaseColumnList(false);// XML columList
        gc.setAuthor("DouQingRen");//作者
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setTypeConvert(new SqlServerTypeConvert() {
            // 自定义数据库表字段类型转换【可选】
            @Override
            public IColumnType processTypeConvert(GlobalConfig gc, String fieldType) {
                System.out.println("转换类型：" + fieldType);
                // 注意！！processTypeConvert 存在默认类型转换，如果不是你要的效果请自定义返回、非如下直接返回。
                //默认会把日期类型 转为LocalDateTime ，在查询的时候会报错，这里改为Date
                String t = fieldType.toLowerCase();
                if (t.contains("date") || t.contains("time")) {
                    return DbColumnType.DATE;
                } else {
                    return super.processTypeConvert(gc, fieldType);
                }

            }
        });
        dsc.setDbType(DbType.DM);
        dsc.setDriverName("dm.jdbc.driver.DmDriver");
        dsc.setUsername("TENDDEPOSIT");
        dsc.setPassword("tenddeposit@123");
        dsc.setUrl("jdbc:dm://192.168.46.177:5236/TENDDEPOSIT");
        mpg.setDataSource(dsc);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setEntityLombokModel(true);//【实体】是否为lombok模型（默认 false）
        strategy.setTablePrefix(new String[]{"T_FNTD", "temp_", "log_", "toa_", "process_"});//
        // 此处可以修改为您的表前缀

        //需要生成的表和包名
        strategy.setInclude(new String[]{"T_FNTDBankKey"});
        String pkg = "com.yzc.deposit.common.entity";

        strategy.setNaming(NamingStrategy.underline_to_camel);// 表名生成策略
        mpg.setStrategy(strategy);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setParent(pkg);
        mpg.setPackageInfo(pc);

        // 注入自定义配置，可以在 VM 中使用 cfg.abc 设置的值
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                Map<String, Object> map = new HashMap<>();
                map.put("abc", this.getConfig().getGlobalConfig().getAuthor() + "-mp");
                this.setMap(map);
            }
        };
        mpg.setCfg(cfg);

        // 执行生成
        mpg.execute();

        // 打印注入设置
        System.err.println(mpg.getCfg().getMap().get("abc"));
    }

}
