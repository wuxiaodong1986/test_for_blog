package com.test.service.stream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class StreamDemo1
{
    public static void main(String[] args)
    {
        //从集合创建
        List<Integer> list = new ArrayList<>();
        //串行流
        list.stream();
        //并行流
        list.parallelStream();

        //从数组创建
        Arrays.stream(new int[]{1,2,3,4});

        //数字流
        IntStream.of(1,2,3);
        IntStream.rangeClosed(1, 10);

        //随机无限流
        new Random().ints().limit(10);

        //自己创建流
        Stream.generate(new Random()::nextInt).limit(10);
    }
}
