package com.edunge.srtool.response;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventRecordDashboardResponse {
    private long positiveResponse;
    private long negativeResponse;
    private long noResponse;

    public EventRecordDashboardResponse() {
    }

    public EventRecordDashboardResponse(long positiveResponse, long negativeResponse, long noResponse) {
        this.positiveResponse = positiveResponse;
        this.negativeResponse = negativeResponse;
        this.noResponse = noResponse;
    }

    public long getPositiveResponse() {
        return positiveResponse;
    }

    public void setPositiveResponse(long positiveResponse) {
        this.positiveResponse = positiveResponse;
    }

    public long getNegativeResponse() {
        return negativeResponse;
    }

    public void setNegativeResponse(long negativeResponse) {
        this.negativeResponse = negativeResponse;
    }

    public long getNoResponse() {
        return noResponse;
    }

    public void setNoResponse(long noResponse) {
        this.noResponse = noResponse;
    }
}
