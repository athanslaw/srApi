package com.edunge.srtool.service;

import com.edunge.srtool.exceptions.NotFoundException;
import com.edunge.srtool.response.EventRecordDashboardResponse;

public interface EventRecordDashboardService {
    EventRecordDashboardResponse getDashboardByState(String eventId) throws NotFoundException;
    EventRecordDashboardResponse getDashboardByState(Long stateId, String eventId) throws NotFoundException;

    EventRecordDashboardResponse getDashboardBySenatorialDistrict(Long lgaId, String eventId) throws NotFoundException;
    EventRecordDashboardResponse getDashboardByLga(Long lgaId, String eventId) throws NotFoundException;
    EventRecordDashboardResponse getDashboardByWard(Long lgaId, String eventId) throws NotFoundException;
}
