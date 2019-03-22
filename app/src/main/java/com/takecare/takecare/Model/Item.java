package com.takecare.takecare.Model;

public class Item {
    private String text,subText,symptoms,causes,prevention,precautions,sideeffect,scientificname,commonname,origin,medicinaluses
            ,date,placeofconsultation,diagnosis,prescriptedmedicine,otheradvice;
    private boolean expandable;

    public Item() {
    }

    public Item(String id, String text, String subText, boolean expandable) {
        this.date = date;
        this.placeofconsultation = placeofconsultation;
        this.diagnosis = diagnosis;
        this.prescriptedmedicine = prescriptedmedicine;
        this.otheradvice = otheradvice;
        this.scientificname = scientificname;
        this.commonname = commonname;
        this.origin = origin;
        this.medicinaluses = medicinaluses;
        this.precautions = precautions;
        this.sideeffect = sideeffect;
        this.prevention = prevention;
        this.causes = causes;
        this.symptoms = symptoms;
        this.text = text;
        this.subText = subText;
        this.expandable = expandable;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPlaceofconsultation() {
        return placeofconsultation;
    }

    public void setPlaceofconsultation(String placeofconsultation) {
        this.placeofconsultation = placeofconsultation;
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

    public String getScientificname() {
        return scientificname;
    }

    public void setScientificname(String scientificname) {
        this.scientificname = scientificname;
    }

    public String getCommonname() {
        return commonname;
    }

    public void setCommonname(String commonname) {
        this.commonname = commonname;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getMedicinaluses() {
        return medicinaluses;
    }

    public void setMedicinaluses(String medicinaluses) {
        this.medicinaluses = medicinaluses;
    }

    public String getPrecautions() {
        return precautions;
    }

    public void setPrecautions(String precautions) {
        this.precautions = precautions;
    }

    public String getSideeffect() {
        return sideeffect;
    }

    public void setSideeffect(String sideeffect) {
        this.sideeffect = sideeffect;
    }

    public String getPrevention() {
        return prevention;
    }

    public void setPrevention(String prevention) {
        this.prevention = prevention;
    }

    public String getCauses() {
        return causes;
    }

    public void setCauses(String causes) {
        this.causes = causes;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSubText() {
        return subText;
    }

    public void setSubText(String subText) {
        this.subText = subText;
    }

    public boolean isExpandable() {
        return expandable;
    }

    public void setExpandable(boolean expandable) {
        this.expandable = expandable;
    }
}
