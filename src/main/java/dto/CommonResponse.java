package dto;

import com.google.gson.annotations.SerializedName;

public class CommonResponse<T> {
    private String code;
    private String message;
    private T data;

    public CommonResponse(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public String getCode() { return code; }
    public String getMessage() { return message; }
    public T getData() { return data; }

    public static <T> CommonResponse<T> success(T data) {
        return new CommonResponse<>("OK", "success", data);
    }

    public static <T> CommonResponse<T> error(String code, String message) {
        return new CommonResponse<>(code, message, null);
    }
}