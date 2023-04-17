package com.yandex.market.basketservice.errors;

public class NotEnoughIGoodsInStockException extends Exception{
    public NotEnoughIGoodsInStockException(String message){
        super(message);
    }
}
