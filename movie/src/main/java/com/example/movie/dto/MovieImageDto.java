package com.example.movie.dto;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Setter
@Getter
public class MovieImageDto {
    private Long inum;

    private String uuid;

    private String imgName;

    private String path;

    private LocalDateTime createdDate;

    private LocalDateTime lastModifiedDate;

    // Movie 관계 설정

    // 저장된 파일의 위치(주소) 얻는 메소드 (imageURL)
    public String getImageURL() {
        String fullPath = "";

        try {
            // 한글 안깨지게 인코딩 작업을 해준다
            fullPath = URLEncoder.encode(path + "/" + uuid + "_" + imgName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return fullPath;
    }

    // 썸네일 (s_ 를 추가)
    public String getThumbImageURL() {
        String fullPath = "";

        try {
            // 한글 안깨지게 인코딩 작업을 해준다
            fullPath = URLEncoder.encode(path + "/s_" + uuid + "_" + imgName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return fullPath;
    }
}
