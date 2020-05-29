package com.jy.fibre;

import com.xmgsd.lan.roadhog.mybatis.ConfigBuildHelper;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;

/**
 * @author hzhou
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class TestClassBase {
    static {
        try {
            ConfigBuildHelper.loadPropertiesFile("env.properties");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
