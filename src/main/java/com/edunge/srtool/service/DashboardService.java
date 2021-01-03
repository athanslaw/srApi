package com.edunge.srtool.service;

import com.edunge.srtool.exceptions.NotFoundException;
import com.edunge.srtool.response.DashboardResponse;

public interface DashboardService {
    public DashboardResponse getDefaultDashboard();
    public DashboardResponse getDashboardByState(Long stateId) throws NotFoundException;
}
