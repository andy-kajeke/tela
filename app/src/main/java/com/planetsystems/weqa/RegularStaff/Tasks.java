package com.planetsystems.weqa.RegularStaff;

public class Tasks {
    String startTime;
    String endTime;
    String taskDescription;
    String id;
    String inTime;
    String extraTime;
    String status;
    String comment;
    String taughtNotTaught;

    public String getTaughtNotTaught() {
        return taughtNotTaught;
    }

    public void setTaughtNotTaught(String taughtNotTaught) {
        this.taughtNotTaught = taughtNotTaught;
    }

    public String getDeploymentSite_id() {
        return deploymentSite_id;
    }

    public String setDeploymentSite_id(String deploymentSite_id) {
        this.deploymentSite_id = deploymentSite_id;
        return deploymentSite_id;
    }

    String deploymentSite_id;

    public String getInTime() {
        return inTime;
    }

    public void setInTime(String inTime) {
        this.inTime = inTime;
    }

    public String getExtraTime() {
        return extraTime;
    }

    public void setExtraTime(String extraTime) {
        this.extraTime = extraTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public String setStatus(String status) {
        this.status = status;
        return status;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }



    public Tasks(){

    }

    public Tasks(String startTime, String endTime, String taskDescription, String id, String status,
                 String comment){
        this.startTime =startTime;
        this.endTime=endTime;
        this.taskDescription=taskDescription;
        this.id=id;

    }

    public String getStartTime() {
        return startTime;
    }

    public String setStartTime(String startTime) {
        this.startTime = startTime;
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String setEndTime(String endTime) {
        this.endTime = endTime;
        return endTime;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public String setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
        return taskDescription;
    }



}
