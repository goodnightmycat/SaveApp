package com.example.saveapp.face.RealManFaceCheck;

public class FaceVerifyBean {
    private String image;
    private String image_type;
    private String face_field;
    private String option;
    public FaceVerifyBean(){ }
    public FaceVerifyBean(String image, String image_type){
        super();
        this.image=image;
        this.image_type=image_type;
    }
    public FaceVerifyBean(String image, String image_type, String face_field, String option){
        super();
        this.image=image;
        this.image_type=image_type;
        this.face_field=face_field;
        this.option=option;
    }

    public String getFace_field() {
        return face_field;
    }

    public void setFace_field(String face_field) {
        this.face_field = face_field;
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

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }
}
