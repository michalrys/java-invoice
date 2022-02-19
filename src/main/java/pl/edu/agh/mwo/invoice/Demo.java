package pl.edu.agh.mwo.invoice;

import java.math.BigDecimal;

public class Demo {
    public static void main(String[] args) {
//        System.out.println(0.1 + 0.2);
//
//        BigDecimal results = BigDecimal.valueOf(0.1).add(BigDecimal.valueOf(0.2));
//        System.out.println(results);

        System.out.println("" == "");
        System.out.println(new Integer(300) == new Integer(300));
        System.out.println(new String("") ==  new String(""));

        // java szuka wystąpienia wszędzie pustego stringa

        System.out.println("a" == "a");
        System.out.println("ab" == "ab");

    }
}
