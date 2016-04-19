package com.example.millner.barcodescanningapp;

/**
 * Class used for creating inventory item objects
 * Containing descriptions, tags, buildings, and room numbers
 */
public class InventoryItem {
    private String description, building, tag;
    private int roomNumber;

    public InventoryItem() {
    }

    public InventoryItem(String description, String building, String tag, int roomNumber) {
        this.description = description;
        this.building = building;
        this.tag = tag;
        this.roomNumber = roomNumber;
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building=building;
    }

    public String getTag() {
        return tag;
    }

    public void setTag (String tag) {
        this.tag=tag;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber (int roomNumber) {
        this.roomNumber = roomNumber;
    }
}
