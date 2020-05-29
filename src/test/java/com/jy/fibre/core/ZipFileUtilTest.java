package com.jy.fibre.core;

import com.google.common.io.Files;
import com.xmgsd.lan.gwf.domain.Attachment;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author hzhou
 */
class ZipFileUtilTest {

    /**
     * 生成一个文件
     *
     * @param filename 文件名
     * @return 文件
     */
    @Contract("_ -> new")
    private @NotNull Attachment generateFile(@NotNull String filename) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            sb.append("现在时间是：");
            sb.append(LocalDateTime.now().toString());
            sb.append("\n");
        }
        Attachment attachment = new Attachment();
        attachment.setName(filename);
        attachment.setContent(sb.toString().getBytes());
        attachment.setSize(attachment.getContent().length);
        return attachment;
    }

    /**
     * 运行这个测试，会在D盘生成一个1.zip的文件
     *
     * @throws IOException 异常
     */
    @Test
    public void testCompress() throws IOException {
        final int level = 2;
        Map<String, List<Attachment>> files = new HashMap<>(level);
        for (int i = 0; i < level; i++) {

            String path = "";
            path = String.format("%s%d/", path, i + 1);
            List<Attachment> attachments = new ArrayList<>();
            for (int i2 = 0; i2 < 5; i2++) {
                Attachment attachment = generateFile(String.format("第%d个文件.txt", i2 + 1));
                attachments.add(attachment);
            }
            files.put(path, attachments);

        }


        byte[] compress = ZipFileUtil.compressAsMultipleFiles(files);
        File file = new File("d:\\7.zip");
        Files.write(compress, file);

        // 如果要下载就搭配下面这句
        // FileDownloadUtil.download("你要的文件.zip", compress, response);
    }

    @Test
    public void testUnZip() throws Exception {
        File file = new File("C:\\Users\\zzh_1\\Desktop\\20200526巡检 (1).zip");
        byte[] bytes = Files.toByteArray(file);
        Map<String, List<Attachment>> map = ZipFileUtil.unzip(bytes);
        System.out.println(map.keySet().toString());
        System.out.println(map.values().stream().flatMap(Collection::stream).map(Attachment::getName).collect(Collectors.toList()));
    }
}
