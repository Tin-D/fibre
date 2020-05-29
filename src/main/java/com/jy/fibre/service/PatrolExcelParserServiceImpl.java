package com.jy.fibre.service;

import com.jy.fibre.service.patrol.excel.parsers.PatrolExcelParser;
import com.xmgsd.lan.gwf.domain.Attachment;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author hzhou
 * @date 2020/5/22 11:17
 */
@Service
public class PatrolExcelParserServiceImpl {

    private final ApplicationContext applicationContext;


    @Autowired
    public PatrolExcelParserServiceImpl(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    private StringBuffer handleAttachment(@NotNull Attachment attachment, @NotNull String taskId) throws Exception {
        PatrolExcelParser parser = null;
        Map<String, PatrolExcelParser> parserMap = applicationContext.getBeansOfType(PatrolExcelParser.class);
        for (PatrolExcelParser patrolExcelParser : parserMap.values()) {
            boolean fit = patrolExcelParser.fitExcelFile(attachment);
            if (fit) {
                parser = patrolExcelParser;
                break;
            }
        }

        if (parser == null) {
            throw new IllegalArgumentException("不支持解析文件: " + attachment.getName());
        }

        return parser.parseExcel(attachment, taskId);
    }

    public StringBuffer parse(@NotNull List<Attachment> attachments, @NotNull String taskId) throws Exception {
        StringBuffer news = new StringBuffer();
        for (Attachment attachment : attachments) {
            news.append(handleAttachment(attachment, taskId));
        }
        return news;
    }
}
