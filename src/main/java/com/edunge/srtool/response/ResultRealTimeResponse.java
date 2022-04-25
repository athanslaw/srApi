package com.edunge.srtool.response;

import com.edunge.srtool.model.Result;
import com.edunge.srtool.model.ResultRealTime;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResultRealTimeResponse extends BaseResponse{
    ResultRealTime result;
    List<ResultRealTime> results;
    Integer count;

    public Integer getCount() {
        return count;
    }
    public ResultRealTimeResponse(String code, String message, ResultRealTime result) {
        super(code, message);
        this.result = result;
    }

    public ResultRealTimeResponse(ResultRealTime result) {
        this.result = result;
    }

    public ResultRealTimeResponse(String code, String message) {
        super(code, message);
    }

    public ResultRealTimeResponse(String code, String message, List<ResultRealTime> results) {
        super(code, message);
        this.results = results;
        this.count = results.size();
    }

    public ResultRealTime getResult() {
        return result;
    }

    public void setResult(ResultRealTime result) {
        this.result = result;
    }

    public List<ResultRealTime> getResults() {
        return results;
    }

    public void setResults(List<ResultRealTime> results) {
        this.results = results;
    }
}
