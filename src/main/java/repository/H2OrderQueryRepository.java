package repository;

import domain.sample.Item;
import domain.sample.Model;
import domain.sample.Order;
import domain.sample.OrderSummary;
import infra.db.Db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class H2OrderQueryRepository implements OrderQueryRepository {



    public int insert(String name) throws Exception {
        String sql = "INSERT INTO model(name) VALUES(?)";
        Connection c = Db.get();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, name);
            ps.executeUpdate();
            rs = ps.getGeneratedKeys();
            return rs.next() ? rs.getInt(1) : -1;
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            c.close();
        }
    }

    public Model findById(int id) throws Exception {
        String sql = "SELECT id, name, created_at FROM model WHERE id=?";
        Connection c = Db.get();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = c.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (!rs.next()) {
                return null;
            }
            Integer mid = rs.getInt("id");
            String name = rs.getString("name");
            java.time.LocalDateTime createdAt = rs.getObject("created_at", java.time.LocalDateTime.class);
            return new Model(mid, name, createdAt);
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            c.close();
        }
    }

    public List<Model> list(String q, int page, int size) throws Exception {
        String sql = "SELECT id, name, created_at FROM model " +
                "WHERE (? IS NULL OR name LIKE ?) " +
                "ORDER BY id DESC LIMIT ? OFFSET ?";
        Connection c = Db.get();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = c.prepareStatement(sql);
            ps.setObject(1, q == null ? null : q);
            ps.setObject(2, q == null ? null : "%" + q + "%");
            ps.setInt(3, size);
            ps.setInt(4, page * size);
            rs = ps.executeQuery();

            List<Model> out = new ArrayList<Model>();
            while (rs.next()) {
                out.add(new Model(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getObject("created_at", LocalDateTime.class)
                ));
            }
            return out;
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            c.close();
        }
    }

    public boolean updateName(int id, String newName) throws Exception {
        String sql = "UPDATE model SET name=? WHERE id=?";
        Connection c = Db.get();
        PreparedStatement ps = null;
        try {
            ps = c.prepareStatement(sql);
            ps.setString(1, newName);
            ps.setInt(2, id);
            return ps.executeUpdate() == 1;
        } finally {
            if (ps != null) ps.close();
            c.close();
        }
    }



    @Override
    public List<OrderSummary> listSummaries(String nameFilter, int page, int size) throws Exception {
        String sql =
                "SELECT o.id AS oid, c.name AS cname, o.created_at " +
                        "FROM orders o JOIN customer c ON c.id = o.customer_id " +
                        "WHERE (? IS NULL OR c.name LIKE ?) " +
                        "ORDER BY o.id DESC " +
                        "LIMIT ? OFFSET ?";

        Connection c = Db.get();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = c.prepareStatement(sql);
            ps.setObject(1, nameFilter == null ? null : nameFilter);
            ps.setObject(2, nameFilter == null ? null : "%" + nameFilter + "%");
            ps.setInt(3, size);
            ps.setInt(4, page * size);

            rs = ps.executeQuery();

            List<OrderSummary> out = new ArrayList<OrderSummary>();
            while (rs.next()) {
                int orderId = rs.getInt("oid");
                String customerName = rs.getString("cname");
                LocalDateTime createdAt = rs.getObject("created_at", LocalDateTime.class);
                out.add(new OrderSummary(orderId, customerName, createdAt));
            }
            return out;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            c.close();
        }
    }

    @Override
    public List<Order> findWithItems(int fromId, int toId) throws Exception {
        String sql =
                "SELECT o.id AS oid, o.created_at, i.id AS iid, i.product_name, i.qty " +
                        "FROM orders o " +
                        "LEFT JOIN order_item i ON i.order_id = o.id " +
                        "WHERE o.id BETWEEN ? AND ? " +
                        "ORDER BY o.id, i.id";

        Connection c = Db.get();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = c.prepareStatement(sql);
            ps.setInt(1, fromId);
            ps.setInt(2, toId);

            rs = ps.executeQuery();

            LinkedHashMap<Integer, Order> map = new LinkedHashMap<Integer, Order>();
            while (rs.next()) {
                int oid = rs.getInt("oid");
                Order ord = map.get(oid);
                if (ord == null) {
                    LocalDateTime createdAt = rs.getObject("created_at", LocalDateTime.class);
                    ord = new Order(oid, createdAt);
                    map.put(oid, ord);
                }

                int iid = rs.getInt("iid");
                if (!rs.wasNull()) {
                    String productName = rs.getString("product_name");
                    int qty = rs.getInt("qty");
                    ord.items.add(new Item(iid, productName, qty));
                }
            }
            return new ArrayList<Order>(map.values());
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            c.close();
        }
    }

    @Override
    public List<OrderSummary> listRecentByView(int page, int size) throws Exception {
        String sql =
                "SELECT s.order_id, s.customer_name, s.created_at " +
                        "FROM v_order_summary s " +
                        "JOIN v_recent_orders r ON r.id = s.order_id " +
                        "ORDER BY s.order_id DESC " +
                        "LIMIT ? OFFSET ?";

        Connection c = Db.get();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = c.prepareStatement(sql);
            ps.setInt(1, size);
            ps.setInt(2, page * size);

            rs = ps.executeQuery();

            List<OrderSummary> out = new ArrayList<OrderSummary>();
            while (rs.next()) {
                int orderId = rs.getInt("order_id");
                String customerName = rs.getString("customer_name");
                LocalDateTime createdAt = rs.getObject("created_at", LocalDateTime.class);
                out.add(new OrderSummary(orderId, customerName, createdAt));
            }
            return out;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            c.close();
        }
    }

}
