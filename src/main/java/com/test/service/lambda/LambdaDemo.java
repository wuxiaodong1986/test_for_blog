package com.test.service.lambda;

/**
 * Created by 吴晓冬 on 2018/8/17.
 */
public class LambdaDemo
{
    public static void main(String[] args)
    {
        Interface1 i1 = (i) -> i*2;
        Interface1 i2 = i -> i*2;
        Interface1 i3 = (int i) -> i*2;
        Interface1 i4 = (int i) -> { return i*2;};


    }
}

@FunctionalInterface
interface Interface1
{
    int doubleNum(int i);

    default int add(int x, int y)
    {
        return x + y;
    }
}

@FunctionalInterface
interface Interface2
{
    int doubleNum(int i);

    default int add(int x, int y)
    {
        return x + y;
    }
}

@FunctionalInterface
interface Interface3 extends Interface1, Interface2
{

    @Override
    default int add(int x, int y)
    {
        return Interface1.super.add(x, y);
    }
}