package com.edunge.bukinz.response;

import com.edunge.bukinz.model.AbstractBaseModel;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseTemplate extends BaseResponse {
    AbstractBaseModel baseModel;
    List<AbstractBaseModel> entityObjects;

    public ResponseTemplate(String code, String message, AbstractBaseModel baseModel) {
        super(code, message);
        this.baseModel = baseModel;
    }

    public ResponseTemplate(AbstractBaseModel abstractBaseModel) {
        this.baseModel = abstractBaseModel;
    }

    public ResponseTemplate(String code, String message) {
        super(code, message);
    }

    public ResponseTemplate(String code, String message, List<AbstractBaseModel> objects) {
        super(code, message);
        this.entityObjects = objects;
    }

    public AbstractBaseModel getBaseModel() {
        return baseModel;
    }

    public void setBaseModel(AbstractBaseModel abstractBaseModel) {
        this.baseModel = abstractBaseModel;
    }

    public List<AbstractBaseModel> getPoliticalParties() {
        return entityObjects;
    }

    public void setPoliticalParties(List<AbstractBaseModel> entityObjects) {
        this.entityObjects = entityObjects;
    }
}
