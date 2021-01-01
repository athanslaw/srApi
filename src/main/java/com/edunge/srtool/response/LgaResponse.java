package com.edunge.srtool.response;

import com.edunge.srtool.model.Lga;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class LgaResponse extends BaseResponse{
    Lga lga;
    List<Lga> lgas;
    Integer count;

    public Integer getCount() {
        return count;
    }

    public LgaResponse(String code, String message, Lga lga) {
        super(code, message);
        this.lga = lga;
    }

    public LgaResponse(Lga lga) {
        this.lga = lga;
    }

    public LgaResponse(String code, String message) {
        super(code, message);
    }

    public LgaResponse(String code, String message, List<Lga> lgas) {
        super(code, message);
        this.lgas = lgas;
        this.count = lgas.size();
    }

    public Lga getLga() {
        return lga;
    }

    public void setLga(Lga lga) {
        this.lga = lga;
    }

    public List<Lga> getLgas() {
        return lgas;
    }

    public void setLgas(List<Lga> lgas) {
        this.lgas = lgas;
    }
}
