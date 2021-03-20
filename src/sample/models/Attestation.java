package sample.models;

import java.time.LocalDate;
import java.util.Date;


public class Attestation {
    private int attestationId;
    private String subject;
    public boolean isPassed(int result) {
        return false;
    }
    private String type;
    private String mark;
    private LocalDate pointDate;
    private String teacher;
    private int specialityId;



    public String getSubject() {

        return subject;
    }

    public Attestation setSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public String getMark() {
        return mark;
    }

    public Attestation setMark(String mark) {
        this.mark = mark;
        return this;
    }

    public LocalDate getPointDate() {
        return pointDate;
    }

    public Attestation setPointDate(LocalDate pointDate) {
        this.pointDate = pointDate;
        return this;
    }

    public String getTeacher() {
        return teacher;
    }

    public Attestation setTeacher(String teacher) {
        this.teacher = teacher;
        return this;
    }

    public String getType() {
        return type;
    }

    public Attestation setType(String type) {
        this.type = type;
        return this;
    }

    public Attestation Attestation(int attestationId,String subject,String type,String mark, LocalDate pointDate, String teacher,int specialityId){

        setAttestationId(attestationId);
        setSubject(subject);
        setType(type);
        setMark(mark);
        setPointDate(pointDate);
        setTeacher(teacher);
        setSpecialityId(specialityId);
        return new Attestation();

    }

    public int getSpecialityId() {
        return specialityId;
    }

    public Attestation setSpecialityId(int specialityId) {
        this.specialityId = specialityId;
        return this;
    }

    public int getAttestationId() {
        return attestationId;
    }

    public Attestation setAttestationId(int attestationId) {
        this.attestationId = attestationId;
        return this;
    }
}
