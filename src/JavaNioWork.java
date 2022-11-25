
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.*;
import java.util.List;

import static java.nio.file.StandardWatchEventKinds.*;


public class JavaNioWork {

    private static Path folderPath = Paths.get("C:\\Users\\Computer\\IdeaProjects\\RKSP\\test\\");
    private static Path testFilePath = Paths.get("C:\\Users\\Computer\\IdeaProjects\\RKSP\\test\\TestFile.txt");
    private static Path testFilePathFor1 = Paths.get("C:\\Users\\Computer\\IdeaProjects\\RKSP\\test\\TestFileFor1.txt");

    public JavaNioWork() {
    }

    /*Простое создание файла и запись в него, чтение с помощью nio*/
    public void prac1() throws IOException {
        String text = "There is an idea of a Patrick Bateman; some kind of abstraction. But there is no real me: only an entity, something illusory. And though I can hide my cold gaze, and you can shake my hand and feel flesh gripping yours and maybe you can even sense our lifestyles are probably comparable... I simply am not there";
        try (FileOutputStream file = new FileOutputStream(String.valueOf(testFilePathFor1))) {
            byte[] buffer = text.getBytes();
            file.write(buffer, 0, buffer.length);
            file.close();
            System.out.println("Записано");
            //вывод
            RandomAccessFile aFile = new RandomAccessFile(String.valueOf(testFilePathFor1), "rw");
            FileChannel inChannel = aFile.getChannel();

            ByteBuffer buf = ByteBuffer.allocate(256);
            int bytesRead = inChannel.read(buf);

            while (bytesRead != -1) {
                buf.flip();
                while (buf.hasRemaining()) {
                    System.out.print((char) buf.get());
                }
                buf.clear();
                bytesRead = inChannel.read(buf);
            }
            aFile.close();
        }
    }

    public void FileInputStream() throws IOException {
        FileInputStream fileInputStream = new FileInputStream(String.valueOf(testFilePath));
        FileOutputStream fileOutputStream = new FileOutputStream(String.valueOf(folderPath) + "/TestFileCopyByFIS.txt");
        fileOutputStream.write(fileInputStream.readAllBytes());
    }

    public void FileChannel() throws IOException {
        FileChannel source = null;
        FileChannel destination = null;
        try {
            source = new FileInputStream(String.valueOf(testFilePath)).getChannel();
            destination = new FileOutputStream(String.valueOf(folderPath) + "/TestFileCopyByFC.txt").getChannel();
            destination.transferFrom(source, 0, source.size());

        } finally {
            source.close();
            destination.close();
        }

    }

    public void ApacheCommonsIo() throws IOException {
        File sourceFile = new File(String.valueOf(testFilePath));
        File destFile = new File(String.valueOf(folderPath) + "/TestFilePathByApache.txt");
        FileUtils.copyFile(sourceFile, destFile);
    }

    public void FileClass() throws IOException {
        File sourceFile = new File(String.valueOf(testFilePath));
        File destFile = new File(String.valueOf(folderPath) + "/TestFilePathByFile.txt");
        Files.copy(sourceFile.toPath(), destFile.toPath());
    }

    public void copyFile(Path path) throws IOException {
        File sourceFile = new File(String.valueOf(path));
        File destFile = new File(String.valueOf(path.getParent()));
        Files.copy(sourceFile.toPath(), destFile.toPath());
    }

    /*Вычисление контрольной суммы файла с последующим высвобождением буффера*/
    public void prac3(String name) throws IOException, NoSuchFieldException, ClassNotFoundException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        FileInputStream fileInputStream = new FileInputStream(name);
        FileChannel fileChannel = fileInputStream.getChannel();
        MappedByteBuffer byteBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, (int) fileChannel.size());

        /*Алгоритм подсчета контрольной суммы*/
        int sum = 0;
        while (byteBuffer.hasRemaining()) {
            if ((sum & 1) != 0) {
                sum = (sum >> 1) + 0x8000;
            } else {
                sum >>= 1;
            }
            sum += byteBuffer.get() & 0xff;
            sum &= 0xffff;
        }
        /*Высвобождение буффера*/
        fileChannel.close();
        fileInputStream.close();

        Class<?> unsafeClass = Class.forName("sun.misc.Unsafe");
        Field unsafeField = unsafeClass.getDeclaredField("theUnsafe");
        unsafeField.setAccessible(true);
        Object unsafe = unsafeField.get(null);
        Method invokeCleaner = unsafeClass.getMethod("invokeCleaner", ByteBuffer.class);
        invokeCleaner.invoke(unsafe, byteBuffer);
        System.out.println("\n Контрольная сумма для файла " + name + ": " + sum);
    }

    /*Чтение строк из файлов в списки, после чего поиск различий*/
    public void compareByLine(String filePath) throws IOException {
        Path newFilePath = Paths.get(filePath);
        String copiedFile = newFilePath.getParent() + "_copy\\" + newFilePath.getFileName();
        Path copiedPath = Paths.get(copiedFile);

        List<String> newFileLines = Files.readAllLines(newFilePath);
        List<String> copiedFileLines = Files.readAllLines(copiedPath);

        List<String> copiedFileLinesListCopy = List.copyOf(copiedFileLines);

        copiedFileLines.removeAll(newFileLines);
        newFileLines.removeAll(copiedFileLinesListCopy);

        System.out.println("Изменена строка: \n Было: " + copiedFileLines + "\nСтало: " + newFileLines);
        copyDir();
    }

    /*Создание копии отслеживаемой папки (для вычисления суммы и сравнения файлов)*/
    public void copyDir() throws IOException {
        File src = new File(String.valueOf(folderPath));
        File dst = new File(String.valueOf(folderPath) + "_copy/");
        FileUtils.copyDirectory(src, dst);
    }


    public void prac4() throws IOException, InterruptedException, NoSuchFieldException, ClassNotFoundException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        WatchService watchService = FileSystems.getDefault().newWatchService();

        copyDir();


        //будем следить за созданием, изменение и удалением файлов.
        folderPath.register(watchService, ENTRY_CREATE, ENTRY_MODIFY, ENTRY_DELETE);
        boolean poll = true;
        while (poll) {
            WatchKey key = watchService.take();
            for (WatchEvent<?> event : key.pollEvents()) {
                WatchEvent.Kind<?> kind = event.kind();
                if (ENTRY_CREATE.equals(kind)) {
                    System.out.println("Создан файл: " + event.context());
                    copyDir();
                } else if (ENTRY_MODIFY.equals(kind)) {
                    String filePath = String.valueOf(folderPath) + "\\" + event.context();
                    compareByLine(filePath);
                } else if (ENTRY_DELETE.equals(kind)) {
                    String filePath = String.valueOf(folderPath.getParent()) + "\\test_copy\\" + event.context();
                    prac3(filePath);
                    Files.delete(Paths.get(filePath));
                }
                System.out.println("Событие: " + event.kind() + " - Файл : " + event.context());
            }
            poll = key.reset();
        }
    }
}
