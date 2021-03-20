package sample.models;

import sample.models.BasicClass.Person;

public class Teacher extends Person {

    private int teacherId;
    private String position;
    private String chair;

    public Teacher Teacher(String firstName, String secondName, String lastName, String position, String chair,int teacherId){

        setFirstName(firstName);
        setSecondName(secondName);
        setLastName(lastName);
        setChair(chair);
        setPosition(position);
        setTeacherId(teacherId);
        return new Teacher();

    }

    public String getPosition() {
        return position;
    }

    public Teacher setPosition(String position) {
        this.position = position;
        return this;
    }

    public String getChair() {
        return chair;
    }

    public Teacher setChair(String chair) {
        this.chair = chair;
        return this;
    }




    public int getTeacherId() {
        return teacherId;
    }

    public Teacher setTeacherId(int teacherId) {
        this.teacherId = teacherId;
        return this;
    }
}
