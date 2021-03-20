package sample.models;

import java.util.ArrayList;

public class Speciality {

    private int specialityId;
    private String specialityName;
    private ArrayList<Subject> subjects = new ArrayList<>();

    public Speciality Speciality(int specialityId,String specialityName){
        setSpecialityId(specialityId);
        setSpecialityName(specialityName);
        return this;
    }

    public int getSpecialityId() {
        return specialityId;
    }

    public Speciality setSpecialityId(int specialityId) {
        this.specialityId = specialityId;
        return this;
    }

    public String getSpecialityName() {
        return specialityName;
    }

    public Speciality setSpecialityName(String name) {

        this.specialityName = name;
        return this;
    }

    public ArrayList<Subject> getSubjects() {
        return subjects;
    }

    public Speciality setSubjects(ArrayList<Subject> subjects) {
        this.subjects = subjects;
        return this;
    }
}
