package repository;

import domain.sample.Order;
import domain.sample.OrderSummary;

import java.util.List;

public interface OrderQueryRepository {
    List<OrderSummary> listSummaries(String customerNameFilter, int page, int size) throws Exception; // 선택지 1

    List<Order> findWithItems(int fromId, int toId) throws Exception;                                  // 선택지 2

    List<OrderSummary> listRecentByView(int page, int size) throws Exception;
}
