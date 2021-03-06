package com.kata;

import com.kata.inventory.Item;
import com.kata.inventory.SimpleItem;
import com.kata.pricing.PricingTerminal;
import com.kata.strategies.PricingStrategy;
import com.kata.strategies.QuantityPromoPricingStrategy;
import com.kata.strategies.WeightedPricingStrategy;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * Created by crist on 5/16/2017.
 */
public class BasketTest {

    private Basket basket;
    private PricingTerminal pricingTerminal;
    private static final String YOGURT = "Yogurt";
    private static final String CHEESE = "Cheese";
    private static final String MILK = "Milk";
    private static final String SANDWICH = "Sandwich";

    @Before
    public void setUp() throws Exception {
        pricingTerminal = new PricingTerminal();
        basket = new Basket(pricingTerminal);
    }

    @Test
    public void testEmptyBasket(){
        assertEquals("Nothing in basket",0.0, basket.getSum(),0);
    }

    @Test
    public void testOneSimpleItemInBasketNoStrategy(){
        Item yogurt = new SimpleItem(YOGURT, 5.0);

        basket.addItem(yogurt);

        assertEquals("One item, no strategy",5.0, basket.getSum(),0);
    }

    @Test
    public void testTwoSimpleSameItemsInBasketNoStrategy(){
        Item item1 = new SimpleItem(YOGURT, 5.0);
        Item item2 = new SimpleItem(YOGURT,5.0);

        basket.addItems(Arrays.asList(item1,item2));

        assertEquals("Two same items, no strategy",10.0, basket.getSum(),0);
    }

    @Test
    public void testTwoSimpleItemsDifferentTypeNoStrategy(){
        Item item1 = new SimpleItem(YOGURT, 5.0);
        Item item2 = new SimpleItem(CHEESE,3.0);

        basket.addItems(Arrays.asList(item1,item2));

        assertEquals("Two different items, no strategy",8.0, basket.getSum(),0);
    }

    @Test
    public void testMultipleItemsDifferentTypeNoStrategy(){
        Item item1 = new SimpleItem(YOGURT, 5.0);
        Item item2 = new SimpleItem(YOGURT, 5.0);
        Item item3 = new SimpleItem(CHEESE,3.0);
        Item item4 = new SimpleItem(CHEESE,3.0);
        Item item5 = new SimpleItem(MILK,1.0);

        basket.addItems(Arrays.asList(item1,item2,item3,item4,item5));

        assertEquals("Multiple different items, no strategy",17.0, basket.getSum(),0);
    }

    @Test
    public void testOneItemWithThreeForOnePromo(){
        Item yogurt = new SimpleItem(YOGURT, 5.0);

        PricingStrategy quantityPromoPricingStrategy = new QuantityPromoPricingStrategy(3,2);

        pricingTerminal.addPricingStrategy(quantityPromoPricingStrategy);
        pricingTerminal.addPromotion(quantityPromoPricingStrategy.getClass(),YOGURT);

        basket.addItem(yogurt);

        assertEquals("One item, no strategy",5.0, basket.getSum(),0);
    }

    @Test
    public void testTwoItemsOneOnTwoForOnePromo(){
        Item item1 = new SimpleItem(YOGURT, 5.0);
        Item item2 = new SimpleItem(YOGURT, 5.0);
        Item item3 = new SimpleItem(CHEESE,3.0);

        PricingStrategy quantityPromoPricingStrategy = new QuantityPromoPricingStrategy(2,1);

        pricingTerminal.addPricingStrategy(quantityPromoPricingStrategy);
        pricingTerminal.addPromotion(quantityPromoPricingStrategy.getClass(),YOGURT);

        basket.addItems(Arrays.asList(item1,item2,item3));

        assertEquals("One item on 2 for 1 promo, other not", 8.0,basket.getSum(),0);
    }

