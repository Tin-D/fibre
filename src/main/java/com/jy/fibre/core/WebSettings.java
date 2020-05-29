package com.jy.fibre.core;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.xmgsd.lan.gwf.core.SystemConfig;
import com.xmgsd.lan.roadhog.utils.JSON;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

/**
 * 网站配置
 *
 * @author hzhou
 */
@Slf4j
@Component
@Data
public class WebSettings {

    @Value("${web.title}")
    private String title;

    @Value("${web.url:null}")
    private String url;

    @Value("${web.tel:null}")
    private String tel;

    @Value("${web.copyright:null}")
    private String copyright;

    /**
     * 用户单位名称
     */
    @Value("${web.companyName}")
    private String companyName;

    private String version;

    private boolean enableValidCode;

    private Integer passwordLength;

    private Boolean passwordUseStrictPolicy;

    private String buildRevisionNumber;

    private String buildDate;

    @Autowired
    public WebSettings(SystemConfig systemConfig) {
        this.loadEnv();
        this.loadGitVersion();
        this.enableValidCode = systemConfig.isEnableValidCode();
    }

    private void loadGitVersion() {
        try {
            URL url = Resources.getResource("git.properties");
            String gitContent = Resources.asCharSource(url, Charsets.UTF_8).read();
            JsonNode node = JSON.deserializeToNode(gitContent);
            this.buildRevisionNumber = node.get("git.commit.id.abbrev").asText();
        } catch (IllegalArgumentException | IOException e) {
            log.warn("can not found git.properties");
        }
    }

    private void loadEnv() {
        try {
            URL url = Resources.getResource("env.properties");
            InputStream is = Resources.asByteSource(url).openBufferedStream();
            Properties properties = new Properties();
            properties.load(is);
            this.buildDate = properties.getProperty("build.timestamp");
        } catch (IllegalArgumentException | IOException e) {
            log.warn("can not found env.properties");
        }
    }

    public String getVersion() {
        return System.getProperty("version");
    }
}
