package pl.edu.agh.mwo.invoice;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import pl.edu.agh.mwo.invoice.product.Product;

public class Invoice {
    private Map<Product, Integer> products = new HashMap<>();
    private final IdentificationNumber numberStrategy = new IdentificationNumberBySimpleIncrement();
    private final int number = numberStrategy.generate();
    public static final String SUMMARY_HEAD = "Faktura";
    public static final String SUMMARY_TAIL = "Liczba pozycji:";

    public int getTotalAmount() {
        return numberStrategy.getTotalAmount();
    }

    public void addProduct(Product product) {
        addProduct(product, 1);
    }

    public void addProduct(Product product, Integer quantity) {
        if (product == null || quantity <= 0) {
            throw new IllegalArgumentException();
        }
        int currentAmount = products.getOrDefault(product, 0);
        products.put(product, quantity + currentAmount);
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
        StringBuilder summary = new StringBuilder();
        summary.append(SUMMARY_HEAD + " " + number + "\n");

        int amountOfProducts = 0;

        List<Product> sortedProductsByName = products.keySet().stream()
                .sorted(Comparator.comparing(Product::getName))
                .collect(Collectors.toList());

        for (Product product : sortedProductsByName) {
            amountOfProducts += products.get(product);
            summary.append("\t");
            summary.append(product.getName());
            summary.append(" x");
            summary.append(products.get(product));
            summary.append(" x");
            summary.append(String.format("%.2f", product.getPrice().doubleValue()));
            summary.append(" PLN\n");
        }
        summary.append(SUMMARY_TAIL);
        summary.append(" ");
        summary.append(amountOfProducts);
        return summary.toString();
    }
}