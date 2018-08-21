package com.test.service.lambda;

import java.text.DecimalFormat;
import java.util.function.Function;

/**
 * Created by 吴晓冬 on 2018/8/17.
 */
public class MoneyDemo
{
    public static void main(String[] args)
    {
        MyMoney myMoney = new MyMoney(99999999);

        myMoney.printMoney(i -> new DecimalFormat("#,###").format(i));
    }
}

interface IMoneyFormat
{
    String format(int i);
}

class MyMoney
{
    private final int money;

    MyMoney(int money)
    {
        this.money = money;
    }

    public void printMoney(IMoneyFormat moneyFormat)
    {
        System.out.println(moneyFormat.format(this.money));
    }

    public void printMoney(Function<Integer, String> moneyFormat)
    {
        System.out.println(moneyFormat.format(this.money));
    }
}