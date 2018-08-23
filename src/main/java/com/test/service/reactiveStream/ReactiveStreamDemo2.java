package com.test.service.reactiveStream;

import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;

public class ReactiveStreamDemo2
{
    public static void main(String[] args) throws InterruptedException
    {
        SubmissionPublisher<Integer> publisher = new SubmissionPublisher<>();

        MyProcessor processor = new MyProcessor();

        publisher.subscribe(processor);

        Flow.Subscriber<String> subscriber = new Flow.Subscriber<String>() {

            private Flow.Subscription subscription;

            @Override
            public void onSubscribe(Flow.Subscription subscription)
            {
                this.subscription = subscription;

                this.subscription.request(1);
            }

            @Override
            public void onNext(String item)
            {
                System.out.println("订阅者接收到数据：" + item);

                this.subscription.request(1);
            }

            @Override
            public void onError(Throwable throwable)
            {

            }

            @Override
            public void onComplete()
            {

            }
        };

        processor.subscribe(subscriber);

        //生产数据,丢入发布者
        publisher.submit(111);
        publisher.submit(222);
        publisher.submit(333);
        //关闭发布者
        publisher.close();

        Thread.currentThread().join(1000);
    }
}

class MyProcessor extends SubmissionPublisher<String> implements Flow.Processor<Integer, String>
{

    private Flow.Subscription subscription;

    @Override
    public void onSubscribe(Flow.Subscription subscription)
    {
        this.subscription = subscription;

        this.subscription.request(1);
    }

    @Override
    public void onNext(Integer item)
    {
        System.out.println("处理器接收到数据：" + item);

        if (item > 0)
        {
            this.submit("转换后的数据：" + item);
        }

        this.subscription.request(1);
    }

    @Override
    public void onError(Throwable throwable)
    {

    }

    @Override
    public void onComplete()
    {

    }
}
