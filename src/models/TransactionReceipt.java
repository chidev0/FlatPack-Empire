package models;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TransactionReceipt {
List<Product> transactionLog = new ArrayList<>();
BigDecimal total;

public void addProduct(Product p) {
    transactionLog.add(p);
}

public BigDecimal calculateTotal() {
    for (Product p : transactionLog) {
        total = total.add(p.getPrice());
    }
    return total;
}

}
