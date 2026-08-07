package com.dinsaren.springbootjwtapi.services;

import com.dinsaren.springbootjwtapi.constants.Constants;
import com.dinsaren.springbootjwtapi.models.FileImageDetail;
import com.dinsaren.springbootjwtapi.models.res.UploadFileRes;
import com.dinsaren.springbootjwtapi.repository.FileImageDetailRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class UploadFileServiceImpl implements UploadFileService {

    @Value("${upload.server.path}")
    private String serverPath;

    @Value("${server.base-url}")
    private String baseUrl;

    @Autowired
    private FileImageDetailRepository fileImageDetailRepository;

    @Override
    public UploadFileRes uploadFile(MultipartFile file) {

        UploadFileRes response = new UploadFileRes();

        try {

            if (file == null || file.isEmpty()) {
                throw new RuntimeException("File is empty.");
            }

            // Create upload folder if it does not exist
            File uploadDir = new File(serverPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            String originalFileName =
                    StringUtils.cleanPath(file.getOriginalFilename());

            String extension = getFileExtension(originalFileName);

            List<String> allowExtensions =
                    Arrays.asList("jpg", "jpeg", "png");

            if (extension == null ||
                    !allowExtensions.contains(extension.toLowerCase())) {

                throw new RuntimeException(
                        "Only JPG, JPEG and PNG images are allowed."
                );
            }

            // Generate unique filename
            String fileName =
                    UUID.randomUUID().toString() + "." + extension;

            File destination =
                    new File(uploadDir, fileName);

            file.transferTo(destination);

            // Save metadata
            FileImageDetail detail = new FileImageDetail();

            detail.setFileName(fileName);
            detail.setOriginalFileName(getFileNoExtension(originalFileName));
            detail.setFilePath(destination.getAbsolutePath());
            detail.setFileType(file.getContentType());
            detail.setFileSize(file.getSize());
            detail.setStatus(Constants.STATUS_ACTIVE);

            fileImageDetailRepository.save(detail);

            response.setFileName(fileName);

            response.setFileDownloadUri(
                    baseUrl + "/image/" + fileName
            );

            response.setFileType(file.getContentType());

            response.setSize(file.getSize());

            log.info("Upload Success : {}", fileName);

        } catch (IOException e) {

            log.error("Upload file failed", e);

            throw new RuntimeException("Upload file failed.");

        }

        return response;
    }

    @Override
    public FileImageDetail findImageByFileName(String fileName) {

        return fileImageDetailRepository.findByFileNameAndStatus(
                fileName,
                Constants.STATUS_ACTIVE
        );

    }

    private String getFileExtension(String fileName) {

        int dotIndex = fileName.lastIndexOf('.');

        if (dotIndex == -1) {
            return null;
        }

        return fileName.substring(dotIndex + 1);

    }

    private String getFileNoExtension(String fileName) {

        int dotIndex = fileName.lastIndexOf('.');

        if (dotIndex == -1) {
            return fileName;
        }

        return fileName.substring(0, dotIndex);

    }

}