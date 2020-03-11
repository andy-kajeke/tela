package com.planetsystems.weqa.Administration.School_Updates.Edit_TimeTable;

public class TimetableModel {
    String startTime;
    String endTime;
    String taskDescription;
    String id;
    String empName;
    String deploymentSite_id;
    String task_id;

    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getDeploymentSite_id() {
        return deploymentSite_id;
    }

    public String setDeploymentSite_id(String deploymentSite_id) {
        this.deploymentSite_id = deploymentSite_id;
        return deploymentSite_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public TimetableModel(){

    }

    public TimetableModel(String startTime, String endTime, String taskDescription, String id, String status,
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
