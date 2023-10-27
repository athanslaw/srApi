package com.edunge.bukinz.response;

import com.edunge.bukinz.model.Config;
import com.fasterxml.jackson.annotation.JsonInclude;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConfigResponse extends BaseResponse {
    private Config config;

    public ConfigResponse(String code, String message, Config config) {
        super(code,message);
        this.config = config;
    }

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }
}
