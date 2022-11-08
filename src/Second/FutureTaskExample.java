package Second;

import java.util.concurrent.Callable;

public class FutureTaskExample implements Callable {
    private int userNumber;

    public FutureTaskExample(int userNumber) {
        this.userNumber = userNumber;
    }

    @Override
    public Object call() throws Exception {
        System.out.println("Обработка для потока " + Thread.currentThread().getName());
        Thread.currentThread().sleep(2500);
        System.out.println(userNumber * userNumber);
        return userNumber * userNumber;
    }
}
