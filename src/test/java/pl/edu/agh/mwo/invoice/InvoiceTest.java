package pl.edu.agh.mwo.invoice;

import java.math.BigDecimal;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pl.edu.agh.mwo.invoice.Invoice;
import pl.edu.agh.mwo.invoice.product.DairyProduct;
import pl.edu.agh.mwo.invoice.product.OtherProduct;
import pl.edu.agh.mwo.invoice.product.Product;
import pl.edu.agh.mwo.invoice.product.TaxFreeProduct;

public class InvoiceTest {
    private Invoice invoice;

    @Before
    public void createEmptyInvoiceForTheTest() {
        invoice = new Invoice();
    }

    @Test
    public void testEmptyInvoiceHasEmptySubtotal() {
        Assert.assertThat(BigDecimal.ZERO, Matchers.comparesEqualTo(invoice.getNetTotal()));
    }

    @Test
    public void testEmptyInvoiceHasEmptyTaxAmount() {
        Assert.assertThat(BigDecimal.ZERO, Matchers.comparesEqualTo(invoice.getTaxTotal()));
    }

    @Test
    public void testEmptyInvoiceHasEmptyTotal() {
        Assert.assertThat(BigDecimal.ZERO, Matchers.comparesEqualTo(invoice.getGrossTotal()));
    }

    @Test
    public void testInvoiceSubtotalWithTwoDifferentProducts() {
        Product onions = new TaxFreeProduct("Warzywa", new BigDecimal("10"));
        Product apples = new TaxFreeProduct("Owoce", new BigDecimal("10"));
        invoice.addProduct(onions);
        invoice.addProduct(apples);
        Assert.assertThat(new BigDecimal("20"), Matchers.comparesEqualTo(invoice.getNetTotal()));
    }

    @Test
    public void testInvoiceSubtotalWithManySameProducts() {
        Product onions = new TaxFreeProduct("Warzywa", BigDecimal.valueOf(10));
        invoice.addProduct(onions, 100);
        Assert.assertThat(new BigDecimal("1000"), Matchers.comparesEqualTo(invoice.getNetTotal()));
    }

    @Test
    public void testInvoiceHasTheSameSubtotalAndTotalIfTaxIsZero() {
        Product taxFreeProduct = new TaxFreeProduct("Warzywa", new BigDecimal("199.99"));
        invoice.addProduct(taxFreeProduct);
        Assert.assertThat(invoice.getNetTotal(), Matchers.comparesEqualTo(invoice.getGrossTotal()));
    }

    @Test
    public void testInvoiceHasProperSubtotalForManyProducts() {
        invoice.addProduct(new TaxFreeProduct("Owoce", new BigDecimal("200")));
        invoice.addProduct(new DairyProduct("Maslanka", new BigDecimal("100")));
        invoice.addProduct(new OtherProduct("Wino", new BigDecimal("10")));
        Assert.assertThat(new BigDecimal("310"), Matchers.comparesEqualTo(invoice.getNetTotal()));
    }

    @Test
    public void testInvoiceHasProperTaxValueForManyProduct() {
        // tax: 0
        invoice.addProduct(new TaxFreeProduct("Pampersy", new BigDecimal("200")));
        // tax: 8
        invoice.addProduct(new DairyProduct("Kefir", new BigDecimal("100")));
        // tax: 2.30
        invoice.addProduct(new OtherProduct("Piwko", new BigDecimal("10")));
        Assert.assertThat(new BigDecimal("10.30"), Matchers.comparesEqualTo(invoice.getTaxTotal()));
    }

    @Test
    public void testInvoiceHasProperTotalValueForManyProduct() {
        // price with tax: 200
        invoice.addProduct(new TaxFreeProduct("Maskotki", new BigDecimal("200")));
        // price with tax: 108
        invoice.addProduct(new DairyProduct("Maslo", new BigDecimal("100")));
        // price with tax: 12.30
        invoice.addProduct(new OtherProduct("Chipsy", new BigDecimal("10")));
        Assert.assertThat(new BigDecimal("320.30"), Matchers.comparesEqualTo(invoice.getGrossTotal()));
    }

    @Test
    public void testInvoiceHasPropoerSubtotalWithQuantityMoreThanOne() {
        // 2x kubek - price: 10
        invoice.addProduct(new TaxFreeProduct("Kubek", new BigDecimal("5")), 2);
        // 3x kozi serek - price: 30
        invoice.addProduct(new DairyProduct("Kozi Serek", new BigDecimal("10")), 3);
        // 1000x pinezka - price: 10
        invoice.addProduct(new OtherProduct("Pinezka", new BigDecimal("0.01")), 1000);
        Assert.assertThat(new BigDecimal("50"), Matchers.comparesEqualTo(invoice.getNetTotal()));
    }

