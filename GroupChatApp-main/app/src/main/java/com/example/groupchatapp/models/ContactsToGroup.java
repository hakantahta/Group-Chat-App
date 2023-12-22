package com.example.groupchatapp.models;

public class ContactsToGroup {

    private String userId, groupId, contactsPhoneNumber;

    public ContactsToGroup(String userId, String groupId, String contactsPhoneNumber) {
        this.userId = userId;
        this.groupId = groupId;
        this.contactsPhoneNumber = contactsPhoneNumber;
    }

    public ContactsToGroup() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getContactsPhone() {
        return contactsPhoneNumber;
    }

    public void setContactsPhone(String contactsPhoneNumber) {
        this.contactsPhoneNumber = contactsPhoneNumber;
    }
}
