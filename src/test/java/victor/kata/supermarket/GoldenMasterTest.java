package victor.kata.supermarket;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;

public class GoldenMasterTest {
    public static final Product TOOTHBRUSH = new Product("toothbrush", ProductUnit.EACH);
    public static final Product BREAD = new Product("bread", ProductUnit.EACH);
    public static final Product APPLES = new Product("apples", ProductUnit.KILO);
    public static final Product TOMATOES = new Product("tomatoes", ProductUnit.KILO);
    public static final File FOLDER = new File("src/test/resources/captured-output");

    private static final Product[] PRODUCTS = {TOOTHBRUSH, BREAD, APPLES, TOMATOES};

    public static Stream<Integer> seeds() {
        return IntStream.range(0,1000).boxed();
    }

    @BeforeAll
    public static void checkForCapturedOutput() throws IOException {
        if (!FOLDER.isDirectory()) {
            System.out.println("Created: " + FOLDER.mkdirs());
        }
        if (FOLDER.list().length == 0) {
            System.err.println("NO CAPTURED OUTPUT YET: capturing ...");
            for (Integer seed : seeds().collect(toList())) {
                String output = runForSeed(seed);
                FileUtils.write(getFileForSeed(seed), output, StandardCharsets.UTF_8);
            }
            System.err.println("DONE");
        } else {
            System.err.println("CAPTURED OUTPUT FOUND IN " + FOLDER.getAbsolutePath());
            System.err.println("IF YOU WANT TO RE-RECORD CURRENT BEHAVIOR, DELETE THIS FOLDER AND RERUN THIS TEST");
        }
    }

    private static File getFileForSeed(Integer seed) {
        return new File(FOLDER, "seed-" + seed + ".txt");
    }

    @ParameterizedTest(name = "seed-{0}.txt")
    @MethodSource("seeds")
    void testSeedVersusFile(int seed) throws IOException {
        String actualOutput = runForSeed(seed);

        File file = getFileForSeed(seed);
        String expectedOutput = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
        assertThat(actualOutput).isEqualTo(expectedOutput);
    }

//    public static void main(String[] args) {
//        runForSeed(new Random().nextInt());
//    }

    public static String runForSeed(int seed) {
        Catalog catalogMock = mock(Catalog.class);
        Teller teller = new Teller(catalogMock);
        lenient().when(catalogMock.getUnitPrice(TOOTHBRUSH)).thenReturn(10.0);
        lenient().when(catalogMock.getUnitPrice(BREAD)).thenReturn(4.0);
        lenient().when(catalogMock.getUnitPrice(APPLES)).thenReturn(2.0);
        lenient().when(catalogMock.getUnitPrice(TOMATOES)).thenReturn(3.0);

        Random r = new Random(seed);
        ShoppingCart cart = new ShoppingCart();

        for (Product product : PRODUCTS) {
            if (r.nextBoolean()) {
                SpecialOfferType randomOffer = SpecialOfferType.values()[r.nextInt(SpecialOfferType.values().length)];
                teller.addSpecialOffer(randomOffer, product, 5);
            }
        }

        for (int i = 0; i < r.nextInt(11); i++) {
            switch (r.nextInt(4)) {
                case 0: cart.addItemQuantity(APPLES, r.nextDouble()*5); break;
                case 1: cart.addItemQuantity(TOMATOES, 1+ r.nextDouble()*5); break;
                case 2: cart.addItemQuantity(TOOTHBRUSH, r.nextInt(3)); break;
                case 3: cart.addItemQuantity(BREAD, 2+ r.nextInt(4)); break;
            }
        }

        Receipt receipt = teller.checksOutArticlesFrom(cart);

        ReceiptPrinter printer = new ReceiptPrinter();
        return printer.printReceipt(receipt);
    }

}
