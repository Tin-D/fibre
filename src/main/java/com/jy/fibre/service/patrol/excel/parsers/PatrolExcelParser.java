package com.jy.fibre.service.patrol.excel.parsers;

import com.jy.fibre.bean.pe.PatrolExcelData;
import com.xmgsd.lan.gwf.domain.Attachment;
import org.jetbrains.annotations.NotNull;

/**
 * 运维表格解析
 *
 * @param <T> 表格对应的数据
 * @author hzhou
 */
public interface PatrolExcelParser<T extends PatrolExcelData> {

    /**
     * 匹配附件
     *
     * @param attachment 附件
     * @return true: 当前附件可以匹配这个解析器，false：不匹配这个解析器
     */
    boolean fitExcelFile(@NotNull Attachment attachment);


    /**
     * 解析Excel，多数情况下，解析出来的都应该是个列表，某些情况不是列表的，请包装成一个列表
     *
     * @param attachment 附件
     * @param taskId     任务id
     * @return 解析出来的数据
     * @throws Exception 异常
     */
    StringBuffer parseExcel(@NotNull Attachment attachment, @NotNull String taskId) throws Exception;
}
