package com.dinsaren.springbootjwtapi.controllers.rest;


import com.dinsaren.springbootjwtapi.models.res.UploadFileRes;
import com.dinsaren.springbootjwtapi.services.UploadFileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/app/upload")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Upload", description = "Upload Image API")
public class UploadController {

    private final UploadFileService uploadFileService;

    @Operation(summary = "Upload Image")
    @PostMapping(
            value = "/{folder}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UploadFileRes> uploadImage(

            @PathVariable String folder,

            @RequestParam("file")
            MultipartFile file) {

        UploadFileRes response =
                uploadFileService.uploadFile(
                        file,
                        folder);

        return ResponseEntity.ok(response);
    }
}