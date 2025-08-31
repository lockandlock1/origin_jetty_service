package dto;

import com.google.gson.annotations.SerializedName;

public class Meta {

    @SerializedName("total_count")
    private Integer totalCount;

    @SerializedName("pageable_count")
    private Integer pageableCount;


    @SerializedName("is_end")
    private Boolean isEnd;

}
