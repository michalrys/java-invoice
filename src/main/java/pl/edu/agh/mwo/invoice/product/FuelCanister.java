package pl.edu.agh.mwo.invoice.product;

import java.math.BigDecimal;
import java.time.LocalDate;

public class FuelCanister extends Product {
    private final BigDecimal tax;
    private final LocalDate promoDay;
    private BigDecimal excise;

    public FuelCanister(String name, BigDecimal price, BigDecimal tax, BigDecimal excise, LocalDate promoDay) {
        super(name, price, shallBeWithoutExcise(name) && isPromoDay(promoDay) ? tax : tax.add(excise.divide(price)));
        this.tax = tax;
        this.promoDay = promoDay;

        this.excise = shallBeWithoutExcise(name) && isPromoDay(promoDay) ? BigDecimal.ZERO : excise;
    }

    public BigDecimal getExcise() {
        return excise;
    }

    private static boolean isPromoDay(LocalDate promoDay) {
        return promoDay.isEqual(LocalDate.now());
    }

    private static boolean shallBeWithoutExcise(String name) {
        return name.toLowerCase().matches("(.*liquid.*fuel.*)|(.*fuel.*liquid.*)");
    }
}
