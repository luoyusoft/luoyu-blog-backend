package com.luoyu.blog.project.service.fileservice.impl;

import com.alibaba.fastjson.JSONObject;
import com.luoyu.blog.common.constant.AbstractConstant;
import com.luoyu.blog.common.exception.ExceptionFactory;
import com.luoyu.blog.project.pojo.file.ExportExcelTest;
import com.luoyu.blog.project.service.fileservice.IFileService;
import com.luoyu.blog.project.pojo.uploaddownloadfile.DownloadFileReq;
import com.luoyu.blog.project.pojo.uploaddownloadfile.UploadFileReq;
import com.luoyu.blog.common.response.ResponseFactory;
import com.luoyu.blog.common.response.ResponseMsg;
import com.luoyu.blog.common.response.ResponseResult;
import com.luoyu.blog.common.util.datautil.ExcelUtil;
import com.luoyu.blog.common.util.idutil.IdUtil;

import java.net.URLEncoder;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @version 1.0
 * @author jinhaoxun
 * @date 2019-08-09
 * @description 上传下载文件服务类
 */
@Slf4j
@Service
public class FileServiceImpl implements IFileService {

    @Resource
    private ExceptionFactory exceptionFactory;
    @Value("${file.upload.path}")
    private String fileUploadPath;
    @Value("${file.download.path}")
    private String fileDownloadPath;

    /**
     * @author jinhaoxun
     * @description 上传文件
     * @param uploadFileReq 上传文件参数
     * @return ResponseResult 上传结果
     * @throws Exception
     */
    @Override
    public ResponseResult uploadFile(UploadFileReq uploadFileReq) throws Exception{
        if (uploadFileReq.getMultipartFile() == null ) {
            throw exceptionFactory.build(ResponseMsg.UPLOAD_FILE_NULL.getCode(),(ResponseMsg.UPLOAD_FILE_NULL.getMsg()));
        }
        int size = uploadFileReq.getMultipartFile().length;
        for(int i = 0 ; i<size ;i++){
            String filename = uploadFileReq.getMultipartFile()[i].getOriginalFilename();
            long fileSize = uploadFileReq.getMultipartFile()[i].getSize();
            log.info("开始上传文件，文件名称:{}，文件大小:{}",filename,fileSize);
            String url = fileUploadPath + IdUtil.getId() + filename;
            File dest = new File(url);
            if (!dest.getParentFile().exists()) {
                //父目录不存在就创建一个
                dest.getParentFile().mkdir();
            }
            //保存文件
            uploadFileReq.getMultipartFile()[i].transferTo(dest);
            log.info("文件上传成功！");
        }
        return ResponseFactory.buildSuccessResponse("上传文件成功！");
    }

