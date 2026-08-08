package com.dinsaren.springbootjwtapi.services;

import com.dinsaren.springbootjwtapi.models.FileImageDetail;
import com.dinsaren.springbootjwtapi.models.res.UploadFileRes;
import org.springframework.web.multipart.MultipartFile;

public interface UploadFileService {

    UploadFileRes uploadFile(
            MultipartFile file,
            String folder);

    FileImageDetail findImageByFileName(String fileName);
}