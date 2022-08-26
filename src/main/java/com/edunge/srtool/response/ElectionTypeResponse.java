package com.edunge.srtool.response;

import com.edunge.srtool.model.Election;
import com.edunge.srtool.model.ElectionType;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ElectionTypeResponse extends BaseResponse{
    ElectionType electionType;
    List<ElectionType> electionTypes;
    Integer count;

    public Integer getCount() {
        return count;
    }

    public ElectionTypeResponse(String code, String message) {
        super(code, message);
    }

    public ElectionTypeResponse(String code, String message, List<ElectionType> electionTypes) {
        super(code, message);
        this.count  = electionTypes.size();
        this.electionTypes = electionTypes;
    }

    public ElectionType getElectionType() {
        return electionType;
    }

    public void setElectionType(ElectionType electionType) {
        this.electionType = electionType;
    }

    public List<ElectionType> getElectionTypes() {
        return electionTypes;
    }

    public void setElectionTypes(List<ElectionType> electionTypes) {
        this.electionTypes = electionTypes;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
