package com.jy.fibre;

import com.xmgsd.lan.gwf.AbstractGwfConfiguration;
import com.xmgsd.lan.gwf.core.SystemConfig;
import com.xmgsd.lan.gwf.service.CacheList;
import com.xmgsd.lan.roadhog.bean.DataSourceSettingBO;
import com.xmgsd.lan.roadhog.bean.SqlSessionFactorySettingBO;
import com.xmgsd.lan.roadhog.mybatis.ConfigBuildHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.flywaydb.core.Flyway;
import org.jetbrains.annotations.NotNull;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import tk.mybatis.mapper.code.Style;

import javax.sql.DataSource;
import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hzhou
 */
@Slf4j
@Configuration
@EnableTransactionManagement
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@MapperScan("com.jy.fibre.dao")
@PropertySource(value = "${configFile}", encoding = "utf-8")
public class FirbreConfig extends AbstractGwfConfiguration {

    private final static String MAPPER_LOCATION = "classpath*:mariadb/mappers/**/*.xml";

    private final SystemConfig systemConfig;

    @Value("${db.migrate:false}")
    private boolean dbMigrate;

    @Autowired
    public FirbreConfig(SystemConfig systemConfig) {
        this.systemConfig = systemConfig;
    }

    @Bean
    public DataSource dataSource() {
        // 这里不能开启wall，因为druid不支持达梦数据库
        return ConfigBuildHelper.buildDataSource(new DataSourceSettingBO(
                MessageFormat.format("{0}/{1}", this.systemConfig.getDbUrl(), this.systemConfig.getDbName()),
                this.systemConfig.getDbUsername(),
                this.systemConfig.getDbPassword(),
                false,
                0
        ));
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        return ConfigBuildHelper.buildSqlSessionFactory(new SqlSessionFactorySettingBO(
                MAPPER_LOCATION,
                "com.jy.fibre.core",
                null,
                true,
                Style.camelhumpAndLowercase,
                null,
                true,
                "mariadb"
        ), dataSource);
    }

    @Bean
    public DataSourceTransactionManager dataSourceTransactionManager(DataSource dataSource) {
        return ConfigBuildHelper.buildTransactionManager(dataSource);
    }

    @Override
    public CacheList cacheList() {
        return new CacheList() {
            @Override
            public @NotNull <V> List<V> getAll(@NotNull Cache cache) {
                return ((CaffeineCache) cache).getNativeCache().asMap().values().stream().map(i -> (V) i).collect(Collectors.toList());
            }
        };
    }

    @Bean
    @DependsOn("flyway")
    public CacheManager cacheManager() {
        return new CaffeineCacheManager();
    }

    /**
     * 调用flyway升级数据库
     *
     * @param dataSource 连接
     * @return flyway对象
     */
    @Bean
    public Flyway flyway(DataSource dataSource) {
        if (dbMigrate) {
            Flyway load = Flyway.configure().dataSource(dataSource).load();
            load.migrate();
            return load;
        }
        return null;
    }
}
