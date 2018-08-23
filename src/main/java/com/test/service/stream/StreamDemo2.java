package com.test.service.stream;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamDemo2
{
    public static void main(String[] args)
    {
        String str = "my name is wuxiaodong";

        //过滤后打印长度
        Stream.of(str.split(" ")).filter(s -> s.length() > 2).map(s -> s.length()).forEach(System.out::println);

        //将单词里的字符都拿出来拼成新的流
        Stream.of(str.split(" ")).flatMap(s -> s.chars().boxed()).forEach(i -> System.out.println((char) i.intValue()));

        //peek 中间操作，可debug用
        Stream.of(str.split(" ")).peek(System.out::println).forEach(System.out::println);

        //并行打印
        Stream.of(str.split(" ")).parallel().forEach(System.out::println);

        //顺序打印
        Stream.of(str.split(" ")).parallel().forEachOrdered(System.out::println);

        //收集到容器
        List<String> list = Stream.of(str.split(" ")).collect(Collectors.toList());
        System.out.println(list);

        //结果整理处理
        Optional<String> reduce = Stream.of(str.split(" ")).reduce((s1, s2) -> s1 + "|" + s2);
        System.out.println(reduce.orElse("123"));


    }
}
