package com.test.service.lambda;

import java.util.Date;
import java.util.function.*;

public class FunctionDemo
{
    public static void main(String[] args)
    {
        //断言
        Predicate<Integer> predicate = i -> i > 0;
        System.out.println(predicate.test(-9));

        //消费
        Consumer<String> consumer = s -> System.out.println(s);
        consumer.accept("123");

        //提供对象
        Supplier<Date> supplier = () -> new Date();
        System.out.println(supplier.get());

        //输入一个对象输出其他类型的对象
        Function<Integer, String> function = i -> String.valueOf(i);
        System.out.println(function.apply(123));

        //输入两个不同类型的对象输出另一个类型的对象
        BiFunction<Integer, String, Date> biFunction  = (i, s) ->
        {
            System.out.println(i);
            System.out.println(s);
            return new Date();
        };
        System.out.println(biFunction.apply(1, "123"));

        //一元函数 一个输入一个输出类型相同
        UnaryOperator<String> unaryOperator = s -> "s: " + s;
        System.out.println(unaryOperator.apply("123"));

        //二元函数 两个输入一个输出类型相同
        BinaryOperator<String> binaryOperator = (s1, s2) -> s1 + "|" + s2;
        System.out.println(binaryOperator.apply("123", "321"));
    }
}
