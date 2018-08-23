package com.test.service.reactiveStream;

import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;

public class ReactiveStreamDemo1
{
    public static void main(String[] args) throws InterruptedException
    {
        //发布者
        SubmissionPublisher<Integer> publisher = new SubmissionPublisher<>();
        //订阅者
        Flow.Subscriber<Integer> subscriber = new Flow.Subscriber<Integer>()
        {

            private Flow.Subscription subscription;

            @Override
            public void onSubscribe(Flow.Subscription subscription)
            {
                this.subscription = subscription;

                //请求一条数据
                this.subscription.request(1);
            }

            @Override
            public void onNext(Integer item)
            {
                System.out.println("接收到数据：" + item);

                //数据不满足，再获取一条数据
                this.subscription.request(1);
//                //数据满足，不再请求数据
//                this.subscription.cancel();
            }

            @Override
            public void onError(Throwable throwable)
            {
                //出现异常，打印异常
                throwable.printStackTrace();
                //不再接收数据
                this.subscription.cancel();
            }

            @Override
            public void onComplete()
            {
                System.out.println("数据全部处理完毕");
            }
        };
        //绑定
        publisher.subscribe(subscriber);
        //生产数据,丢入发布者
        publisher.submit(111);
        publisher.submit(222);
        publisher.submit(333);
        //关闭发布者
        publisher.close();

        Thread.currentThread().join(1000);
    }
}
