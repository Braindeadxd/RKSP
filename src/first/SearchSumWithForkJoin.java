package first;

import java.util.Arrays;
import java.util.concurrent.RecursiveTask;

public class SearchSumWithForkJoin extends RecursiveTask<Integer> {

    private int[] array;

    public SearchSumWithForkJoin(int[] array){
        this.array = array;
    }

    @Override
    protected Integer compute() {
        //System.out.printf("Task %s execute in thread %s%n", this, Thread.currentThread().getName());
        if(array.length <= 2){
            return Arrays.stream(array).sum();
        }
        SearchSumWithForkJoin leftPartOfArray = new SearchSumWithForkJoin(Arrays.copyOfRange(array,0, array.length/2));
        SearchSumWithForkJoin rightPartOfArray = new SearchSumWithForkJoin(Arrays.copyOfRange(array, array.length/2, array.length));
        leftPartOfArray.fork();
        rightPartOfArray.fork();

        int maxInLeft = leftPartOfArray.join();
        int maxInRight = rightPartOfArray.join();

        try {
            Thread.currentThread().sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return maxInLeft+maxInRight;
    }
}
