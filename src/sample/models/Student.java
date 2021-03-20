package sample.models;

import sample.models.BasicClass.Person;

import java.util.ArrayList;


public class Student extends Person {
    private int idGroup;
    private String speciality;
    private int studentId;
    private int mark;
    private String attestations;




    public Student Student(int studentId,String firstName, String secondName, String lastName, int group, String speciality,int mark,String attestations){

        setStudentId(studentId);
        setFirstName(firstName);
        setSecondName(secondName);
        setLastName(lastName);
        setGroup(group);
        setSpeciality(speciality);
        setMark(mark);
        setAttestations(attestations);
        return this;

    }

    public String getSpeciality() {
        return speciality;
    }

    public Student setSpeciality(String speciality) {
        this.speciality = speciality;
        return this;
    }

    public int getGroup() {
        return idGroup;
    }

    public Student setGroup(int group) {
        this.idGroup = group;
        return this;
    }

    public int getStudentId() {
        return studentId;
    }

    public Student setStudentId(int studentId) {
        this.studentId = studentId;
        return this;
    }

    public int getMark() {
        return mark;
    }

    public Student setMark(int mark) {
        this.mark = mark;
        return this;
    }

    public String getAttestations() {
        return attestations;
    }

    public Student setAttestations(String attestations) {
        this.attestations = attestations;
        return this;
    }
}
