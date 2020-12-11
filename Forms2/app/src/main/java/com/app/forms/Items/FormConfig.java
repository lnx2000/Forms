package com.app.forms.Items;

public class FormConfig {
    boolean acceptingResponses;
    boolean loginToSubmit;
    boolean allowMultipleResponses;
    boolean allowEdit;
    boolean recordEmail;
    boolean shuffle;
    boolean sendResponseCopy;
    boolean publish;
    boolean unPublish;
    String publishDate;
    String unPublishDate;
    String publishTime;
    String unPublishTime;

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public String getUnPublishTime() {
        return unPublishTime;
    }

    public void setUnPublishTime(String unPublishTime) {
        this.unPublishTime = unPublishTime;
    }

    public boolean isAllowEdit() {
        return allowEdit;
    }

    public void setAllowEdit(boolean allowEdit) {
        this.allowEdit = allowEdit;
    }

    public boolean isAcceptingResponses() {
        return acceptingResponses;
    }

    public void setAcceptingResponses(boolean acceptingResponses) {
        this.acceptingResponses = acceptingResponses;
    }

    public boolean isLoginToSubmit() {
        return loginToSubmit;
    }

    public void setLoginToSubmit(boolean loginToSubmit) {
        this.loginToSubmit = loginToSubmit;
    }

    public boolean isAllowMultipleResponses() {
        return allowMultipleResponses;
    }

    public void setAllowMultipleResponses(boolean allowMultipleResponses) {
        this.allowMultipleResponses = allowMultipleResponses;
    }

    public boolean isRecordEmail() {
        return recordEmail;
    }

    public void setRecordEmail(boolean recordEmail) {
        this.recordEmail = recordEmail;
    }

    public boolean isShuffle() {
        return shuffle;
    }

    public void setShuffle(boolean shuffle) {
        this.shuffle = shuffle;
    }

    public boolean isSendResponseCopy() {
        return sendResponseCopy;
    }

    public void setSendResponseCopy(boolean sendResponseCopy) {
        this.sendResponseCopy = sendResponseCopy;
    }

    public boolean isPublish() {
        return publish;
    }

    public void setPublish(boolean publish) {
        this.publish = publish;
    }

    public boolean isUnPublish() {
        return unPublish;
    }

    public void setUnPublish(boolean unPublish) {
        this.unPublish = unPublish;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public String getUnPublishDate() {
        return unPublishDate;
    }

    public void setUnPublishDate(String unPublishDate) {
        this.unPublishDate = unPublishDate;
    }
}
