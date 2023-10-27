package com.edunge.bukinz.response;

import com.edunge.bukinz.model.State;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UploadResponse extends BaseResponse{
    State state;
    List<State> states;
    Integer count;

    public Integer getCount() {
        return count;
    }

    public UploadResponse(String code, String message, State state) {
        super(code, message);
        this.state = state;
    }

    public UploadResponse(State state) {
        this.state = state;
    }

    public UploadResponse(String code, String message) {
        super(code, message);
    }

    public UploadResponse(String code, String message, List<State> states) {
        super(code, message);
        this.states = states;
        this.count = states.size();
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public List<State> getStates() {
        return states;
    }

    public void setStates(List<State> states) {
        this.states = states;
    }
}
