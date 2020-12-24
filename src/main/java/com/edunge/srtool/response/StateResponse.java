package com.edunge.srtool.response;

import com.edunge.srtool.model.State;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StateResponse extends BaseResponse{
    State state;
    List<State> states;

    public StateResponse(String code, String message, State state) {
        super(code, message);
        this.state = state;
    }

    public StateResponse(State state) {
        this.state = state;
    }

    public StateResponse(String code, String message) {
        super(code, message);
    }

    public StateResponse(String code, String message, List<State> states) {
        super(code, message);
        this.states = states;
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
