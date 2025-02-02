/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package employes;





import javafx.beans.property.*;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class officer {
    private final IntegerProperty id;
    private final StringProperty name;
    private final StringProperty department;
    private final StringProperty email;
    private final DoubleProperty gpa;

    public officer(int id, String name, String department, String email, double gpa) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.department = new SimpleStringProperty(department);
        this.email = new SimpleStringProperty(email);
        this.gpa = new SimpleDoubleProperty(gpa);
    }
    public int getId() {
        return id.get();
    }
    public void setId(int id) {
        this.id.set(id);
    }
    public IntegerProperty idProperty() {
        return id;
    }
    public String getName() {
        return name.get();
    }
    public void setName(String name) {
        this.name.set(name);
    }
    public StringProperty nameProperty() {
        return name;
    }
    public String getDepartment() {
        return department.get();
    }
    public void setDepartment(String department) {
        this.department.set(department);
    }
    public StringProperty departmentProperty() {
        return department;
    }
    public String getEmail() {
        return email.get();
    }
    public void setEmail(String email) {
        this.email.set(email);
    }
    public StringProperty emailProperty() {
        return email;
    }
    public double getGpa() {
        return gpa.get();
    }
    public void setGpa(double gpa) {
        this.gpa.set(gpa);
    }
    public DoubleProperty gpaProperty() {
        return gpa;
    }
}
