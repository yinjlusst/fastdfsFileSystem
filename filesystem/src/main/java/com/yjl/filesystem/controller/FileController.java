package com.yjl.filesystem.controller;

import com.yjl.filesystem.bean.DmsProcessDocument;
import com.yjl.filesystem.service.DocumentService;
import com.yjl.filesystem.utils.FastdfsUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.lang.annotation.Documented;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("fileSystem")
public class FileController {

    @Autowired
    DocumentService documentService;

    @RequestMapping("hello")
    @ResponseBody
    public String hello(){
        return "hello";
    }


    /**
     * 上传文件
     */
    @RequestMapping("uploadFile")
    @ResponseBody
    public String uploadDocuments(MultipartFile file, HttpServletRequest request){
        //String username = "yinjianliang";
        Integer userId = 8;
        //将上传的文件保存到分布式文件存储系统
        DmsProcessDocument dmsProcessDocument = new DmsProcessDocument();

        String url = FastdfsUtil.uploadDocument(file);
        if (StringUtils.isBlank(url)){
            return "fail";
        }
        dmsProcessDocument.setUrl(url);
        dmsProcessDocument.setSize(String.valueOf(file.getSize()));
        dmsProcessDocument.setDocumentName(file.getOriginalFilename());
        dmsProcessDocument.setUpdateTimes("0");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String nowDate = format.format(Calendar.getInstance().getTime());
        dmsProcessDocument.setUploadDate(nowDate);
        dmsProcessDocument.setUploadUserId(userId);
        //存储进数据库
        String success = documentService.saveDocument(dmsProcessDocument);
        if (!success.equals("fail")){
            //上传成功了
            return url;
        }
        //可能需要删除分布式文件存储系统上的数据（回滚）
        return "fail";
    }


    /**
     *下载文件
     * 返回的是 将文件保存到服务器之后的文件地址
     */
    @RequestMapping("downloadDocument")
    @ResponseBody
    public String downloadDocument(String url, HttpServletRequest request) throws IOException {
        String[] path = url.split("/");
        //获取groupName和remoteFilename
        String groupName = path[3];
        String remoteFilename = "";
        for (int i = 4; i < path.length; i++) {
            remoteFilename += path[i];
            if (i != path.length - 1) {
                remoteFilename += "/";
            }
        }
        //创建输出流
        //获取服务器真实路径
        String realPath = request.getSession().getServletContext().getRealPath("/tmp/");
        //判断该路径是否存在
        File file = new File(realPath);
        if(!file.exists()){
            //创建文件
            file.mkdir();
            realPath += "a.txt";
            File file1 = new File(realPath);
            file1.createNewFile();
        }else{
            file.delete();
            //创建文件
            file.mkdir();
            realPath += "a.txt";
            File file1 = new File(realPath);
            file1.createNewFile();
        }
        //下载文件
        OutputStream outputStream = null;

        outputStream = new FileOutputStream(realPath);
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
        int result = FastdfsUtil.downloadDocument(groupName, remoteFilename, bufferedOutputStream);
        if (result == -1 ) {
            return "fail";
        }
        return realPath;
    }


}
