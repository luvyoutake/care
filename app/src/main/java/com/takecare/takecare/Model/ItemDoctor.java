package com.takecare.takecare.Model;

/**
 * Created by xrezut on 03/03/2018.
 */

public class ItemDoctor {

    private String dName;
    private String specialization;
    private String image;
    private String url;
    private String sched;
    private String clinic;
    private String fee;
    private String place;

    public ItemDoctor(String dName, String specialization, String image, String url, String sched, String clinic,String place) {
        this.dName = dName;
        this.specialization = specialization;
        this.image = image;
        this.url = url;
        this.sched = sched;
        this.clinic = clinic;
        this.fee = fee;
        this.place = place;
    }

    public ItemDoctor(){

    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getClinic() {
        return clinic;
    }

    public void setClinic(String clinic) {
        this.clinic = clinic;
    }

    public String getSched() {
        return sched;
    }

    public void setSched(String sched) {
        this.sched = sched;
    }

    public String getdName() {
        return dName;
    }

    public void setdName(String dName) {
        this.dName = dName;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }
}
