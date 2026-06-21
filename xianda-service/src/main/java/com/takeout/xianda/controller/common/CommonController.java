package com.takeout.xianda.controller.common;

import com.takeout.xianda.result.Result;
import com.takeout.xianda.utils.AliOssUtil;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("api/common")
@Tag(name = "通用接口", description = "通用接口")
public class CommonController {
    @Autowired
    private AliOssUtil aliOssUtil;

    @PostMapping("upload")
    public Result<String> upload(MultipartFile file){
        try {
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String objectName = UUID.randomUUID().toString() + extension;

            String filePath = aliOssUtil.upload(file.getBytes(), objectName);
            return Result.success(filePath);

        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(500,"上传失败");
        }

        }
    }

