package com.test.service.stream;

import java.util.*;
import java.util.stream.Collectors;

public class StreamDemo3
{
    public static void main(String[] args)
    {
        Student student1 = new Student(1, "1", 1);
        Student student2 = new Student(2, "2", 2);
        Student student3 = new Student(3, "2", 3);
        Student student4 = new Student(4, "3", 4);
        Student student5 = new Student(5, "3", 5);
        Student student6 = new Student(6, "3", 6);

        List<Student> students = new ArrayList<>();
        students.add(student1);
        students.add(student2);
        students.add(student3);
        students.add(student4);
        students.add(student5);
        students.add(student6);

        //得到所有学生的得分列表
        List<Integer> scores = students.stream().map(Student::getScore).collect(Collectors.toCollection(LinkedList::new));
        System.out.println(scores);

        //统计汇总信息
        IntSummaryStatistics scoreSummaryStatistics = students.stream().collect(Collectors.summarizingInt(Student::getScore));
        System.out.println(scoreSummaryStatistics);

        //分块
        Map<Boolean, List<Student>> partitions = students.stream().collect(Collectors.partitioningBy(student -> student.getScore() > 3));
        System.out.println(partitions);

        //按姓名分组
        Map<String, List<Student>> names = students.stream().collect(Collectors.groupingBy(Student::getName));
        System.out.println(names);

        //获取每个姓名重名学生的个数
        Map<String, Long> nameCounts = students.stream().collect(Collectors.groupingBy(Student::getName, Collectors.counting()));
        System.out.println(nameCounts);
    }
}

class Student
{
    private Integer id;

    private String name;

    private Integer score;

    public Student(Integer id, String name, Integer score)
    {
        this.id = id;
        this.name = name;
        this.score = score;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Integer getScore()
    {
        return score;
    }

    public void setScore(Integer score)
    {
        this.score = score;
    }

    @Override
    public String toString()
    {
        return "Student{" + "id=" + id + ", name='" + name + '\'' + ", score=" + score + '}';
    }
}