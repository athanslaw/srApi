package com.edunge.srtool.service;

import com.edunge.srtool.exceptions.NotFoundException;
import com.edunge.srtool.response.DashboardResponse;

public interface DashboardService {
    DashboardResponse getDefaultDashboard() throws NotFoundException;
    DashboardResponse getDashboardByState() throws NotFoundException;
    DashboardResponse getDashboardBySenatorialDistrict(Long senatorialDistrictId) throws NotFoundException;
    DashboardResponse getDashboardByLga(Long lgaId) throws NotFoundException;
}
