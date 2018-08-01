package com.test.service;

import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

/**
 * Created by 吴晓冬 on 2018/8/1.
 */
public class SumTask extends RecursiveTask<Long>
{
    static final int threshold = 10;

    long[] array;

    int start;

    int end;

    SumTask(long[] array, int start, int end)
    {
        this.array = array;
        this.start = start;
        this.end = end;
    }

    @Override
    protected Long compute()
    {
        if ((end - start) <= threshold)
        {
            long sum = 0;
            for (int i = start; i < end; i++)
            {
                sum += array[i];

                try
                {
                    Thread.sleep(10);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }

            return sum;
        }

        int middle = (end+start) / 2;
//        System.out.println(String.format("split %d~%d ==> %d~%d, %d~%d", start, end, start, middle, middle, end));
        SumTask subtask1 = new SumTask(this.array, start, middle);
        SumTask subtask2 = new SumTask(this.array, middle, end);
        invokeAll(subtask1, subtask2);

        Long subresult1 = subtask1.join();
        Long subresult2 = subtask2.join();

        Long result = subresult1 + subresult2;
//        System.out.println("result = " + subresult1 + " + " + subresult2 + " ==> " + result);

        return result;
    }

    public static void main(String[] args)
    {
        long[] array = new long[1000];
        Random random = new Random();
        for (int i = 0; i < array.length; i++)
        {
            array[i] = random.nextInt(100);
        }

        long startTime = System.currentTimeMillis();
        long sum = 0;
        for (int i = 0; i < array.length; i++)
        {
            sum += array[i];
            try
            {
                Thread.sleep(10);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        long endTime = System.currentTimeMillis();
        System.out.println("signel sum: " + sum + " in " + (endTime - startTime) + " ms.");

        ForkJoinPool fjp = new ForkJoinPool(10);
        ForkJoinTask<Long> task = new SumTask(array, 0, array.length);
        startTime = System.currentTimeMillis();
        Long result = fjp.invoke(task);
        endTime = System.currentTimeMillis();
        System.out.println("Fork/join sum: " + result + " in " + (endTime - startTime) + " ms.");
    }
}