    @Test
    public void testInvoiceHasPropoerTotalWithQuantityMoreThanOne() {
        // 2x chleb - price with tax: 10
        invoice.addProduct(new TaxFreeProduct("Chleb", new BigDecimal("5")), 2);
        // 3x chedar - price with tax: 32.40
        invoice.addProduct(new DairyProduct("Chedar", new BigDecimal("10")), 3);
        // 1000x pinezka - price with tax: 12.30
        invoice.addProduct(new OtherProduct("Pinezka", new BigDecimal("0.01")), 1000);
        Assert.assertThat(new BigDecimal("54.70"), Matchers.comparesEqualTo(invoice.getGrossTotal()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvoiceWithZeroQuantity() {
        invoice.addProduct(new TaxFreeProduct("Tablet", new BigDecimal("1678")), 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvoiceWithNegativeQuantity() {
        invoice.addProduct(new DairyProduct("Zsiadle mleko", new BigDecimal("5.55")), -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddingNullProduct() {
        invoice.addProduct(null);
    }

    @Test
    public void shouldHaveNumberGreaterThanZero() {
        int invoiceNumber = invoice.getNumber();
        Assert.assertThat(invoiceNumber, Matchers.greaterThan(0));
    }

    @Test
    public void shouldHaveDifferentNumberFromAnotherInvoice() {
        int numberForInvoiceA = new Invoice().getNumber();
        int numberForInvoiceB = new Invoice().getNumber();
        Assert.assertNotEquals(numberForInvoiceA, numberForInvoiceB);
    }

    @Test
    public void shouldHaveConstantNumber() {
        Assert.assertEquals(invoice.getNumber(), invoice.getNumber());
        Assert.assertEquals(invoice.getNumber(), invoice.getNumber());
        Assert.assertEquals(invoice.getNumber(), invoice.getNumber());
        Assert.assertEquals(invoice.getNumber(), invoice.getNumber());
    }

    @Test
    public void shouldHaveIncrementalNumberBasedOnCreationOrder() {
        int numberOf1stInvoice = new Invoice().getNumber();
        int numberOf2ndInvoice = new Invoice().getNumber();
        int numberOf3rdInvoice = new Invoice().getNumber();
        int numberOf4thInvoice = new Invoice().getNumber();

        Assert.assertThat(numberOf1stInvoice, Matchers.lessThan(numberOf2ndInvoice));
        Assert.assertThat(numberOf2ndInvoice, Matchers.lessThan(numberOf3rdInvoice));
        Assert.assertThat(numberOf3rdInvoice, Matchers.lessThan(numberOf4thInvoice));
    }

    @Test
    public void shouldHaveTheLowestNumberEqualsOne() {
        //given
        int amountOfInvoicesCreatedSoFar = Invoice.amountOfInvoicesCreatedSoFar;
        //when
        int numberForFirstInvoice = new Invoice().getNumber();
        //then
        Assert.assertEquals(amountOfInvoicesCreatedSoFar + 1, numberForFirstInvoice);
    }

    @Test
    public void shouldGetEmptySummaryForInvoiceWithoutProducts() {
        //given
        Invoice emptyInvoice = new Invoice();
        int number = emptyInvoice.getNumber();
        //when
        String summary = emptyInvoice.getSummary();
        //then
        Assert.assertEquals("Faktura " + number + "\n\nLiczba pozycji: 0", summary);
    }

    @Test
    public void shouldGetSummaryForInvoiceHavingSingleProduct() {
        //given
        Invoice invoiceWithSingleProduct = new Invoice();
        String productA = "Mleczko";
        double priceA = 12.54;
        int amountA = 1;
        invoiceWithSingleProduct.addProduct(new DairyProduct(productA, BigDecimal.valueOf(priceA)));
        int number = invoiceWithSingleProduct.getNumber();
        int amountOfProducts = amountA;
        String expectedSummary = String.format("Faktura %d\n\t%s x%d x%.2f PLN\nLiczba pozycji: %d",
                number, productA, amountA, priceA, amountOfProducts);

        //when
        String summary = invoiceWithSingleProduct.getSummary();

        //then
        Assert.assertEquals(expectedSummary, summary);
    }

    @Test
    public void shouldGetSummaryForInvoiceHavingSeveralDifferentProducts() {
        //given
        Invoice invoiceWithSeveralProducts = new Invoice();
        String productA = "Mleczko";
        double priceA = 12.54;
        int amountA = 3;
        String productB = "Ser";
        double priceB = 2.34;
        int amountB = 2;
        invoiceWithSeveralProducts.addProduct(new DairyProduct(productB, BigDecimal.valueOf(priceB)), amountB);
        invoiceWithSeveralProducts.addProduct(new DairyProduct(productA, BigDecimal.valueOf(priceA)), amountA);
        int number = invoiceWithSeveralProducts.getNumber();
        int amountOfProducts = amountA + amountB;
        String expectedSummary = String.format("Faktura %d\n\t%s x%d x%.2f PLN\n\t%s x%d x%.2f PLN\nLiczba pozycji: %d",
                number,
                productA, amountA, priceA, productB, amountB, priceB,
                amountOfProducts);
        //when
        String summary = invoiceWithSeveralProducts.getSummary();

        //then
        Assert.assertEquals(expectedSummary, summary);
    }
}
