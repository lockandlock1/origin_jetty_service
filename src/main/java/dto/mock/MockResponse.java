package dto.mock;

import java.util.List;

public class MockResponse {

    private List<Message> messages;

    private Boolean stream;

    public List<Message> getMessages() {
        return messages;
    }

    public Boolean getStream() {
        return stream;
    }
}
