package main.java;

import main.java.controller.Controller;

import java.util.Scanner;

/**
 * Created by devilpi on 06/01/2018.
 */
public class Main {
    static String info0 = "\nwelcome to simple attendance system v1.0\nPlease input your username: ";
    static String info1 = "Please input your password: ";
    static Controller controller = new Controller();

    public static void main(String[] args) {
        control();
    }

    public static void control() {
        System.out.print(info0);
        Scanner sc = new Scanner(System.in);
        int employeeId = Integer.parseInt(sc.nextLine());
        System.out.print(info1);
        String password = sc.nextLine();
        controller.login(employeeId, password);
    }
}
