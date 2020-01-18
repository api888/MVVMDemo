package com.codepig.common.bean;

public class Base {
    private String respCode;
    private String respMsg;

    public String getRespCode() {
        return respCode;
    }

    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }

    public String getRespMsg() {
        return respMsg + "";
    }

    public void setRespMsg(String respMsg) {
        this.respMsg = respMsg;
    }
}
