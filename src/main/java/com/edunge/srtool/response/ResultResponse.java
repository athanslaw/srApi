package com.edunge.srtool.response;

import com.edunge.srtool.model.Result;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResultResponse extends BaseResponse{
    Result result;
    List<Result> results;

    public ResultResponse(String code, String message, Result result) {
        super(code, message);
        this.result = result;
    }

    public ResultResponse(Result result) {
        this.result = result;
    }

    public ResultResponse(String code, String message) {
        super(code, message);
    }

    public ResultResponse(String code, String message, List<Result> results) {
        super(code, message);
        this.results = results;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }
}
