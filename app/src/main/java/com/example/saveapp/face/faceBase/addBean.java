package com.example.saveapp.face.faceBase;

public class addBean {
    private String image;
    private String image_type;
    private String group_id;
    private String user_id;
    private String user_info;
    private String quality_control;
    private String liveness_control;
    private String action_type;
    public addBean() {
    }

    public addBean(String image, String image_type, String group_id,
                   String user_id) {
        super();
        this.image = image;
        this.image_type = image_type;
        this.group_id=group_id;
        this.user_id=user_id;

    }

    public addBean(String image, String image_type, String group_id,
                   String user_id, String user_info, String quality_control,
                   String liveness_control, String action_type) {
        super();
        this.image = image;
        this.image_type = image_type;
        this.group_id = group_id;
        this.user_id = user_id;
        this.user_info = user_info;
        this.quality_control = quality_control;
        this.liveness_control = liveness_control;
        this.action_type = action_type;
    }
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public String getImage_type() {
        return image_type;
    }
    public void setImage_type(String image_type) {
        this.image_type = image_type;
    }

    public String getgroup_id() {
        return group_id;
    }
    public void setgroup_id(String group_id) {
        this.group_id = group_id;
    }
    public String getuser_id() {
        return user_id;
    }
    public void setuser_id(String user_id) {
        this.user_id = user_id;
    }
    public String getuser_info() {
        return user_info;
    }
    public void setuser_info(String user_info) {
        this.user_info = user_info;
    }

    public String getQuality_control() {
        return quality_control;
    }
    public void setQuality_control(String quality_control) {
        this.quality_control = quality_control;
    }
    public String getLiveness_control() {
        return liveness_control;
    }
    public void setLiveness_control(String liveness_control) {
        this.liveness_control = liveness_control;
    }

    public String getAction_type() {
        return action_type;
    }
    public void setFace_type(String action_type) {
        this.action_type = action_type;
    }
}