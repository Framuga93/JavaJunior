package org.example;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;


public class IOSerializableObj {
    private Serializable serializableObj;
    private final Path path;

    public IOSerializableObj(Serializable serializableObj, Path path) {
        this.serializableObj = serializableObj;
        this.path = path;
    }

    public IOSerializableObj(Path path) {
        this.path = path;
    }

    public void saveObjAsFile() {
        try(OutputStream outputStream = Files.newOutputStream(path);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream))
        {
            objectOutputStream.writeObject(serializableObj);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public void findObjByPathAndDelete() {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(Files.newInputStream(path))) {
            Files.delete(path);
            System.out.println(objectInputStream.readObject());
        } catch (IOException e) {
            if (e instanceof StreamCorruptedException)
                throw new IllegalStateException("Файл поврежден" + path);
            throw new IllegalStateException("Такого файла нет " + path);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }


}
