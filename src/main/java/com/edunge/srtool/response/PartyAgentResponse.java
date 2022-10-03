package com.edunge.srtool.response;

import com.edunge.srtool.dto.PartyAgentDto;
import com.edunge.srtool.model.PartyAgent;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PartyAgentResponse extends BaseResponse{
    PartyAgent partyAgent;
    List<PartyAgent> partyAgents;
    List<PartyAgentDto> partyAgentDtoList;
    PartyAgentDto partyAgentDto;
    Integer count;

    public Integer getCount() {
        return count;
    }
    public PartyAgentResponse(String code, String message, PartyAgent partyAgent) {
        super(code, message);
        this.partyAgent = partyAgent;
    }

    public PartyAgentResponse(String code, String message, List<PartyAgentDto> partyAgent) {
        super(code, message);
        this.partyAgentDtoList = partyAgent;
    }

    public PartyAgentResponse(String code, String message, PartyAgentDto partyAgentDto) {
        super(code, message);
        this.partyAgentDto = partyAgentDto;
    }

    public PartyAgentResponse(PartyAgent partyAgent) {
        this.partyAgent = partyAgent;
    }

    public PartyAgentResponse(String code, String message) {
        super(code, message);
    }

    public PartyAgent getPartyAgent() {
        return partyAgent;
    }

    public void setPartyAgent(PartyAgent partyAgent) {
        this.partyAgent = partyAgent;
    }

    public List<PartyAgent> getPartyAgents() {
        return partyAgents;
    }

    public void setPartyAgents(List<PartyAgent> partyAgents) {
        this.partyAgents = partyAgents;
    }

    public List<PartyAgentDto> getPartyAgentDtoList() {
        return partyAgentDtoList;
    }

    public void setPartyAgentDtoList(List<PartyAgentDto> partyAgentDtoList) {
        this.partyAgentDtoList = partyAgentDtoList;
    }

    public PartyAgentDto getPartyAgentDto() {
        return partyAgentDto;
    }

    public void setPartyAgentDto(PartyAgentDto partyAgentDto) {
        this.partyAgentDto = partyAgentDto;
    }
}
