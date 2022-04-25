package com.edunge.srtool.service;

import com.edunge.srtool.exceptions.NotFoundException;
import com.edunge.srtool.response.IncidentDashboardResponse;

public interface IncidentDashboardService {
    IncidentDashboardResponse getDashboardByState() throws NotFoundException;

    IncidentDashboardResponse getDashboardBySenatorialDistrict(Long lgaId) throws NotFoundException;
    IncidentDashboardResponse getDashboardByLga(Long lgaId) throws NotFoundException;
}
