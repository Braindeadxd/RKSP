package first;

public class SearchSum {
    private int[] array;
    private int size;

    private int taskSize;
    private int currentTask;

    public SearchSum(int[] array, int size) {
        this.array = array;
        this.size = size;
    }

    /*Последовательный поиск суммы*/
    public int sumInSequence() throws InterruptedException {
        int sum = 0;
        for (int i = 0; i < size; i++) {
           sum += array[i];
          Thread.sleep(1);
        }
        return sum;
    }

    /*Разделение поиска суммы на два параллельных потока - один вычисляет от 0 элемента до 4999, второй от 5000 до 10000*/
    public int searchForMaxWithThreads() throws InterruptedException {
        MyThread leftPartOfArray = new MyThread(this.array, 5000, 0);
        MyThread rightPartOfArray = new MyThread(this.array, 10000, 5000);
        Thread thread1 = new Thread(leftPartOfArray);
        Thread thread2 = new Thread(rightPartOfArray);

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        return leftPartOfArray.getSum()+rightPartOfArray.getSum();
    }

    public void searchForMaxWithForkJoin() {

    }

    public void printArray() {
        for (int i = 0; i < size; i++) {
            System.out.print(array[i] + " ");
        }
    }
}
