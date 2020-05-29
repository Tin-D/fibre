package com.jy.fibre.controller;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.io.BaseEncoding;
import com.jy.fibre.core.WebSettings;
import com.xmgsd.lan.gwf.bean.CodeImageVO;
import com.xmgsd.lan.gwf.bean.ExcelGenerateVO;
import com.xmgsd.lan.gwf.bean.LoginUser;
import com.xmgsd.lan.gwf.bean.dsc.PasswordPolicyVO;
import com.xmgsd.lan.gwf.enums.DynamicSystemConfigType;
import com.xmgsd.lan.gwf.service.DynamicSystemConfigsService;
import com.xmgsd.lan.gwf.service.OneOffTokenServiceImpl;
import com.xmgsd.lan.gwf.utils.CodeUtil;
import com.xmgsd.lan.gwf.utils.ExcelHelper;
import com.xmgsd.lan.gwf.utils.FileDownloadUtil;
import com.xmgsd.lan.gwf.utils.PasswordUtil;
import com.xmgsd.lan.roadhog.bean.SimpleResponseVO;
import com.xmgsd.lan.roadhog.utils.JSON;
import org.apache.commons.math3.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

import static com.xmgsd.lan.gwf.core.SystemConfig.VALID_CODE_ATTRIBUTE;

/**
 * @author hzhou
 */
@RestController
public class IndexController {

    public HttpSession session;

    public WebSettings webSettings;

    private DynamicSystemConfigsService dynamicSystemConfigsService;

    private OneOffTokenServiceImpl oneOffTokenService;

    public IndexController(HttpSession httpSession, WebSettings webSettings, DynamicSystemConfigsService dynamicSystemConfigsService, OneOffTokenServiceImpl oneOffTokenService) {
        this.session = httpSession;
        this.webSettings = webSettings;
        this.dynamicSystemConfigsService = dynamicSystemConfigsService;
        this.oneOffTokenService = oneOffTokenService;
    }

    @GetMapping("/web_settings")
    public WebSettings webSettings() {
        return this.webSettings;
    }

    @GetMapping(value = "/logo")
    public String logo() throws IOException {
        String logo = this.dynamicSystemConfigsService.getConfig(DynamicSystemConfigType.Logo);
        if (!Strings.isNullOrEmpty(logo)) {
            Object result = JSON.deserialize(logo, Object.class);
            return result.toString();
        }
        return null;
    }

    @GetMapping("/favicon.ico")
    public byte[] favico() throws IOException {
        String logo = this.dynamicSystemConfigsService.getConfig(DynamicSystemConfigType.Logo);
        if (!Strings.isNullOrEmpty(logo)) {
            String result = JSON.deserialize(logo, Object.class).toString().split(",")[1];
            return BaseEncoding.base64().decode(result);
        }
        return null;
    }

    @GetMapping("/current_user")
    public LoginUser getCurrentUser(@AuthenticationPrincipal LoginUser user) {
        return user;
    }

    @GetMapping(value = "/code_image", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] codeImage() throws IOException {
        CodeImageVO codeImageVO = CodeUtil.generateCodeAndPic();
        session.setAttribute(VALID_CODE_ATTRIBUTE, codeImageVO.getCode());
        BufferedImage img = codeImageVO.getImage();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(img, "jpg", baos);
        return baos.toByteArray();
    }

    @GetMapping("/unique_id")
    public String getUniqueId() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    @PostMapping("/validate_password")
    public SimpleResponseVO validatePassword(@RequestBody String password) throws IOException, IllegalAccessException, InstantiationException {
        try {
            PasswordUtil.checkPasswordPolicy(password, this.dynamicSystemConfigsService.getConfig(DynamicSystemConfigType.PasswordPolicy, PasswordPolicyVO.class));
            return new SimpleResponseVO(true);
        } catch (IllegalArgumentException e) {
            return new SimpleResponseVO(false, e.getMessage(), HttpStatus.OK.value());
        }
    }

    @PostMapping("/generate_excel")
    public String generateExcel(@RequestBody ExcelGenerateVO excelGenerateVO) throws IOException {
        byte[] bytes = ExcelHelper.generateExcel(excelGenerateVO);
        String fileName = Strings.isNullOrEmpty(excelGenerateVO.getFileName()) ? "1.xlsx" : excelGenerateVO.getFileName();
        if (!fileName.endsWith(".xlsx")) {
            fileName = fileName + ".xlsx";
        }
        return this.oneOffTokenService.put(new Pair<>(fileName, bytes));
    }

    @GetMapping("/download_generate_excel/{token}")
    public void downloadGenerateExcel(HttpServletResponse response, @PathVariable String token) throws IOException {
        Pair pair = Preconditions.checkNotNull(this.oneOffTokenService.get(token, Pair.class), "没有找到附件");
        FileDownloadUtil.download(pair.getKey().toString(), ((byte[]) pair.getValue()), response);
    }
}
