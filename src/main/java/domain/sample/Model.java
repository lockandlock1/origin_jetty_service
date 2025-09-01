package domain.sample;

import java.time.LocalDateTime;

public class Model {
    public final Integer id;
    public final String name;
    public final LocalDateTime createdAt;

    public Model(Integer id, String name, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
    }
}
