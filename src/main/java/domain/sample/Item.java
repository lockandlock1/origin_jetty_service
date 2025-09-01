package domain.sample;

public class Item {
    public final int id;
    public final String productName;
    public final int qty;

    public Item(int id, String productName, int qty) {
        this.id = id;
        this.productName = productName;
        this.qty = qty;
    }

}
