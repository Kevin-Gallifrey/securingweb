package com.example.securingweb.entity;

public class LogInfo {
    private long id;
    private String time;
    private String ipaddr;
    private String user;
    private String event;
    private String status;

    public LogInfo() {
    }

    public LogInfo(String time, String ipaddr, String user, String event, String status) {
        // this.id = id;
        this.time = time;
        this.ipaddr = ipaddr;
        this.user = user;
        this.event = event;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getIpaddr() {
        return ipaddr;
    }

    public void setIpaddr(String ipaddr) {
        this.ipaddr = ipaddr;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String toString() {
        return String.format("[id=%d, time=%s, ipaddr=%s, user=%s, event=%s, status=%s]", 
        this.id, this.time, this.ipaddr, this.user, this.event, this.status);
    }

}
