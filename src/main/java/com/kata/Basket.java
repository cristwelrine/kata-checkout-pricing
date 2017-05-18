package com.kata;

import com.kata.inventory.Item;
import com.kata.strategies.PricingStrategy;
import com.kata.strategies.SimplePricingStrategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by crist on 5/18/2017.
 */
public class Basket {

    private final Map<String,List<Item>> shoppingItems;
    private final Map<Class<? extends PricingStrategy>, PricingStrategy> strategyMap;

    public Basket() {
        this.shoppingItems = new HashMap<>();
        this.strategyMap = new HashMap<>();
    }

    public double getTotal() {
        double total = 0 ;

        Map<String, List<Item>> basketCopy = new HashMap<>(shoppingItems);

        for(PricingStrategy pricingStrategy : strategyMap.values()){
            total += pricingStrategy.calculate(basketCopy);
        }

        PricingStrategy simplePricingStrategy = new SimplePricingStrategy();
        total += simplePricingStrategy.calculate(basketCopy);

        return total;
    }

    public void addItem(Item item){
        if(!shoppingItems.containsKey(item.getName())){
            shoppingItems.put(item.getName(),new ArrayList<>());
        }
        shoppingItems.get(item.getName()).add(item);
    }

    public void addItems(List<Item> items){
        for(Item item : items){
            this.addItem(item);
        }
    }

    public void addPricingStrategy(PricingStrategy pricingStrategy){
        strategyMap.put(pricingStrategy.getClass(),pricingStrategy);
    }

    public void addPromotion(Class<? extends PricingStrategy> pricingStrategy, String promotionItem){
        strategyMap.get(pricingStrategy).addPromotionItem(promotionItem);
    }

    public void addPromotions(Class<? extends PricingStrategy> pricingStrategy, List<String> promotionItems){
        strategyMap.get(pricingStrategy).addPromotionItems(promotionItems);
    }
}