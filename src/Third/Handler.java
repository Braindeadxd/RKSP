package Third;

public class Handler implements Runnable {
    private FileType fileType;
    private Queue queue;

    public Handler(FileType fileType, Queue queue) {
        this.fileType = fileType;
        this.queue = queue;
    }

    @Override
    public synchronized void run() {
        while (true) {
            try {
                synchronized (queue) {
                    if (queue.isEmpty()) {
                        try {
                            Thread.currentThread().sleep(2000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    MyFiles nextFile = queue.getElement();
                    if (nextFile == null) {
                        wait(1000);
                    }
                    if (nextFile.getFileType() == this.fileType) {

                        nextFile = queue.getFile();
                        System.out.println("Обработка " + fileType + " файла в потоке " + Thread.currentThread().getName());
                        Thread.currentThread().sleep(7 * nextFile.getFileSize());
                        System.out.println("Файл обработан в потоке " + Thread.currentThread().getName());

                    }
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
