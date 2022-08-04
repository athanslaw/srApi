package com.edunge.srtool.service;

import com.edunge.srtool.exceptions.NotFoundException;
import com.edunge.srtool.response.DashboardResponse;
import com.edunge.srtool.response.NationalDashboardResponse;

public interface DashboardService {
    DashboardResponse getDefaultDashboard(Long electionType) throws NotFoundException;
    DashboardResponse getDashboardByState(Long electionType) throws NotFoundException;
    DashboardResponse getDashboardByState(Long stateId, Long electionType) throws NotFoundException;
    NationalDashboardResponse getDashboardByCountry(Long electionType) throws NotFoundException;
    NationalDashboardResponse getDashboardByZone(Long id, Long electionType) throws NotFoundException;
    DashboardResponse getDashboardBySenatorialDistrict(Long senatorialDistrictId, Long electionType) throws NotFoundException;
    DashboardResponse getDashboardByLga(Long lgaId, Long electionType) throws NotFoundException;

}
