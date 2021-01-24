package com.ahmed.othman.akhysai.pojo;

public class Service {
    private int id;
    private String specialist_id;
    private String price;
    private String is_active;
    private Ar ar;
    private En en;


    private class Ar{
        private int id;
        private String service_id;
        private String language;
        private String name;

        public Ar(int id, String service_id, String language, String name) {
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
    private class En{
        private int id;
        private String service_id;
        private String language;
        private String name;

        public En(int id, String service_id, String language, String name) {
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
