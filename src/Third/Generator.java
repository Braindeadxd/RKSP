package Third;

import java.io.File;
import java.util.Random;

public class Generator implements Runnable {
    private Queue queue;

    public Generator(Queue queue) {
        this.queue = queue;
    }

    @Override
    public synchronized void run() {
        try {
            Random random = new Random();
            //Генерирует файлы до остановки программы
            while (true) {
                FileType fileType = FileType.JSON;
                int type = random.nextInt(3);
                if (type == 0) {
                    fileType = FileType.JSON;
                } else if (type == 1) {
                    fileType = FileType.XML;
                } else {
                    fileType = FileType.XLS;
                }
                MyFiles myFile = new MyFiles((random.nextInt((100 - 10) + 1) + 10), fileType);
                try {
                    queue.addFile(myFile);
                } catch (IllegalStateException e) {
                }
                ;
                notifyAll();

                Thread.currentThread().sleep(random.nextInt((1000 - 100) + 1) + 100);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
