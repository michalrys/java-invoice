package pl.edu.agh.mwo.invoice;

public class IdentificationNumberBySimpleIncrement implements IdentificationNumber {
    private static int amount = 0;

    @Override
    public int generate() {
        return ++amount;
    }

    @Override
    public int getTotalAmount() {
        return amount;
    }
}
