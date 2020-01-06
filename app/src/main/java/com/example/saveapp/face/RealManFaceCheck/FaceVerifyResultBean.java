package com.example.saveapp.face.RealManFaceCheck;

public class FaceVerifyResultBean {
    private int error_code;
    private Result result;
    public int getError_code() {
        return error_code;
    }
    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public static class Result{
        private float face_liveness;
        public float getFace_liveness() {
            return face_liveness;
        }
        public void setFace_liveness(float face_liveness) {
            this.face_liveness = face_liveness;
        }

    }
 }
