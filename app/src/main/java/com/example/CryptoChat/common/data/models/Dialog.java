package com.example.CryptoChat.common.data.models;

import android.util.Log;

import com.example.CryptoChat.common.data.exceptions.ObjectNotExistException;
import com.example.CryptoChat.common.data.fake.FakeContactProvider;
import com.example.CryptoChat.common.data.provider.SQLiteMessageProvider;
import com.stfalcon.chatkit.commons.models.IDialog;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Transient;

import java.util.ArrayList;
import java.util.UUID;

@Entity
public class Dialog implements IDialog<Message> {

    @Id(autoincrement = true)
    private Long pk;

    @NotNull
    private String id;
    private String lastMessageId;
    private String dialogPhoto;
    private String dialogName;
    private String receiverId;
    private int unreadCount;

    @Transient
    private ArrayList<User> users;
    @Transient
    private Message lastMessage;

    public Dialog(String id, String name, String photo,
                  ArrayList<User> users, Message lastMessage, int unreadCount) {

        this.id = id;
        this.dialogName = name;
        this.dialogPhoto = photo;
        this.users = users;
        this.lastMessage = lastMessage;
        this.unreadCount = unreadCount;
        this.receiverId = users.get(0).getId();
    }

    public Dialog(String name, String photo,
                  String receiverId, Message lastMessage, int unreadCount) {

        this.id = UUID.randomUUID().toString();
        this.dialogName = name;
        this.dialogPhoto = photo;
        this.users = new ArrayList<>();
        this.lastMessage = lastMessage;
        this.unreadCount = unreadCount;
        this.receiverId = receiverId;
        this.lastMessageId = lastMessage.getId();
    }

    @Generated(hash = 869846398)
    public Dialog(Long pk, @NotNull String id, String lastMessageId, String dialogPhoto,
                  String dialogName, String receiverId, int unreadCount) {
        this.pk = pk;
        this.id = id;
        this.lastMessageId = lastMessageId;
        this.dialogPhoto = dialogPhoto;
        this.dialogName = dialogName;
        this.receiverId = receiverId;
        this.unreadCount = unreadCount;
    }

    @Generated(hash = 679371034)
    public Dialog() {
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getDialogPhoto() {
        return dialogPhoto;
    }

    @Override
    public String getDialogName() {
        return dialogName;
    }

    @Override
    public ArrayList<User> getUsers() {

        ArrayList<User> al = new ArrayList<>();
        try {
            al.add(FakeContactProvider.getInstance().getUser(receiverId));
        } catch (ObjectNotExistException e) {
            Log.e("Dialog", "User not exist");
        }
        return al;

    }

    @Override
    public Message getLastMessage() {
        return SQLiteMessageProvider.getInstance(null).getMessageById(this.lastMessageId);
    }

    @Override
    public void setLastMessage(Message lastMessage) {
        this.lastMessage = lastMessage;
        this.setLastMessageId(lastMessage.getId());
    }

    public String getLastMessageId() {
        return this.lastMessageId;
    }

    @Override
    public int getUnreadCount() {
        return unreadCount;
    }

    public String getReceiverId() {
        return this.receiverId;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }

    public void setId(String id) {
        this.id = id;
    }


    public void setLastMessageId(String lastMessageId) {
        this.lastMessageId = lastMessageId;
    }

    public void setDialogPhoto(String dialogPhoto) {
        this.dialogPhoto = dialogPhoto;
    }

    public void setDialogName(String dialogName) {
        this.dialogName = dialogName;
    }

    public void setReceLongiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public Long getPk() {
        return this.pk;
    }

    public void setPk(long pk) {
        this.pk = pk;
    }

    public void setPk(Long pk) {
        this.pk = pk;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }
}