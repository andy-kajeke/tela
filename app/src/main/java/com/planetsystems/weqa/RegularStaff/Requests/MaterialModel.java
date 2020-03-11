package com.planetsystems.weqa.RegularStaff.Requests;

public class MaterialModel {
    String itemName;
    String type;
    String code;
    String id;

    public String getItemCode() {
        return code;
    }

    public void setItemCode(String code) {
        this.code = code;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemType() {
        return type;
    }

    public void setItemType(String type) {
        this.type = type;
    }

    public String getItemId() {
        return id;
    }

    public void setItemId(String id) {
        this.id = id;
    }
}
