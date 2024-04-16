package org.zerock.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovieImageDTO { // UploadResultDTO와 동일

    private String uuid;
    private String imgName;
    private String path;

    public String getImageURL() { // 추후 타임리프로 출력해 사용할 예정
        try {
            return URLEncoder.encode(path+"/"+uuid+"_"+imgName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getThumbnailURL(){ // 추후 타임리프로 출력해 사용할 예정
        try{
            return URLEncoder.encode(path+"/s_"+uuid+"_"+imgName, "UTF-8");
        } catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
        return "";
    }
}
