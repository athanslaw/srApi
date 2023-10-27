package com.edunge.bukinz.service;

import com.edunge.bukinz.exceptions.NotFoundException;
import com.edunge.bukinz.response.DashboardResponse;

public interface DashboardService {
    DashboardResponse getDefaultDashboard(Long electionType) throws NotFoundException;
    DashboardResponse getDashboardByState(Long electionType) throws NotFoundException;
    DashboardResponse getDashboardByState(Long stateId, Long electionType) throws NotFoundException;
    DashboardResponse getDashboardBySenatorialDistrict(Long senatorialDistrictId, Long electionType) throws NotFoundException;
    DashboardResponse getDashboardByLga(Long lgaId, Long electionType) throws NotFoundException;

}
