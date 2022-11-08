package Third;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Queue {
    private int size = 5;
    private BlockingQueue<MyFiles> files;

    public Queue() {
        this.files = new ArrayBlockingQueue<>(5);
    }

    public MyFiles getFile() throws InterruptedException {
        return files.take();
    }

    public boolean addFile(MyFiles file) {
        return files.add(file);
    }

    public MyFiles getElement() {
        return files.peek();
    }

    public boolean isEmpty() {
        return files.isEmpty();
    }
}
