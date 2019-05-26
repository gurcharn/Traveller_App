package com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.chat;

import java.util.List;

public class Chat {
    private String chatId;
    private String userOne;
    private String userTwo;
    private List<Message> messages;

    public Chat(String chatId, String userOne, String userTwo, List<Message> messages) {
        this.chatId = chatId;
        this.userOne = userOne;
        this.userTwo = userTwo;
        this.messages = messages;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getUserOne() {
        return userOne;
    }

    public void setUserOne(String userOne) {
        this.userOne = userOne;
    }

    public String getUserTwo() {
        return userTwo;
    }

    public void setUserTwo(String userTwo) {
        this.userTwo = userTwo;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    @Override
    public boolean equals (Object o) {
        if(this==null && o==null) return true;
        if(this!=null && o==null) return false;
        if(this==null && o!=null) return false;
        if(!(o instanceof Chat)) return false;
        Chat chat=(Chat) o;
        return (this.getChatId()==chat.getChatId() && this.getUserOne()==chat.getUserOne() && this.getUserTwo()==chat.getUserTwo());
    }

    /**
     * Method to get hash of object
     * @return int
     */
    @Override
    public int hashCode() {
        int result;
        result = (chatId != null ? chatId.hashCode() : 0);
        result = 31 * result + (userOne != null ? userOne.hashCode() : 0);
        result = 31 * result + (userTwo != null ? userTwo.hashCode() : 0);
        return result;
    }
}
