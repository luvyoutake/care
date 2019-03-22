package com.takecare.takecare.Model;

public class MedicalHistory {

    String date, hospital, doctor, diagnosis, prescriptedmedicine, otheradvice, followup, currentuserid, id;

    public MedicalHistory() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getPrescriptedmedicine() {
        return prescriptedmedicine;
    }

    public void setPrescriptedmedicine(String prescriptedmedicine) {
        this.prescriptedmedicine = prescriptedmedicine;
    }

    public String getOtheradvice() {
        return otheradvice;
    }

    public void setOtheradvice(String otheradvice) {
        this.otheradvice = otheradvice;
    }

    public String getFollowup() {
        return followup;
    }

    public void setFollowup(String followup) {
        this.followup = followup;
    }

    public String getCurrentuserid() {
        return currentuserid;
    }

    public void setCurrentuserid(String currentuserid) {
        this.currentuserid = currentuserid;
    }
}

