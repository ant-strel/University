package sample.models;

public class Subject {
        private String speciality;
        private String subjectName;
        private int subjectId;



    public String getSubjectName() {
        return subjectName;
    }

    public Subject setSubjectName(String subjectName) {
        this.subjectName = subjectName;
        return this;
    }

    public String getSpeciality() {
        return speciality;
    }

    public Subject setSpeciality(String speciality) {
        this.speciality = speciality;
        return this;
    }


    public Subject Subject(String subjectName,String speciality,int subjectId){
        setSubjectName(subjectName);
        setSpeciality(speciality);
        setSubjectId(subjectId);

        return new Subject();

    }

    public int getSubjectId() {
        return subjectId;
    }

    public Subject setSubjectId(int subjectId) {
        this.subjectId = subjectId;
        return this;
    }
}
