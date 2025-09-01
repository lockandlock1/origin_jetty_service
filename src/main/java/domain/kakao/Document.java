package domain.kakao;


import com.google.gson.annotations.SerializedName;

import java.time.LocalDateTime;

public class Document {

    private String title;
    private String contents;
    private String url;
    @SerializedName("blogname")
    private String blogName;
    private LocalDateTime datetime;
}
