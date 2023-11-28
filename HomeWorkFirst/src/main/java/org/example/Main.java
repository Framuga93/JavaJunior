package org.example;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 0.1. Посмотреть разные статьи на Хабр.ру про Stream API
 * 0.2. Посмотреть видеоролики на YouTube.com Тагира Валеева про Stream API
 * <p>
 * 1. Создать список из 1_000 рандомных чисел от 1 до 1_000_000
 * 1.1 Найти максимальное
 * 2.2 Все числа, большие, чем 500_000, умножить на 5, отнять от них 150 и просуммировать
 * 2.3 Найти количество чисел, квадрат которых меньше, чем 100_000
 * <p>
 * 2. Создать класс Employee (Сотрудник) с полями: String name, int age, double salary, String department
 * 2.1 Создать список из 10-20 сотрудников
 * 2.2 Вывести список всех различных отделов (department) по списку сотрудников
 * 2.3 Всем сотрудникам, чья зарплата меньше 10_000, повысить зарплату на 20%
 * 2.4 * Из списка сотрудников с помощью стрима создать Map<String, List<Employee>> с отделами и сотрудниками внутри отдела
 * 2.5 * Из списока сорудников с помощью стрима создать Map<String, Double> с отделами и средней зарплатой внутри отдела
 */

public class Main {
    public static void main(String[] args) {

        //Задание 1
        //Создать список из 1_000 рандомных чисел от 1 до 1_000_000
        List<Integer> integerList = Stream.generate(() -> ThreadLocalRandom.current().nextInt(1_000_000))
                .limit(1_000)
                .toList();

        System.out.println(findMax(integerList));
        System.out.println(biggerThan500(integerList));
        System.out.println(lessThen100(integerList));

        //Задание 2
        //Создать список из 10-20 сотрудников
        List<Employee> employeeList = new ArrayList<>(List.of(new Employee("Алексей", 35, 95_000.00, "Продажи"),
                new Employee("Игорь", 23, 85_000.00, "Инженеры"),
                new Employee("Александр", 30, 80_000.00, "Продажи"),
                new Employee("Ольга", 33, 95_000.00, "Продажи"),
                new Employee("Тимур", 36, 85_000.00, "Инженеры"),
                new Employee("Олег", 28, 73_000.00, "Менеджеры"),
                new Employee("Анна", 32, 115_000.00, "Продажи"),
                new Employee("Степан", 50, 45_000.00, "Менеджеры"),
                new Employee("Андрей", 18, 60_000.00, "Менеджеры"),
                new Employee("Ксения", 44, 23_000.00, "Разнорабочий"),
                new Employee("Богдан", 35, 67_000.00, "Менеджеры"),
                new Employee("Инна", 31, 150_000.00, "Директор"),
                new Employee("Елена", 34, 95_000.00, "Продажи"),
                new Employee("Константин", 29, 15_000.00, "Разнорабочий"),
                new Employee("Екатерина", 45, 5_000.00, "Разнорабочий"),
                new Employee("Алексей", 50, 87_000.00, "Инженеры")));

        System.out.println(employeeByDepartment(employeeList));
        System.out.println(employeeList);
        salaryLess10k(employeeList);
        System.out.println(employeeList);
        System.out.println(getMapEmployeeByDepartment(employeeList));
        System.out.println(getMapAverageSalaryInDepartment(employeeList));


    }

    //Найти максимальное
    public static int findMax(List<Integer> integerList) {
        return integerList.stream()
                .max(Integer::compare)
                .get();
    }

    //Все числа, большие, чем 500_000, умножить на 5, отнять от них 150 и просуммировать
    public static int biggerThan500(List<Integer> integerList) {
        return integerList.stream()
                .filter(i -> i > 500_000)
                .reduce(0, (x, y) -> {
                    return x + (y * 5) - 150;
                });

    }

    //Найти количество чисел, квадрат которых меньше, чем 100_000
    public static long lessThen100(List<Integer> integerList) {
        return integerList.stream()
                .filter(i -> (i * i) < 100_000)
                .count();
    }

    //Вывести список всех различных отделов (department) по списку сотрудников
    public static List<Employee> employeeByDepartment(List<Employee> employeeList) {
        return employeeList.stream()
                .sorted(Comparator.comparing(Employee::getDepartment))
                .collect(Collectors.toList());
    }

    //Всем сотрудникам, чья зарплата меньше 10_000, повысить зарплату на 20%
    public static void salaryLess10k(List<Employee> employeeList) {
        employeeList.stream()
                .filter(e -> e.getSalary() < 10_000)
                .forEach(e -> e.setSalary(e.getSalary() * 1.2));
    }

    //Из списка сотрудников с помощью стрима создать Map<String, List<Employee>> с отделами и сотрудниками внутри отдела
    public static Map<String,List<Employee>> getMapEmployeeByDepartment(List<Employee> employeeList){
        return employeeList.stream()
                .collect(Collectors.groupingBy(Employee::getDepartment));
    }

    //Из списока сорудников с помощью стрима создать Map<String, Double> с отделами и средней зарплатой внутри отдела
    public static Map<String,Double> getMapAverageSalaryInDepartment(List<Employee> employeeList){
        return employeeList.stream()
                .collect(Collectors.groupingBy(Employee::getDepartment,Collectors.averagingDouble(Employee::getSalary)));
    }


}

//Создать класс Employee (Сотрудник) с полями: String name, int age, double salary, String department
class Employee {
    private final String name;
    private final int age;
    private double salary;
    private String department;

    public Employee(String name, int age, double salary, String department) {
        this.name = name;
        this.age = age;
        this.salary = salary;
        this.department = department;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public String getDepartment() {
        return department;
    }

    @Override
    public String toString() {
        return String.format("%s: %d лет, зарплата - %,.2f, отдел - %s", name, age, salary, department + "\n");
    }
}