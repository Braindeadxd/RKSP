import Second.FutureTaskExample;
import Third.FileType;
import Third.Generator;
import Third.Handler;
import Third.Queue;
import first.SearchSumWithForkJoin;
import first.SearchSum;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.FutureTask;

public class Main {
    private static final long MEGABYTE = 1024L * 1024L;

    public static void main(String[] args) throws InterruptedException, ExecutionException, IOException, NoSuchFieldException, ClassNotFoundException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        //startFirst();
        //startFutureTaskExample();
        //startGenerator();
        JavaNioWork second = new JavaNioWork();
       /* second.prac1();
        second.FileInputStream();
        second.FileChannel();
        second.ApacheCommonsIo();
        second.FileClass();*/
        //second.prac3("\\test\\TestFile.txt");
        second.prac4();
    }

    public static void startFirst() throws InterruptedException {
        int[] array = generateArray(10000);
        Runtime runtime = Runtime.getRuntime();
        runtime.gc();
        //Обьект для первых двух пунктов

        SearchSum searchSum = new SearchSum(array, 10000);
        long startTime = System.nanoTime();
        System.out.println(searchSum.sumInSequence());
        long memory = runtime.totalMemory() - runtime.freeMemory();
        long endTime = System.nanoTime();
        System.out.println("Время последовательного алгоритма: " + (endTime - startTime) / 1000000 + " Память: " + bytesToMegabytes(memory));
        startTime = System.nanoTime();
        System.out.println(searchSum.searchForMaxWithThreads());
        memory = runtime.totalMemory() - runtime.freeMemory();
        endTime = System.nanoTime();
        System.out.println("Время с исп. Thread: " + (endTime - startTime) / 1000000 + " Память: " + bytesToMegabytes(memory));
        // searchSum.printArray();

        //ForkJoin

        SearchSumWithForkJoin searchSumWithForkJoin = new SearchSumWithForkJoin(array);
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        startTime = System.nanoTime();
        System.out.println(forkJoinPool.invoke(searchSumWithForkJoin));
        memory = runtime.totalMemory() - runtime.freeMemory();
        endTime = System.nanoTime();
        System.out.println("Время ForkJoin: " + (endTime - startTime) / 1000000 + " Память: " + bytesToMegabytes(memory));
    }

    public static long bytesToMegabytes(long bytes) {
        return bytes / MEGABYTE;
    }

    public static void startFutureTaskExample() throws ExecutionException, InterruptedException {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        while (n != 0) {
            FutureTaskExample task = new FutureTaskExample(n);
            FutureTask<Integer> future = new FutureTask<>(task);
            new Thread(future).start();
            //System.out.println(future.get());
            n = scanner.nextInt();
        }
        System.out.println("End");
    }

    public static int[] generateArray(int size) {
        int[] array = new int[size];
        Random rand = new Random();
        for (int i = 0; i < size; i++) {
            array[i] = rand.nextInt((1000));
        }
        return array;
    }

    public static void startGenerator() {
        Queue queue = new Queue();

        Generator generator = new Generator(queue);
        Handler handler1 = new Handler(FileType.JSON, queue);
        Handler handler2 = new Handler(FileType.XML, queue);
        Handler handler3 = new Handler(FileType.XLS, queue);

        new Thread(generator).start();
        new Thread(handler1).start();
        new Thread(handler2).start();
        new Thread(handler3).start();

    }
}
