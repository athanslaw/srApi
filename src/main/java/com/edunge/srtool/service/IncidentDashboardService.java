package com.edunge.srtool.service;

import com.edunge.srtool.exceptions.NotFoundException;
import com.edunge.srtool.response.IncidentDashboardResponse;

public interface IncidentDashboardService {
    IncidentDashboardResponse getDashboardByState(Long stateId) throws NotFoundException;

    IncidentDashboardResponse getDashboardByLga(Long lgaId) throws NotFoundException;
}
