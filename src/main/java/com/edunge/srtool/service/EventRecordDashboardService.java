package com.edunge.srtool.service;

import com.edunge.srtool.exceptions.NotFoundException;
import com.edunge.srtool.response.IncidentDashboardResponse;

public interface EventRecordDashboardService {
    IncidentDashboardResponse getDashboardByState() throws NotFoundException;
    IncidentDashboardResponse getDashboardByState(Long stateId) throws NotFoundException;

    IncidentDashboardResponse getDashboardBySenatorialDistrict(Long lgaId) throws NotFoundException;
    IncidentDashboardResponse getDashboardByLga(Long lgaId) throws NotFoundException;
}
