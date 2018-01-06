package main.java.entity;

/**
 * Created by devilpi on 06/01/2018.
 */
public class Employee extends User {
    int deparment_id;
    Employee(User user){
        this.age = user.age;
        this.employeeId = user.employeeId;
        this.name = user.name;
        this.password = user.password;
        this.type = TYPE.EMPLOYEE;
        
    }
}