    @Test
    public void testTwoItemsOnTwoDifferentPromoOneApplied(){
        Item item = new SimpleItem(YOGURT,5.0,0.7);
        Item item2 = new SimpleItem(CHEESE,5.0);

        PricingStrategy weightedPricingStrategy = new WeightedPricingStrategy();
        PricingStrategy quantityPromoPricingStrategy = new QuantityPromoPricingStrategy(2,1);

        pricingTerminal.addPricingStrategy(weightedPricingStrategy);
        pricingTerminal.addPricingStrategy(quantityPromoPricingStrategy);
        pricingTerminal.addPromotion(weightedPricingStrategy.getClass(),YOGURT);
        pricingTerminal.addPromotion(quantityPromoPricingStrategy.getClass(),CHEESE);

        basket.addItems(Arrays.asList(item,item2));

        assertEquals("Cheese 2 for 1 not applied, Yogurt 0.7 kg @ 5 $/kg",8.5,basket.getSum(),0);
    }

    @Test
    public void testTwoItemsOnTwoDifferentPromoBothApplied(){
        Item item = new SimpleItem(YOGURT,5.0,0.7);
        Item item2 = new SimpleItem(CHEESE,5.0);
        Item item3 = new SimpleItem(CHEESE,5.0);

        PricingStrategy weightedPricingStrategy = new WeightedPricingStrategy();
        PricingStrategy quantityPromoPricingStrategy = new QuantityPromoPricingStrategy(2,1);

        pricingTerminal.addPricingStrategy(weightedPricingStrategy);
        pricingTerminal.addPricingStrategy(quantityPromoPricingStrategy);
        pricingTerminal.addPromotion(weightedPricingStrategy.getClass(),YOGURT);
        pricingTerminal.addPromotion(quantityPromoPricingStrategy.getClass(),CHEESE);

        basket.addItems(Arrays.asList(item,item2,item3));

        assertEquals("Cheese 2 for 1 not applied, Yogurt 0.7 kg @ 5 $/kg",8.5,basket.getSum(),0);
    }

    @Test
    public void testThreeItemsOnTwoDifferentPromoPlusNonPromoItems(){
        Item item = new SimpleItem(YOGURT, 5.0);
        Item item2 = new SimpleItem(YOGURT, 5.0);
        Item item3 = new SimpleItem(CHEESE,3.0);
        Item item4 = new SimpleItem(CHEESE,3.0);
        Item item5 = new SimpleItem(MILK,1.0,3.0);
        Item item6 = new SimpleItem(SANDWICH,1.0);
        Item item7 = new SimpleItem(SANDWICH,1.0);
        Item item8 = new SimpleItem(SANDWICH,1.0);
        Item item9 = new SimpleItem(SANDWICH,1.0);

        PricingStrategy weightedPricingStrategy = new WeightedPricingStrategy();
        PricingStrategy quantityPromoPricingStrategy = new QuantityPromoPricingStrategy(2,1);

        pricingTerminal.addPricingStrategy(weightedPricingStrategy);
        pricingTerminal.addPricingStrategy(quantityPromoPricingStrategy);
        pricingTerminal.addPromotions(quantityPromoPricingStrategy.getClass(),Arrays.asList(YOGURT,CHEESE));
        pricingTerminal.addPromotion(weightedPricingStrategy.getClass(),MILK);
        basket.addItems(Arrays.asList(item,item2,item3,item4,item5,item6,item7,item8,item9));

        assertEquals("2 for 1 on Yogurt & Cheese, Weight on Milk, default on Sandwich",15.0,basket.getSum(),0);
    }

    @Test
    public void testItemEligibleForMultiplePromoAppliedToOneOnly(){
        Item item = new SimpleItem(YOGURT, 5.0, 0.7);
        Item item2 = new SimpleItem(YOGURT, 5.0,0.7);
        Item item3 = new SimpleItem(CHEESE,3.0);
        Item item4 = new SimpleItem(CHEESE,3.0);

        PricingStrategy weightedPricingStrategy = new WeightedPricingStrategy(2);
        PricingStrategy quantityPromoPricingStrategy = new QuantityPromoPricingStrategy(2,1,1);

        pricingTerminal.addPricingStrategy(weightedPricingStrategy);
        pricingTerminal.addPricingStrategy(quantityPromoPricingStrategy);
        pricingTerminal.addPromotion(weightedPricingStrategy.getClass(),YOGURT);
        pricingTerminal.addPromotion(quantityPromoPricingStrategy.getClass(),YOGURT);

        basket.addItems(Arrays.asList(item,item2,item3,item4));

        assertEquals("Yogurt eligible for two promo, applied to one only",13.0,basket.getSum(),0);
    }
}