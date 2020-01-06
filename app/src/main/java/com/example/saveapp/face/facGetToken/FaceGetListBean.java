package com.example.saveapp.face.facGetToken;

public class FaceGetListBean {
    private String user_id;
    private String group_id;
       public FaceGetListBean() {
    }

    public FaceGetListBean(String user_id, String group_id) {
        super();
        this.user_id = user_id;
        this.group_id = group_id;
    }

    public String getUser_id() {
        return user_id;
    }
    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
    public String getGroup_id() {
        return group_id;
    }
    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

}