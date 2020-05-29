package com.jy.fibre;

import com.xmgsd.lan.gwf.security.AbstractAuthConfiguration;
import org.springframework.context.annotation.Configuration;

/**
 * @author hzhou
 */
@Configuration
public class SecurityConfig extends AbstractAuthConfiguration {
    /**
     * 允许匿名访问的URL
     *
     * @return 允许匿名访问的URL
     */
    @Override
    protected String[] getAnonymousUrls() {
        return new String[]{
                "/",
                "/logo",
                "/favicon.ico",
                "/web_settings",
                "/dictionary_code",
                "/group_manager",
                "/code_image",
                "/reset_my_password",
                "/validate_password",
                "/public/*",
                "/group",
                "/attachment/**",
                "/user/reset_my_password",
        };
    }
}
