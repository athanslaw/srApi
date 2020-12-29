package com.edunge.srtool.response;

import com.edunge.srtool.model.PartyAgent;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PartyAgentResponse extends BaseResponse{
    PartyAgent partyAgent;
    List<PartyAgent> partyAgents;

    public PartyAgentResponse(String code, String message, PartyAgent partyAgent) {
        super(code, message);
        this.partyAgent = partyAgent;
    }

    public PartyAgentResponse(PartyAgent partyAgent) {
        this.partyAgent = partyAgent;
    }

    public PartyAgentResponse(String code, String message) {
        super(code, message);
    }

    public PartyAgentResponse(String code, String message, List<PartyAgent> partyAgents) {
        super(code, message);
        this.partyAgents = partyAgents;
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
}
