package pl.edu.agh.mwo.invoice.product;

import java.math.BigDecimal;

public class FuelCanister extends Product {
    private BigDecimal excise;

    public FuelCanister(String name, BigDecimal price, BigDecimal tax, BigDecimal excise) {
        super(name, price, tax.add(excise.divide(price)));
        this.excise = excise;
    }

    public BigDecimal getExcise() {
        return excise;
    }
}
