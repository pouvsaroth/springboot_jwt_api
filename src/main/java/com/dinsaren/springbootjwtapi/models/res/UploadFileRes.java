package com.dinsaren.springbootjwtapi.models.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadFileRes {

    private String fileName;

    private String fileDownloadUri;

    private String fileType;

    private long size;

}