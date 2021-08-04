package com.havefun.bhwsandroidstudy;

import java.util.Objects;

public class Student {

    private String string;
    private int age;
    public Student() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return age == student.age &&
                Objects.equals(string, student.string);
    }

    @Override
    public int hashCode() {
        return Objects.hash(string, age);
    }
}
