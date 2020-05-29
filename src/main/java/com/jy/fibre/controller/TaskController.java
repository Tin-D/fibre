package com.jy.fibre.controller;

import com.jy.fibre.bean.pe.PatrolExcelListVO;
import com.jy.fibre.bean.task.TaskDetailsVO;
import com.jy.fibre.bean.task.TaskVO;
import com.jy.fibre.service.DownloadExcelServiceImpl;
import com.jy.fibre.service.TaskServiceImpl;
import com.jy.fibre.service.UploadPatrolExcelServiceImpl;
import com.xmgsd.lan.gwf.core.audit.AbstractAuditCurdController;
import com.xmgsd.lan.gwf.core.audit.AuditModule;
import com.xmgsd.lan.gwf.utils.FileDownloadUtil;
import com.xmgsd.lan.gwf.utils.SecurityUtil;
import com.xmgsd.lan.roadhog.bean.SimpleResponseVO;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author LinGuoHua
 */
@AuditModule(moduleName = "任务管理")
@RestController
@RequestMapping("/task")
public class TaskController extends AbstractAuditCurdController<TaskServiceImpl> {

    private DownloadExcelServiceImpl downloadExcelService;

    private UploadPatrolExcelServiceImpl uploadPatrolExcelService;


    public TaskController(DownloadExcelServiceImpl downloadExcelService, UploadPatrolExcelServiceImpl uploadPatrolExcelService) {
        this.downloadExcelService = downloadExcelService;
        this.uploadPatrolExcelService = uploadPatrolExcelService;
    }

    /**
     * 下载该任务下所有设备巡检表格模板
     *
     * @param taskId   任务id
     * @param response response
     * @throws Exception 异常
     */
    @GetMapping("/download/{taskId}")
    public void downloadExcel(@PathVariable String taskId, HttpServletResponse response) throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        String fileName = formatter.format(new Date()) + "巡检" + "(" + this.service.getMapper().selectByPrimaryKey(taskId).getName() + ").zip";
        FileDownloadUtil.download(fileName, downloadExcelService.downloadExcel(taskId), response);
    }

    /**
     * 查看任务详情
     *
     * @param taskId 任务id
     * @return TaskDetailsVO集
     */
    @GetMapping("/details/{taskId}")
    public List<TaskDetailsVO> findTaskDetails(@PathVariable String taskId) {
        return this.getService().findTaskDetails(taskId);
    }

    /**
     * 根据任务编号查询任务详情
     *
     * @param code 任务编号
     * @return 任务详情
     */
    @GetMapping("/findTaskDetailsByCode/{code}")
    public TaskVO findTaskDetailsByCode(@PathVariable String code) {
        return this.getService().findTaskDetailsByCode(code);
    }

    /**
     * 录入此任务的巡检表
     *
     * @param patrolExcelListVO 所有巡检表
     */
    @PostMapping("/insertPatrolExcel")
    public void insertPatrolExcel(@RequestBody @NotNull PatrolExcelListVO patrolExcelListVO) {
        this.getService().insertPatrolExcel(patrolExcelListVO);
    }

    /**
     * 根据任务详情id查询巡检表
     *
     * @param taskDetailsId 详情id
     * @return 巡检表
     */
    @GetMapping("find_patrol_excel_by_task_details_id/{taskId}/{taskDetailsId}/{computerRoomId}")
    public PatrolExcelListVO findPatrolExcelByTaskDetailsId(@PathVariable String taskId, @PathVariable String taskDetailsId, @PathVariable String computerRoomId) {
        return this.getService().findPatrolExcelByTaskDetailsId(taskId, taskDetailsId, computerRoomId);
    }

    /**
     * 上传巡检表格
     *
     * @param files  附件
     * @param taskId 任务id
     */
    @PostMapping("/upload_patrol_excel/{taskId}")
    public SimpleResponseVO uploadPatrolExcel2(@RequestParam("files") MultipartFile files, @PathVariable("taskId") String taskId) throws Exception {
        return uploadPatrolExcelService.disposeUploadPatrolExcel(files, taskId);
    }


    /**
     * 根据任务id完成任务
     *
     * @param taskId 任务id
     */
    @PutMapping("finish_task/{taskId}")
    public void finishTask(@PathVariable @NotNull String taskId) {
        this.getService().finishTask(taskId, Objects.requireNonNull(SecurityUtil.getLoginUser()));
    }


}
