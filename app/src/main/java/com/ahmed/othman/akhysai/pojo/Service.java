package com.ahmed.othman.akhysai.pojo;

public class Service {
    private int id;
    private String specialist_id;
    private String price;
    private String is_active;
    private Language ar;
    private Language en;

    public Service(int id, String specialist_id, String price, String is_active, Language ar, Language en) {
        this.id = id;
        this.specialist_id = specialist_id;
        this.price = price;
        this.is_active = is_active;
        this.ar = ar;
        this.en = en;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSpecialist_id() {
        return specialist_id;
    }

    public void setSpecialist_id(String specialist_id) {
        this.specialist_id = specialist_id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getIs_active() {
        return is_active;
    }

    public void setIs_active(String is_active) {
        this.is_active = is_active;
    }

    public Language getAr() {
        return ar;
    }

    public void setAr(Language ar) {
        this.ar = ar;
    }

    public Language getEn() {
        return en;
    }

    public void setEn(Language en) {
        this.en = en;
    }

    public class Language {
        private int id;
        private String service_id;
        private String language;
        private String name;

        public Language(int id, String service_id, String language, String name) {
            this.id = id;
            this.service_id = service_id;
            this.language = language;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getService_id() {
            return service_id;
        }

        public void setService_id(String service_id) {
            this.service_id = service_id;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

}