    /**
     * @author jinhaoxun
     * @description 下载文件
     * @param downloadFileReq 下载文件参数
     * @return ResponseResult 下载结果
     * @throws Exception
     */
    @Override
    public ResponseResult downloadFile(DownloadFileReq downloadFileReq,HttpServletResponse httpServletResponse) throws Exception {
        File file = new File(fileDownloadPath + downloadFileReq.getFileName());
        byte[] buff = new byte[1024];
        BufferedInputStream bis = null;
        OutputStream os;
        String fileType;
        switch (downloadFileReq.getFileType()){
            case 1:
                fileType = AbstractConstant.DOWNLOAD_FILE_TYPE_IMAGE;
                break;
            default:
                fileType = "";
                break;
        }
        if(fileType == ""){
            throw exceptionFactory.build(ResponseMsg.DOWNLOAD_FILE_TYPE_WRONG.getCode(),(ResponseMsg.DOWNLOAD_FILE_TYPE_WRONG.getMsg()));
        }
        // 配置文件下载格式
        httpServletResponse.setContentType(fileType);
        httpServletResponse.setHeader("Content-Disposition", "attachment;filename="
                // 下载文件能正常显示中文
                + URLEncoder.encode(downloadFileReq.getFileName(), "UTF-8"));
        try {
            log.info("开始下载文件，文件名称:{}",downloadFileReq.getFileName());
            os = httpServletResponse.getOutputStream();
            bis = new BufferedInputStream(new FileInputStream(file));
            int i = bis.read(buff);
            while (i != -1) {
                os.write(buff, 0, buff.length);
                os.flush();
                i = bis.read(buff);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw exceptionFactory.build(ResponseMsg.DOWNLOAD_FILE_FAIL.getCode(),e.getMessage());
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            log.info("文件下载成功！");
        }
        return ResponseFactory.buildSuccessResponse("下载文件成功！");
    }

    /**
     * @author jinhaoxun
     * @description 解析Excel
     * @param multipartFile 要解析的文件
     * @return ResponseResult 解析后的数据
     * @throws Exception
     */
    @Override
    public ResponseResult resolveExcel(MultipartFile multipartFile) throws Exception {
        List<String[]> valueList = ExcelUtil.readExcel(multipartFile.getInputStream(), exceptionFactory, multipartFile.getOriginalFilename());
        List<JSONObject> paramList = new ArrayList<>((valueList.size() * 4) / 3);
        log.info("开始解析Excel...");
        for (String[] rowValues : valueList) {
            if (rowValues.length > 4) {
                throw exceptionFactory.build(ResponseMsg.EXCEL_FILE_HEADER_ERROR.getCode(), "Excel文件表头错误");
            }
            JSONObject rowValueJson = new JSONObject();
            rowValueJson.put("id", rowValues[0]);
            rowValueJson.put("name", rowValues[1]);
            rowValueJson.put("age", rowValues[2]);
            rowValueJson.put("sex", rowValues[3]);
            paramList.add(rowValueJson);
        }
        log.info("解析Excel成功！");
        return ResponseFactory.buildSuccessResponse(paramList,"Excel解析成功！");
    }

    /**
     * @author jinhaoxun
     * @description 导出Excel
     * @return ResponseResult 创建结果
     * @throws Exception
     */
    @Override
    public ResponseResult createExcel() throws Exception {
        log.info("开始创建Excel...");
        //数据
        List<ExportExcelTest> exportExcelTestList = createExcelData();
        List<Map<String,Object>> list=createExcelRecord(exportExcelTestList);
        //列名
        String[] columnNames = {"编号","名称","年龄","性别"};
        //map中的key
        String[] keys = {"id","name","age","sex"};
        //生成Excel
        HSSFWorkbook workBook = ExcelUtil.createWorkBook(list, keys, columnNames);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        workBook.write(bos);
        byte[] barray = bos.toByteArray();
        //is第一转
        InputStream is = new ByteArrayInputStream(barray);
        MultipartFile multipartFile = new MockMultipartFile("测试文件.xls","测试文件.xls","",is);

        UploadFileReq uploadFileReq = new UploadFileReq();
        MultipartFile[] multipartFiles = {multipartFile};
        uploadFileReq.setMultipartFile(multipartFiles);
        uploadFile(uploadFileReq);
        log.info("创建Excel成功！");
        return ResponseFactory.buildSuccessResponse("创建Excel成功！");
    }

    /**
     * @author jinhaoxun
     * @description 组装Excel表头数据
     * @param exportExcelTestList 要组装的数据
     * @return List<Map<String, Object>> 组装后的数据
     */
    private List<Map<String, Object>> createExcelRecord(List<ExportExcelTest> exportExcelTestList){
        List<Map<String, Object>> listmap = new ArrayList<Map<String, Object>>();
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("sheetName", "sheet1");
            listmap.add(map);
            exportExcelTestList.forEach(n -> {
                Map<String, Object> mapValue = new HashMap<String, Object>();
                mapValue.put("id",n.getId());
                mapValue.put("name",n.getName());
                mapValue.put("age",n.getAge());
                mapValue.put("sex",n.getSex());
                //mapValue.put("submitTime", DateTimeUtil.dateToStr(projectAuditListVo.getSubmitTime(),"yyyy-MM-dd"));
                //String attachmentURL = projectAuditListVo.getAttachment()==null?"无": FileUtil.getUploadPath()+projectAuditListVo.getAttachment();
                listmap.add(mapValue);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listmap;
    }

    /**
     * @author jinhaoxun
     * @description 生成Excel数据
     * @return List<ExportExcelTest> 生成的数据
     */
    private List<ExportExcelTest> createExcelData() {
        List<ExportExcelTest> exportExcelTestList = new ArrayList<>();
        int id = 10000;
        for (int i = 0; i < 10; i++) {
            ExportExcelTest pcd = new ExportExcelTest();
            pcd.setId(id+i);
            pcd.setName("落雨"+i);
            pcd.setAge(10+i);
            pcd.setSex("男"+i);
            exportExcelTestList.add(pcd);
        }
        return exportExcelTestList;
    }
}
