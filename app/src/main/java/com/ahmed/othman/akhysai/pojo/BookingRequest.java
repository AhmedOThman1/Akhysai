package com.ahmed.othman.akhysai.pojo;

public class BookingRequest {
    private String akhysai_id;
    private String name;
    private String code;
    private String address_in_details;
    private String notes;
    private String phone;
    private boolean management_approval;
    private long date_of_request;
    private long start_time;
    private long end_time;

    public BookingRequest(String name, String code, String address_in_details, String notes, String phone, boolean management_approval, long date_of_request, long start_time, long end_time) {
        this.name = name;
        this.code = code;
        this.address_in_details = address_in_details;
        this.notes = notes;
        this.phone = phone;
        this.management_approval = management_approval;
        this.date_of_request = date_of_request;
        this.start_time = start_time;
        this.end_time = end_time;
    }

    public String getAkhysai_id() {
        return akhysai_id;
    }

    public void setAkhysai_id(String akhysai_id) {
        this.akhysai_id = akhysai_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAddress_in_details() {
        return address_in_details;
    }

    public void setAddress_in_details(String address_in_details) {
        this.address_in_details = address_in_details;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isManagement_approval() {
        return management_approval;
    }

    public void setManagement_approval(boolean management_approval) {
        this.management_approval = management_approval;
    }

    public long getDate_of_request() {
        return date_of_request;
    }

    public void setDate_of_request(long date_of_request) {
        this.date_of_request = date_of_request;
    }

    public long getStart_time() {
        return start_time;
    }

    public void setStart_time(long start_time) {
        this.start_time = start_time;
    }

    public long getEnd_time() {
        return end_time;
    }

    public void setEnd_time(long end_time) {
        this.end_time = end_time;
    }
}
