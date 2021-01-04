package com.edunge.srtool.service;

import com.edunge.srtool.exceptions.NotFoundException;
import com.edunge.srtool.response.DashboardResponse;

public interface DashboardService {
    DashboardResponse getDefaultDashboard();
    DashboardResponse getDashboardByState(Long stateId) throws NotFoundException;
    DashboardResponse getDashboardBySenatorialDistrict(Long senatorialDistrictId) throws NotFoundException;
}
