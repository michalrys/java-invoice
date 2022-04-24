package pl.edu.agh.mwo.invoice;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import pl.edu.agh.mwo.invoice.product.Product;

public class Invoice {
    private Map<Product, Integer> products = new HashMap<Product, Integer>();
    public static int amountOfInvoicesCreatedSoFar = -1;
    private final int number = ++amountOfInvoicesCreatedSoFar;
    public static final String SUMMARY_HEAD = "Faktura";
    public static final String SUMMARY_TAIL = "Liczba pozycji:";

    public void addProduct(Product product) {
        addProduct(product, 1);
    }

    public void addProduct(Product product, Integer quantity) {
        if (product == null || quantity <= 0) {
            throw new IllegalArgumentException();
        }
        products.put(product, quantity);
    }

    public BigDecimal getNetTotal() {
        BigDecimal totalNet = BigDecimal.ZERO;
        for (Product product : products.keySet()) {
            BigDecimal quantity = new BigDecimal(products.get(product));
            totalNet = totalNet.add(product.getPrice().multiply(quantity));
        }
        return totalNet;
    }

    public BigDecimal getTaxTotal() {
        return getGrossTotal().subtract(getNetTotal());
    }

    public BigDecimal getGrossTotal() {
        BigDecimal totalGross = BigDecimal.ZERO;
        for (Product product : products.keySet()) {
            BigDecimal quantity = new BigDecimal(products.get(product));
            totalGross = totalGross.add(product.getPriceWithTax().multiply(quantity));
        }
        return totalGross;
    }

    public int getNumber() {
        return number;
    }

    public String getSummary() {
        String productName = "";
        double productPrice = 0.0;
        int productAmount = 0;
        int amountOfProducts = 0;
        for (Product product : products.keySet()) {
            productName = product.getName();
            productPrice = product.getPrice().doubleValue();
            productAmount = products.get(product);
            amountOfProducts += productAmount;
        }

        return String.format("%s %d\n\t%s x%d x%.2f PLN\n%s %d",
                SUMMARY_HEAD, number, productName, productAmount, productPrice, SUMMARY_TAIL, amountOfProducts);
    }
}
