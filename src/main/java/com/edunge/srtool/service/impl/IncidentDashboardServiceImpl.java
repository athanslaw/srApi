package com.edunge.srtool.service.impl;

import com.edunge.srtool.model.Incident;
import com.edunge.srtool.model.Lga;
import com.edunge.srtool.repository.IncidentRepository;
import com.edunge.srtool.repository.LgaRepository;
import com.edunge.srtool.response.IncidentDashboardResponse;
import com.edunge.srtool.response.IncidentReport;
import com.edunge.srtool.service.IncidentDashboardService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class IncidentDashboardServiceImpl implements IncidentDashboardService {
    private final IncidentRepository incidentRepository;
    private final LgaRepository lgaRepository;
    public IncidentDashboardServiceImpl(IncidentRepository incidentRepository, LgaRepository lgaRepository) {
        this.incidentRepository = incidentRepository;
        this.lgaRepository = lgaRepository;
    }

    @Override
    public IncidentDashboardResponse getDashboardByState(Long stateId){
        List<Incident> incidentList = incidentRepository.findAll();
        Integer totalIncidents = getStateIncidents(stateId);
        List<IncidentReport> incidentReports = new ArrayList<>();
        HashMap<String, Integer> incidentTypeMap = new HashMap<>();
        incidentList.stream()
                .filter(incident -> incident.getLga().getState().getId().equals(stateId))
                .forEach(incident -> {
                    String incidentType = incident.getIncidentType().getName();
                    incidentTypeMap.put(incidentType, incidentTypeMap.get(incidentType)+1);
                });
        incidentTypeMap.forEach((type, count)->{
            Double percent = (count * 100.0)/totalIncidents;
            incidentReports.add(new IncidentReport(type, count, percent));
        });
        List<IncidentReport> lgaIncidentReport = getLgaReports(stateId);
        return new IncidentDashboardResponse("00","Incident Report loaded.", incidentReports, lgaIncidentReport);
    }

    public Integer getStateIncidents(Long stateId){
        List<Incident> incidentList = incidentRepository.findAll();
        return (int) incidentList.stream().filter(incident -> incident.getLga().getState().getId().equals(stateId)).count();
    }

    @Override
    public IncidentDashboardResponse getDashboardByLga(Long lgaId){
        List<Incident> incidentList = incidentRepository.findAll();
        Integer totalIncidents = getLgaIncidents(lgaId);
        List<IncidentReport> incidentReports = new ArrayList<>();
        HashMap<String, Integer> incidentTypeMap = new HashMap<>();
        incidentList.stream()
                .filter(incident -> incident.getLga().getId().equals(lgaId))
                .forEach(incident -> {
                    String incidentType = incident.getIncidentType().getName();
                    incidentTypeMap.put(incidentType, incidentTypeMap.get(incidentType)+1);
                });
        incidentTypeMap.forEach((type, count)->{
            Double percent = (count * 100.0)/totalIncidents;
            incidentReports.add(new IncidentReport(type, count, percent));
        });
        return new IncidentDashboardResponse("00","Incident Report loaded.", incidentReports);
    }

    public Integer getLgaIncidents(Long lgaId){
        List<Incident> incidentList = incidentRepository.findAll();
        return (int) incidentList.stream().filter(incident -> incident.getLga().getId().equals(lgaId)).count();
    }

    private List<IncidentReport> getLgaReports(Long stateId){
        List<Lga> lgas = lgaRepository.findAll();
        List<IncidentReport> incidentReports  = new ArrayList<>();

        List<Incident> incidentList = incidentRepository.findAll();
        lgas.stream().filter(lga -> lga.getState().getId().equals(stateId))
                .forEach(lga -> {
                    Integer totalIncidents = getLgaIncidents(lga.getId());
                    HashMap<String, Integer> incidentTypeMap = new HashMap<>();
                    incidentList.stream()
                            .filter(incident -> incident.getLga().getId().equals(lga.getId()))
                            .forEach(incident -> {
                                String incidentType = incident.getIncidentType().getName();
                                incidentTypeMap.put(incidentType, incidentTypeMap.get(incidentType)+1);
                            });
                    incidentTypeMap.forEach((type, count)->{
                        Double percent = (count * 100.0)/totalIncidents;
                        incidentReports.add(new IncidentReport(lga,type, count, percent));
                    });
                });
        return incidentReports;
    }
}
