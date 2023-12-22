package com.example.groupchatapp.models;

public class GroupModels {
    private String userId, groupName, groupDescription, link, groupId;

    public GroupModels(String userId, String groupName, String groupDescription, String link, String groupId) {
        this.groupName = groupName;
        this.userId = userId;
        this.groupId = groupId;
        this.groupDescription = groupDescription;
        this.link = link;
    }

    public GroupModels() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupDescription() {
        return groupDescription;
    }

    public void setGroupDescription(String groupDescription) {
        this.groupDescription = groupDescription;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
