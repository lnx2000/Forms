package com.app.forms.items;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class Response {

    @Nullable
    ArrayList<ItemResponse> responses;
    @Nullable
    String email;

    public Response() {
        responses = new ArrayList<>();
        email = null;
    }

    public ArrayList<ItemResponse> getResponses() {
        return responses;
    }

    public void setResponses(ArrayList<ItemResponse> responses) {
        this.responses = responses;
    }

    @Nullable
    public String getEmail() {
        return email;
    }

    public void setEmail(@Nullable String email) {
        this.email = email;
    }
}
