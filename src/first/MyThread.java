package first;

public class MyThread implements Runnable {
    private int[] array;
    private int enxIndex;
    private int sum;
    private int startIndex;

    public MyThread(int[] array, int enxIndex, int startIndex) {
        this.array = array;
        this.enxIndex = enxIndex;
        this.startIndex = startIndex;
    }

    public int getSum() {
        return sum;
    }

    @Override
    public void run() {
        this.sum = 0;
        for (int i = startIndex; i < enxIndex; i++) {
            this.sum += array[i];
            try {
                Thread.currentThread().sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
