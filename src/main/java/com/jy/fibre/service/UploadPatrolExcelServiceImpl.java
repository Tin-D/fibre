package com.jy.fibre.service;


import com.jy.fibre.core.ZipFileUtil;
import com.jy.fibre.dao.TaskDetailsDao;
import com.xmgsd.lan.gwf.domain.Attachment;
import com.xmgsd.lan.roadhog.bean.IdNameEntry;
import com.xmgsd.lan.roadhog.bean.SimpleResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * @author dzq
 * 上传内置模板处理
 */
@Slf4j
@Service
public class UploadPatrolExcelServiceImpl {

    private TaskDetailsDao taskDetailsDao;

    private PatrolExcelParserServiceImpl patrolExcelParserService;

    public UploadPatrolExcelServiceImpl(TaskDetailsDao taskDetailsDao, PatrolExcelParserServiceImpl patrolExcelParserService) {
        this.taskDetailsDao = taskDetailsDao;
        this.patrolExcelParserService = patrolExcelParserService;
    }

    /**
     * 处理上传的文件，并分析数据插入到数据库
     *
     * @param multipartFile 压缩文件
     * @param taskId        任务id
     * @throws Exception 异常
     */
    public SimpleResponseVO disposeUploadPatrolExcel(MultipartFile multipartFile, @NotNull String taskId) throws Exception {

        List<IdNameEntry> computerRoomNames = taskDetailsDao.findComputerRoomNameByTaskId(taskId);
        Map<String, List<Attachment>> fileContent = ZipFileUtil.unzip(multipartFile.getBytes());
        StringBuffer news = new StringBuffer();
        StringBuffer record = new StringBuffer();
        SimpleResponseVO simpleResponseVO = new SimpleResponseVO();
        for (Map.Entry<String, List<Attachment>> entry : fileContent.entrySet()) {
            news.append(entry.getKey());
            record.append(entry.getKey());
            news.append(":n");
            record.append(":n");
            for (IdNameEntry computerRoomName : computerRoomNames) {
                if (entry.getKey().contains(computerRoomName.getName())) {
                    news.append(patrolExcelParserService.parse(entry.getValue(), taskId));
                    break;
                }
            }
            news.append("n");
            record.append("n");
        }
        if (record.length() == news.length()) {
            simpleResponseVO.setSuccess(false);
            simpleResponseVO.setCode(500);
            simpleResponseVO.setMessage("请检查该文件是否属于该运维任务");
        } else {
            simpleResponseVO.setSuccess(true);
            simpleResponseVO.setMessage(news.toString());
        }
        return simpleResponseVO;

    }
}

