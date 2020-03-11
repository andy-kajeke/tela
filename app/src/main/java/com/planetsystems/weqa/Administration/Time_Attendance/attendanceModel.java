package com.planetsystems.weqa.Administration.Time_Attendance;

public class attendanceModel {
    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    String Id;
    String name;
    String rooms;
    String subject;
    String clockIn;

    public String getClockIn() {
        return clockIn;
    }

    public void setClockIn(String clockIn) {
        this.clockIn = clockIn;
    }

    public attendanceModel(){

    }

    public attendanceModel(String Id, String name, String rooms, String subject){
        this.Id =Id;
        this.name=name;
        this.subject=subject;

        }

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }




    public String getRooms() {

        return rooms;
    }

    public void setRooms(String rooms1) {

        this.rooms = rooms1;
    }

    public String getSubject() {

        return subject;
    }

    public void setSubject(String subject1) {

        this.subject = subject1;
    }

}
