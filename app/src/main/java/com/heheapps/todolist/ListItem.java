package com.heheapps.todolist;

import java.io.Serializable;
import java.util.ArrayList;

class ListItem implements Serializable {
    private String itemName;
    private String description;
    private ListItem parent;
    private ArrayList<ListItem> children;
    private boolean isChecked;
    private boolean isLocked;

    public ListItem(String itemName, String description, ListItem parent) {
        this.itemName = itemName;
        this.description = description;
        this.parent = parent;

        this.isChecked = false;
        this.isLocked = false;
        children = new ArrayList<>();
    }

    public ListItem getParent() {
        return parent;
    }

    public void addChild(ListItem child){
        children.add(child);
    }

    public void removeChild(ListItem child){children.remove(child);}

    public ArrayList<ListItem> getChildren() {
        return children;
    }

    @Override
    public String toString() {
        return itemName;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setParent(ListItem parent) {
        this.parent = parent;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void changeLock() {
        isLocked = !isLocked;
    }
}
