package com.edunge.srtool.service.impl;

import com.edunge.srtool.exceptions.NotFoundException;
import com.edunge.srtool.model.Incident;
import com.edunge.srtool.model.Lga;
import com.edunge.srtool.model.State;
import com.edunge.srtool.repository.IncidentRepository;
import com.edunge.srtool.repository.LgaRepository;
import com.edunge.srtool.repository.StateRepository;
import com.edunge.srtool.response.IncidentDashboardResponse;
import com.edunge.srtool.response.IncidentReport;
import com.edunge.srtool.service.IncidentDashboardService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class IncidentDashboardServiceImpl implements IncidentDashboardService {
    private final IncidentRepository incidentRepository;
    private final LgaRepository lgaRepository;
    private final StateRepository stateRepository;
    public IncidentDashboardServiceImpl(IncidentRepository incidentRepository, LgaRepository lgaRepository, StateRepository stateRepository) {
        this.incidentRepository = incidentRepository;
        this.lgaRepository = lgaRepository;
        this.stateRepository = stateRepository;
    }

    @Override
    public IncidentDashboardResponse getDashboardByState(Long stateId) throws NotFoundException {
        getState(stateId);
        List<Incident> incidentList = incidentRepository.findAll();
        Integer totalIncidents = getStateIncidents(stateId);
        List<IncidentReport> incidentReports = getIncidentReport(stateId);
        List<IncidentReport> lgaIncidentReport = getLgaReports(stateId);

        return new IncidentDashboardResponse("00","Incident Report loaded.",totalIncidents, incidentReports, lgaIncidentReport);
    }

    public Integer getStateIncidents(Long stateId){
        List<Incident> incidentList = incidentRepository.findAll();
        return (int) incidentList.stream().filter(incident -> incident.getLga().getState().getId().equals(stateId)).count();
    }

    @Override
    public IncidentDashboardResponse getDashboardByLga(Long lgaId) throws NotFoundException {
        Lga lga = getLga(lgaId);
        List<Incident> incidentList = incidentRepository.findAll();
        Integer totalIncidents = getLgaIncidents(lga.getId());
        List<IncidentReport> incidentReports = new ArrayList<>();
        HashMap<String, Integer> incidentTypeMap = new HashMap<>();
        incidentList.stream()
                .filter(incident -> incident.getLga().getId().equals(lga.getId()))
                .forEach(incident -> {
                    String incidentType = incident.getIncidentType().getName();
                    Integer currentValue = incidentTypeMap.getOrDefault(incidentType, 0);
                    incidentTypeMap.put(incidentType, currentValue+1);
                });
        incidentTypeMap.forEach((type, count)->{
            Double percent = (count * 100.0)/totalIncidents;
            incidentReports.add(new IncidentReport(lga,type, count, percent));
        });
        return new IncidentDashboardResponse("00","Incident Report loaded.", incidentReports);
    }

    public Integer getLgaIncidents(Long lgaId){
        List<Incident> incidentList = incidentRepository.findAll();
        return (int) incidentList.stream().filter(incident -> incident.getLga().getId().equals(lgaId)).count();
    }

    private List<IncidentReport> getIncidentReport(Long stateId){
        List<Incident> incidentList = incidentRepository.findAll();
        HashMap<String, Integer> incidentTypeMap = new HashMap<>();
        List<IncidentReport> incidentReports = new ArrayList<>();
        incidentList.stream()
                .filter(incident -> incident.getLga().getState().getId().equals(stateId))
                .forEach(incident -> {
                    String incidentType = incident.getIncidentType().getName();
                    Integer currentValue = incidentTypeMap.getOrDefault(incidentType, 0);
                    incidentTypeMap.put(incidentType, currentValue+1);
                });
        Integer totalIncident = getStateIncidents(stateId);
        incidentTypeMap.forEach((type, count)->{
            Double percent = (count * 100.0)/totalIncident;
            incidentReports.add(new IncidentReport(type, count, percent));
        });
        return incidentReports;
    }

    private List<IncidentReport> getLgaReports(Long stateId){

        List<Lga> lgas = lgaRepository.findAll();
        List<IncidentReport> incidentReports  = new ArrayList<>();

        List<Incident> incidentList = incidentRepository.findAll();
        lgas.stream().filter(lga -> lga.getState().getId().equals(stateId))
                .forEach(lga -> {
                    Integer totalIncidents = getLgaIncidents(lga.getId());
                    HashMap<String, Integer> incidentTypeMap = new HashMap<>();
                    AtomicInteger totalIncident = new AtomicInteger(0);
                    incidentList.stream()
                            .filter(incident -> incident.getLga().getId().equals(lga.getId()))
                            .forEach(incident -> {
                                String incidentType = incident.getIncidentType().getName();
                                Integer currentValue = incidentTypeMap.getOrDefault(incidentType, 0);
                                incidentTypeMap.put(incidentType, currentValue+1);
                            });
                    totalIncident.addAndGet((int) incidentList.stream()
                            .filter(incident -> incident.getLga().getId().equals(lga.getId()))
                            .filter(incident -> incident.getIncidentStatus().getName().equals("Unresolved"))
                            .count());
                    incidentTypeMap.forEach((type, count)->{
                        Double percent = (count * 100.0)/totalIncidents;
                        incidentReports.add(new IncidentReport(lga,type, count, percent, totalIncident.get()));
                    });
                });
        return incidentReports;
    }

    private Lga getLga(Long id) throws NotFoundException {
        Optional<Lga> currentLga = lgaRepository.findById(id);
        if(!currentLga.isPresent()){
            throw new NotFoundException(String.format("%s not found.", "Lga"));
        }
        return currentLga.get();
    }

    private State getState(Long id) throws NotFoundException {
        Optional<State> currentState = stateRepository.findById(id);
        if(!currentState.isPresent()){
            throw new NotFoundException(String.format("%s not found.", "State"));
        }
        return currentState.get();
    }
}
