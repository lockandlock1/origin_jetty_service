package domain.mock;

import com.google.gson.annotations.SerializedName;

public class Message {

    private String role;
    private String content;

    public Message(String role, String content, Integer frequencyPenalty, Integer maxCompletionTokens, Integer presencePenalty, Integer temperature) {
        this.role = role;
        this.content = content;
        this.frequencyPenalty = frequencyPenalty;
        this.maxCompletionTokens = maxCompletionTokens;
        this.presencePenalty = presencePenalty;
        this.temperature = temperature;
    }

    @SerializedName("frequency_penalty")
    private Integer frequencyPenalty;

    public String getRole() {
        return role;
    }

    public String getContent() {
        return content;
    }

    public Integer getFrequencyPenalty() {
        return frequencyPenalty;
    }

    public Integer getMaxCompletionTokens() {
        return maxCompletionTokens;
    }

    public Integer getPresencePenalty() {
        return presencePenalty;
    }

    public Integer getTemperature() {
        return temperature;
    }

    @SerializedName("max_completion_tokens")
    private Integer maxCompletionTokens;

    @SerializedName("presence_penalty")
    private Integer presencePenalty;


    private Integer temperature;
}
