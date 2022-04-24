package pl.edu.agh.mwo.invoice.product;

import java.math.BigDecimal;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import pl.edu.agh.mwo.invoice.product.Product;

public class ProductTest {
    @Test
    public void testProductNameIsCorrect() {
        Product product = new OtherProduct("buty", new BigDecimal("100.0"));
        Assert.assertEquals("buty", product.getName());
    }

    @Test
    public void testProductPriceAndTaxWithDefaultTax() {
        Product product = new OtherProduct("Ogorki", new BigDecimal("100.0"));
        Assert.assertThat(new BigDecimal("100"), Matchers.comparesEqualTo(product.getPrice()));
        Assert.assertThat(new BigDecimal("0.23"), Matchers.comparesEqualTo(product.getTaxPercent()));
    }

    @Test
    public void testProductPriceAndTaxWithDairyProduct() {
        Product product = new DairyProduct("Szarlotka", new BigDecimal("100.0"));
        Assert.assertThat(new BigDecimal("100"), Matchers.comparesEqualTo(product.getPrice()));
        Assert.assertThat(new BigDecimal("0.08"), Matchers.comparesEqualTo(product.getTaxPercent()));
    }

    @Test
    public void testPriceWithTax() {
        Product product = new DairyProduct("Oscypek", new BigDecimal("100.0"));
        Assert.assertThat(new BigDecimal("108"), Matchers.comparesEqualTo(product.getPriceWithTax()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testProductWithNullName() {
        new OtherProduct(null, new BigDecimal("100.0"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testProductWithEmptyName() {
        new TaxFreeProduct("", new BigDecimal("100.0"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testProductWithNullPrice() {
        new DairyProduct("Banany", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testProductWithNegativePrice() {
        new TaxFreeProduct("Mandarynki", new BigDecimal("-1.00"));
    }

    @Test
    public void shouldIncludeExciseForWineProducts() {
        BigDecimal price = new BigDecimal("100");
        BigDecimal tax = new BigDecimal(0.10);
        BigDecimal excise = new BigDecimal(5.56);
        BigDecimal totalTax = tax.add(excise.divide(price));
        BigDecimal expectedPriceWithTax = price.multiply(totalTax).add(price);

        Product product = new BottleOfWine("Owocowe", price, tax, excise);
        Assert.assertThat(price, Matchers.comparesEqualTo(product.getPrice()));
        Assert.assertThat(expectedPriceWithTax, Matchers.comparesEqualTo(product.getPriceWithTax()));
        Assert.assertThat(totalTax, Matchers.comparesEqualTo(product.getTaxPercent()));
        Assert.assertThat(excise, Matchers.comparesEqualTo(((BottleOfWine) product).getExcise()));
    }

    @Test
    public void shouldIncludeExciseForFuelCanister() {
        BigDecimal price = new BigDecimal("100");
        BigDecimal tax = new BigDecimal(0.20);
        BigDecimal excise = new BigDecimal(5.56);
        BigDecimal totalTax = tax.add(excise.divide(price));
        BigDecimal expectedPriceWithTax = price.multiply(totalTax).add(price);

        Product product = new FuelCanister("NoPb95", price, tax, excise);
        Assert.assertThat(price, Matchers.comparesEqualTo(product.getPrice()));
        Assert.assertThat(expectedPriceWithTax, Matchers.comparesEqualTo(product.getPriceWithTax()));
        Assert.assertThat(totalTax, Matchers.comparesEqualTo(product.getTaxPercent()));
        Assert.assertThat(excise, Matchers.comparesEqualTo(((FuelCanister) product).getExcise()));
    }
}
