package com.example.movie.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Log4j2
@Controller
@RequestMapping("/upload")
public class UploadController {

    // application.properties 에 설정한 변수 가져오기
    @Value("${com.example.upload.path}")
    private String uploadPath;

    @GetMapping("/ex1")
    public void getMethodName() {
        log.info("upload form 요청");
    }

    // MultipartFile : 파일을 받을수 있는 객체
    // MultipartFile[] : 여러개 받아야 되서 배열 사용
    @PostMapping("/uploadAjax")
    public void postMethodName(MultipartFile[] uploadFiles) {

        for (MultipartFile multipartFile : uploadFiles) {
            String oriName = multipartFile.getOriginalFilename();
            String fileName = oriName.substring(oriName.lastIndexOf("\\") + 1);
            log.info("파일정보 - 전체경로 {}", oriName);
            log.info("파일정보 - 파일명 {}", fileName);

            // 파일을 업로드하면 폴더 생성 (오늘날짜로 생성) 후 폴더안에 업로드
            String saveFolderPath = makeFolder();
            String uuid = UUID.randomUUID().toString();
            String saveName = uploadPath + File.separator + saveFolderPath + File.separator + uuid + "_" + fileName;
            // Path : java.nio.Path
            Path savePath = Paths.get(saveName);
            try {
                multipartFile.transferTo(savePath);
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 오늘날짜로 폴더 생성 메소드
    private String makeFolder() {
        String dateStr = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String folderStr = dateStr.replace("/", File.separator);

        File uploadPathFolder = new File(uploadPath, folderStr);
        if (!uploadPathFolder.exists())
            uploadPathFolder.mkdirs();
        return folderStr;
    }

}
