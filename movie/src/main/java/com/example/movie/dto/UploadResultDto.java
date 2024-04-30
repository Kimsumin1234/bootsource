package com.example.movie.dto;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import lombok.AllArgsConstructor;
import lombok.Data;

// Serializable : 객체 상태로 입출력

@AllArgsConstructor
@Data
public class UploadResultDto implements Serializable {
    // 폴더, uuid, 실제 파일명
    // C:\\upload\2024\04\29\7cb98bf0-f43b-49f5-a424-c20d4e58817d_inception2
    // 파일이 저장된 경로를 나눠서 변수에 담는다
    private String folderPath; // C:\\upload\2024\04\29\
    private String uuid; // 7cb98bf0-f43b-49f5-a424-c20d4e58817d
    private String fileName; // inception2

    // 저장된 파일의 위치(주소) 얻는 메소드 (imageURL)
    // @Getter 가 있기때문에 .imageURL 로 메소드를 호출할수있다
    // {folderPath: '2024\\04\\29', uuid: '4dec8815-6268-4791-b6f8-c52a9e069f19',
    // fileName: 'war3.jpg',
    // imageURL: '2024%5C04%5C29%2F4dec8815-6268-4791-b6f8-c52a9e069f19_war3.jpg'}
    public String getImageURL() {
        String fullPath = "";

        try {
            // 한글 안깨지게 인코딩 작업을 해준다
            fullPath = URLEncoder.encode(folderPath + "/" + uuid + "_" + fileName, "UTF-8");
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
            fullPath = URLEncoder.encode(folderPath + "/s_" + uuid + "_" + fileName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return fullPath;
    }

}
