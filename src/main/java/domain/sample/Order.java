package domain.sample;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Order {
    public final int id;
    public final LocalDateTime createdAt;
    public final List<Item> items = new ArrayList<>();

    public Order(int id, LocalDateTime createdAt) {
        this.id = id;
        this.createdAt = createdAt;
    }
}
