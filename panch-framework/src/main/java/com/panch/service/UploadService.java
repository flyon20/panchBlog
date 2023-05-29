package com.panch.service;

import com.panch.domain.ResponseResult;
import org.springframework.web.multipart.MultipartFile;


public interface UploadService {
    ResponseResult uploadImg(MultipartFile img);
}
