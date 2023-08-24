package Entity;

import java.time.LocalDate;

public class Coach {
    private int coachID;
    private String name;
    private String position;
    private double salary;
    private String teamName;
    private LocalDate hireDate;

    public Coach(int coachID, String name, String position, double salary) {
        this.coachID = coachID;
        this.name = name;
        this.position = position;
        this.salary = salary;
    }

    public Coach(String name, String position, double salary) {
        this.name = name;
        this.position = position;
        this.salary = salary;
    }

    public Coach(int coachID, String name, String position, double salary, LocalDate hireDate) {
        this.coachID = coachID;
        this.name = name;
        this.position = position;
        this.salary = salary;
        this.hireDate = hireDate;
    }

    public Coach(String name, String position, double salary, LocalDate hireDate) {
        this.name = name;
        this.position = position;
        this.salary = salary;
        this.hireDate = hireDate;
    }

    public int getCoachID() {
        return coachID;
    }

    public void setCoachID(int coachID) {
        this.coachID = coachID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }

    public String toString() {
        return "Coach{" +
                "coachID='" + coachID + '\'' +
                ", name='" + name + '\'' +
                ", position='" + position + '\'' +
                ", salary=" + salary +
                '}';
    }
}
