package domain.sample;

import java.time.LocalDateTime;

public class OrderSummary {
    public final int orderId;
    public final String customerName;
    public final LocalDateTime createdAt;

    public OrderSummary(int orderId, String customerName, LocalDateTime createdAt) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.createdAt = createdAt;
    }
}
