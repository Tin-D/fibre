package com.jy.fibre.core;

import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.xmgsd.lan.gwf.domain.Attachment;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * 文件压缩工具
 *
 * @author hzhou
 */
public class ZipFileUtil {

    public static byte[] compress(@NotNull Map<String, Attachment> files) throws IOException {
        Preconditions.checkState(!files.isEmpty(), "必须传入要压缩的文件");
        try (
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                ZipOutputStream zos = new ZipOutputStream(outputStream);
        ) {
            for (Map.Entry<String, Attachment> entry : files.entrySet()) {
                String filePath = entry.getKey().replace("\\", "/");
                Attachment file = entry.getValue();

                if (!filePath.endsWith("/")) {
                    filePath += "/";
                }

                ZipEntry zipEntry = new ZipEntry(filePath + file.getName());
                zipEntry.setSize(file.getSize());

                // 写入压缩包
                zos.putNextEntry(zipEntry);
                zos.write(file.getContent());
            }

            zos.close();
            return outputStream.toByteArray();
        }
    }

    /**
     * 压缩文件，文件夹下有多个文件时
     *
     * @param files 路径对应的文件集合
     * @return byte[]
     * @throws IOException io异常
     */
    public static byte[] compressAsMultipleFiles(@NotNull Map<String, List<Attachment>> files) throws IOException {
        Preconditions.checkState(!files.isEmpty(), "必须传入要压缩的文件");
        try (
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                ZipOutputStream zos = new ZipOutputStream(outputStream);
        ) {
            for (Map.Entry<String, List<Attachment>> entry : files.entrySet()) {
                StringBuilder filePath = new StringBuilder(entry.getKey().replace("\\", "/"));
                List<Attachment> attachments = entry.getValue();
                for (Attachment file : attachments) {
                    if (!filePath.toString().endsWith("/")) {
                        filePath.append("/");
                    }

                    ZipEntry zipEntry = new ZipEntry(filePath + file.getName());
                    zipEntry.setSize(file.getSize());

                    // 写入压缩包
                    zos.putNextEntry(zipEntry);
                    zos.write(file.getContent());
                }

            }

            zos.close();
            return outputStream.toByteArray();
        }
    }

    public static @NotNull Map<String, List<Attachment>> unzip(@NotNull byte[] zipFile, @NotNull Charset charset) throws IOException {
        Map<String, List<Attachment>> result = new HashMap<>();
        ZipInputStream inputStream = new ZipInputStream(new ByteArrayInputStream(zipFile), charset);
        ZipEntry zipEntry;
        while ((zipEntry = inputStream.getNextEntry()) != null) {
            if (!zipEntry.isDirectory()) {
                List<String> paths = Splitter.on("/").trimResults().omitEmptyStrings().splitToList(zipEntry.getName());
                String filename = Iterables.getLast(paths);
                String filePath = Joiner.on("/").join(paths.subList(0, paths.size() - 1));
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer, 0, buffer.length)) > -1) {
                    outputStream.write(buffer, 0, length);
                }

                List<Attachment> attachments = result.computeIfAbsent(filePath, i -> new ArrayList<>());
                Attachment attachment = new Attachment();
                attachment.setName(filename);
                attachment.setContent(outputStream.toByteArray());
                attachments.add(attachment);
            } else {
                result.putIfAbsent(zipEntry.getName(), new ArrayList<>());
            }
        }

        List<String> removeKeys = new ArrayList<>();
        for (Map.Entry<String, List<Attachment>> entry : result.entrySet()) {
            if (entry.getValue().isEmpty()) {
                removeKeys.add(entry.getKey());
            }
        }
        for (String removeKey : removeKeys) {
            result.remove(removeKey);
        }

        return result;
    }

    public static @NotNull Map<String, List<Attachment>> unzip(@NotNull byte[] zipFile) throws IOException {
        try {
            return unzip(zipFile, Charsets.UTF_8);
        } catch (IllegalArgumentException | IOException e) {
            return unzip(zipFile, Charset.forName("GBK"));
        }
    }
}
