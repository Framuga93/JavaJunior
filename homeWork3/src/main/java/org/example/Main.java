package org.example;

import java.nio.file.Path;
import java.util.UUID;

/**
 * Написать класс с двумя методами:
 * 1. принимает объекты, имплементирующие интерфейс serializable, и сохраняющие их в файл. Название файл - class.getName() + "_" + UUID.randomUUID().toString()
 * 2. принимает строку вида class.getName() + "_" + UUID.randomUUID().toString() и загружает объект из файла и удаляет этот файл.
 *
 * Что делать в ситуациях, когда файла нет или в нем лежит некорректные данные - подумать самостоятельно.
 */

public class Main {
    public static void main(String[] args){
        Person person = new Person("Aleksey","Demo");
        Path path = Path.of(getPath(Person.class));
        IOSerializableObj io = new IOSerializableObj(person, path);
        Path path1 = Path.of("org.example.Person_2cb7059a-5403-4c59-af25-e9100ebc3bd0"); // тест
        io.saveObjAsFile();
        IOSerializableObj unserializableAndDelete = new IOSerializableObj(path1);
        unserializableAndDelete.findObjByPathAndDelete();


    }

    public static String getPath(Class className){
        return className.getName() + "_" + UUID.randomUUID();
    }

}