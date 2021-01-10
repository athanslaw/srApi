package com.edunge.srtool.service;

import com.edunge.srtool.response.IncidentDashboardResponse;

public interface IncidentDashboardService {
    IncidentDashboardResponse getDashboardByState(Long stateId);

    IncidentDashboardResponse getDashboardByLga(Long lgaId);
}
