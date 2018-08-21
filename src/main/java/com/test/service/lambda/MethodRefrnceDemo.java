package com.test.service.lambda;

import java.util.function.*;

public class MethodRefrnceDemo
{
    public static void main(String[] args)
    {
        Dog dog = new Dog();
        //静态方法引用
        Consumer<Dog> consumer = Dog::bark;
        consumer.accept(dog);

        //非静态方法引用，使用对象实例的方法引用
        IntUnaryOperator operator = dog::eat;
        operator.applyAsInt(2);

        //使用类名来引用非静态方法
        BiFunction<Dog, Integer, Integer> biFunction = Dog::eat;
        biFunction.apply(dog, 2);

        //无参构造函数引用
        Supplier<Dog> supplier = Dog::new;
        System.out.println(supplier.get().name);

        //有参构造函数引用
        Function<String, Dog> function = Dog::new;
        System.out.println(function.apply("旺财").name);
    }
}

class Dog
{
    public Dog(){}

    public Dog(String name)
    {
        this.name = name;
    }

    public String name = "狗";

    private Integer food = 10;

    public static void bark(Dog dog)
    {
        System.out.println(dog.name + " 叫了");
    }

    public Integer eat(Integer num)
    {
        this.food-=num;
        System.out.println("吃了" + num + "斤");
        return this.food;
    }
}