package victor.kata.supermarket;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
public class SupermarketTestDONE { // carefully crafted with pitest checking to cover all lines with mutation testing.
    public static final Product TOOTHBRUSH = new Product("toothbrush", ProductUnit.EACH);
    public static final Product APPLES = new Product("apples", ProductUnit.KILO);
    @Mock
    Catalog catalog;
    @InjectMocks
    Teller teller;

    @BeforeEach
    final void before() {
        lenient().when(catalog.getUnitPrice(TOOTHBRUSH)).thenReturn(10.0);
        lenient().when(catalog.getUnitPrice(APPLES)).thenReturn(2.0);
    }

    @Test
    void sumsUpItems() {
        ShoppingCart cart = new ShoppingCart();
        cart.addItem(TOOTHBRUSH);
        cart.addItemQuantity(APPLES, 1);
        cart.addItemQuantity(APPLES, 1);
        Receipt receipt = teller.checksOutArticlesFrom(cart);

        assertThat(receipt.getTotalPrice()).isEqualTo(14);
        System.out.println(receipt.getItems());
    }

    @Test
    void tenPercentDiscount() {
        teller.addSpecialOffer(SpecialOfferType.TenPercentDiscount, TOOTHBRUSH, 10.0);

        ShoppingCart cart = new ShoppingCart();
        cart.addItemQuantity(TOOTHBRUSH, 1);
        Receipt receipt = teller.checksOutArticlesFrom(cart);

        assertThat(receipt.getTotalPrice()).isEqualTo(9);
        assertThat(receipt.getDiscounts()).hasSize(1);
    }

    @Test
    void threeForTwo_NA() {
        teller.addSpecialOffer(SpecialOfferType.ThreeForTwo, TOOTHBRUSH, /*not used*/-1);

        ShoppingCart cart = new ShoppingCart();
        cart.addItemQuantity(TOOTHBRUSH, 2);
        Receipt receipt = teller.checksOutArticlesFrom(cart);

        assertThat(receipt.getTotalPrice()).isEqualTo(20);
        assertThat(receipt.getDiscounts()).hasSize(0);
    }
    @Test
    void threeForTwo() {
        teller.addSpecialOffer(SpecialOfferType.ThreeForTwo, TOOTHBRUSH, /*not used*/-1);

        ShoppingCart cart = new ShoppingCart();
        cart.addItemQuantity(TOOTHBRUSH, 3);
        Receipt receipt = teller.checksOutArticlesFrom(cart);

        assertThat(receipt.getTotalPrice()).isEqualTo(20);
        assertThat(receipt.getDiscounts()).hasSize(1);
    }
    @Test
    void threeForTwo_oneExtra() {
        teller.addSpecialOffer(SpecialOfferType.ThreeForTwo, TOOTHBRUSH, /*not used*/-1);

        ShoppingCart cart = new ShoppingCart();
        cart.addItemQuantity(TOOTHBRUSH, 2); // also tests " + quantity" part
        cart.addItemQuantity(TOOTHBRUSH, 2);
        Receipt receipt = teller.checksOutArticlesFrom(cart);

        assertThat(receipt.getTotalPrice()).isEqualTo(30);
        assertThat(receipt.getDiscounts()).hasSize(1);
    }
    @Test
    void twoForAmount() {
        teller.addSpecialOffer(SpecialOfferType.TwoForAmount, TOOTHBRUSH, 15);

        ShoppingCart cart = new ShoppingCart();
        cart.addItemQuantity(TOOTHBRUSH, 2);
        Receipt receipt = teller.checksOutArticlesFrom(cart);

        assertThat(receipt.getTotalPrice()).isEqualTo(15);
        assertThat(receipt.getDiscounts()).hasSize(1);
    }
    @Test
    void twoForAmount_plusMore() {
        teller.addSpecialOffer(SpecialOfferType.TwoForAmount, TOOTHBRUSH, 15);

        ShoppingCart cart = new ShoppingCart();
        cart.addItemQuantity(TOOTHBRUSH, 3);
        Receipt receipt = teller.checksOutArticlesFrom(cart);

        assertThat(receipt.getTotalPrice()).isEqualTo(25);
        assertThat(receipt.getDiscounts()).hasSize(1);
    }
    @Test
    void twoForAmount_twice() {
        teller.addSpecialOffer(SpecialOfferType.TwoForAmount, TOOTHBRUSH, 15);

        ShoppingCart cart = new ShoppingCart();
        cart.addItemQuantity(TOOTHBRUSH, 4);
        Receipt receipt = teller.checksOutArticlesFrom(cart);

        assertThat(receipt.getTotalPrice()).isEqualTo(30);
        assertThat(receipt.getDiscounts()).hasSize(1);
    }
    @Test
    void fiveForAmount() {
        teller.addSpecialOffer(SpecialOfferType.FiveForAmount, APPLES, 7);

        ShoppingCart cart = new ShoppingCart();
        cart.addItemQuantity(APPLES, 5);
        Receipt receipt = teller.checksOutArticlesFrom(cart);

        assertThat(receipt.getTotalPrice()).isEqualTo(7);
        assertThat(receipt.getDiscounts()).hasSize(1);
    }
    @Test
    void fiveForAmount_plusExtra() {
        teller.addSpecialOffer(SpecialOfferType.FiveForAmount, APPLES, 7);

        ShoppingCart cart = new ShoppingCart();
        cart.addItemQuantity(APPLES, 6);
        Receipt receipt = teller.checksOutArticlesFrom(cart);

        assertThat(receipt.getTotalPrice()).isEqualTo(9);
        assertThat(receipt.getDiscounts()).hasSize(1);
    }
//    @Test
//    void tenPercentDiscount() {
//        when(catalog.getUnitPrice(TOOTHBRUSH)).thenReturn(10.0);
//        when(catalog.getUnitPrice(APPLES)).thenReturn(2.0);
//
//        teller.addSpecialOffer(SpecialOfferType.TenPercentDiscount, TOOTHBRUSH, 10.0);
//        ShoppingCart cart = new ShoppingCart();
//        cart.addItemQuantity(APPLES, 2.5);
//        Receipt receipt = teller.checksOutArticlesFrom(cart);
//
//        assertEquals(5, receipt.getTotalPrice(), 0.01);
//        assertEquals(Collections.emptyList(), receipt.getDiscounts());
//        assertEquals(1, receipt.getItems().size());
//        ReceiptItem receiptItem = receipt.getItems().get(0);
//        assertEquals(APPLES, receiptItem.getProduct());
//        assertEquals(2.0, receiptItem.getPrice());
//        assertEquals(2.5*1.99, receiptItem.getTotalPrice());
//        assertEquals(2.5, receiptItem.getQuantity());
//    }


}
