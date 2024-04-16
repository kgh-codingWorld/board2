package org.zerock.board.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Data
@AllArgsConstructor
public class UploadResultDTO implements Serializable { // json으로 전달됨

    private String fileName;
    private String uuid;
    private String folderPath;
    public String getImageURL(){ // 실제 파일과 관련된 모든 정보를 가지는데 나중에 전체 경로가 필요한 경우를 대비해 제공됨
        try{
            return URLEncoder.encode(folderPath+"/"+uuid+"_"+fileName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getThumbnailURL() { // 브라우저에서 섬네일 처리
        try {
            return URLEncoder.encode(folderPath+"/s_"+uuid+"_"+fileName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
}
