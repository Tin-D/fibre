package com.jy.fibre;

import com.xmgsd.lan.roadhog.mybatis.ConfigBuildHelper;
import org.apache.xmlbeans.SystemProperties;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.security.Security;
import java.util.Locale;

/**
 * @author hzhou
 */
@SpringBootApplication(scanBasePackages = {"com.jy.fibre", "com.xmgsd.lan.gwf"})
public class FibreApplication {

    public static void main(String[] args) throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        // 强制设置默认语言为中文
        if (Locale.getDefault() != Locale.CHINA) {
            System.out.println("default locale: " + Locale.getDefault().getLanguage() + "; force set to zh_CN");
            Locale.setDefault(Locale.CHINA);
        }

        System.out.println("default file encoding: " + System.getProperty("file.encoding"));

        ConfigBuildHelper.loadPropertiesFile("env.properties");
        System.out.println("load config file: " + SystemProperties.getProperty("configFile"));
        SpringApplication.run(FibreApplication.class, args);
    }
}
