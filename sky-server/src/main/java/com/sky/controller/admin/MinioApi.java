package com.sky.controller.admin;

import com.sky.properties.MinioProperties;
import com.sky.result.Result;
import com.sky.service.MinioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/admin/common")
@Slf4j
public class MinioApi {

    @Resource
    private MinioService minioService;

    @Resource
    private MinioProperties minioProperties;

    @GetMapping("/list")
    public List<Object> list() {

        List<Object> items = minioService.listObjects(minioProperties.getBucketName());

        return items;
    }

    @PostMapping("/upload")
    public Result<String> uploadFile(MultipartFile file) {
        try {
            String fileName = file.getOriginalFilename();
            assert fileName != null;

            // 根据业务设计，设置存储路径：按天创建目录
            String objectName = new SimpleDateFormat("yyyy-MM-dd/").format(new Date())
                    + UUID.randomUUID().toString()
                    + fileName.substring(fileName.lastIndexOf("."));

            minioService.upload(file);
            log.info("文件格式为:{}", file.getContentType());
            log.info("文件原名称为:{}", fileName);
            log.info("文件对象路径为:{}", objectName);
            return Result.success(minioService.getFileUrl(fileName));
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("上传失败");
        }
    }

    @DeleteMapping("/delete/{fileName}")
    public Result<String> deleteFile(@PathVariable("fileName") String fileName) {

        boolean status = minioService.delFile(minioProperties.getBucketName(), fileName);
        return status ? Result.success("删除成功") : Result.error("删除失败");
    }
}