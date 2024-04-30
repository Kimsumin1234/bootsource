package com.example.movie.controller;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.movie.dto.UploadResultDto;

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
    // ResponseEntity : 데이터 + 상태코드 (일반 컨트롤러 에서도 REST 컨트롤러 방식으로 사용하는 방법)
    @PostMapping("/uploadAjax")
    public ResponseEntity<List<UploadResultDto>> postMethodName(MultipartFile[] uploadFiles) {
        log.info("파일 업로드 요청 /uploadAjax");

        List<UploadResultDto> uploadResultDtos = new ArrayList<>();

        for (MultipartFile multipartFile : uploadFiles) {

            // 이미지 파일만 업로드 하는 if 구문
            if (!multipartFile.getContentType().startsWith("image")) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }

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
                // 원본 파일을 저장
                multipartFile.transferTo(savePath);

                // 썸네일 파일 저장 ("s_" 추가)
                // 1. 썸네일 파일 경로 생성
                String thumbSaveName = uploadPath + File.separator + saveFolderPath + File.separator + "s_" + uuid + "_"
                        + fileName;
                // 2. 파일 객체로 생성
                File thumbFile = new File(thumbSaveName);
                // 3. 썸네일 생성 (사이즈는 100, 100)
                Thumbnailator.createThumbnail(savePath.toFile(), thumbFile, 100, 100);
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // 저장된 파일 정보 객체 생성후 리스트에 추가
            uploadResultDtos.add(new UploadResultDto(saveFolderPath, uuid, fileName));
        }

        return new ResponseEntity<>(uploadResultDtos, HttpStatus.OK);
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

    // 이미지 전송하는 메소드
    // <img src="display?fileName= ">
    @GetMapping("/display") // 404 에러가 날경우 컨트롤러 주소를 잘썻는지 확인해보기
    public ResponseEntity<byte[]> getFile(String fileName) {
        ResponseEntity<byte[]> result = null;

        try {
            // 한글꺠짐 처리 + 2024%5C04%5C29% (% 를 / 나 \ 로 처리)
            String srcFileName = URLDecoder.decode(fileName, "utf-8");

            // uploadPath : String fileName 에는 c:\\upload 가 없기때문에 uploadPath 를 사용
            // File.separator : c:\\upload + \\ 를 붙여준다 ("\\" 를 해도되지만 운영체제마다 \\ 를 안쓸수도 있으니
            // 자동으로 처리하기 위함)
            // c:\\upload\\srcFileName
            File file = new File(uploadPath + File.separator + srcFileName);

            // 이미지 파일을 바이트 배열로 처리하기 위한 작업
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", Files.probeContentType(file.toPath()));
            result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return result;
    }

    // 이미지 파일 제거
    @PostMapping("/remove")
    public ResponseEntity<Boolean> postMethodName(String filePath) {
        // filePath :
        // => 2024%5C04%5C29%2F112b09a3-f8b9-4421-bf99-e5e9a8d765f0_intotheworld2.jpg
        log.info("파일 삭제 요청 {}", filePath);

        String srcFileName = null;

        try {
            srcFileName = URLDecoder.decode(filePath, "utf-8");
            File file = new File(uploadPath + File.separator + srcFileName);
            file.delete(); // 원본 파일 제거

            // 썸네일 파일 이름은 s_ 가 붙어서 filePath 를 그대로 사용 못한다
            File thumbFile = new File(file.getParent(), "s_" + file.getName());
            boolean result = thumbFile.delete(); // 썸네일 파일 제거

            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

}
