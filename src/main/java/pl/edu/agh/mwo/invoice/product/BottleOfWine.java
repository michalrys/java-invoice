package pl.edu.agh.mwo.invoice.product;

import java.math.BigDecimal;

public class BottleOfWine extends Product {
    private BigDecimal excise;

    public BottleOfWine(String name, BigDecimal price, BigDecimal tax, BigDecimal excise) {
        super(name, price, tax.add(excise.divide(price)));
        this.excise = excise;
    }

    public BigDecimal getExcise() {
        return excise;
    }
}
