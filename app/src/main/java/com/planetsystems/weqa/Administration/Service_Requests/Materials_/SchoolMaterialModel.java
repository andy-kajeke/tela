package com.planetsystems.weqa.Administration.Service_Requests.Materials_;

public class SchoolMaterialModel {

    String db_id;
    String employeeId;
    String employeeRequestType;
    String itemId;
    String itemName;
    String quantity;
    String requestDate;
    String deploymentSiteId;
    String employee;
    String sm_comment;
    String dateRequired;
    String confirmation;
    String approvalDate;
    String approvalStatus;

    public String getDateRequired() {
        return dateRequired;
    }

    public void setDateRequired(String dateRequired) {
        this.dateRequired = dateRequired;
    }

    public String getDb_id() {
        return db_id;
    }

    public void setDb_id(String db_id) {
        this.db_id = db_id;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeRequestType() {
        return employeeRequestType;
    }

    public void setEmployeeRequestType(String employeeRequestType) {
        this.employeeRequestType = employeeRequestType;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }

    public String getDeploymentSiteId() {
        return deploymentSiteId;
    }

    public void setDeploymentSiteId(String deploymentSiteId) {
        this.deploymentSiteId = deploymentSiteId;
    }

    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }

    public String getSm_comment() {
        return sm_comment;
    }

    public void setSm_comment(String sm_comment) {
        this.sm_comment = sm_comment;
    }

    public String getConfirmation() {
        return confirmation;
    }

    public void setConfirmation(String confirmation) {
        this.confirmation = confirmation;
    }

    public String getApprovalDate() {
        return approvalDate;
    }

    public void setApprovalDate(String approvalDate) {
        this.approvalDate = approvalDate;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }
}
