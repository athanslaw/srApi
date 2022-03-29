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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

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
        State state =getState(stateId);
        Integer totalIncidents = getStateIncidentsCount(state);
        if(totalIncidents == 0) {
            new IncidentDashboardResponse("00","Incident Report loaded.",totalIncidents, null, null);
        }
        List<IncidentReport> incidentReports = getIncidentReport(state);
        List<IncidentReport> lgaIncidentReport = getLgaReports(state);

        return new IncidentDashboardResponse("00","Incident Report loaded.",totalIncidents, incidentReports, lgaIncidentReport);
    }

    public Integer getStateIncidentsCount(State state){
        List<Incident> incidentList = incidentRepository.findAll();
        return (int)incidentList.stream()
                .filter(incident -> getLgabyId(incident.getId()).getState().equals(state))
                .count();
    }

    public List<Incident> getStateIncidents(State state){
        List<Incident> incidentList = incidentRepository.findAll();
        return incidentList.stream()
                .filter(incident -> getLgabyId(incident.getId()).getState().equals(state)).collect(Collectors.toList());
    }

    private State getState() {
        State state = stateRepository.findByDefaultState(true);
        return state;
    }

    @Override
    public IncidentDashboardResponse getDashboardByLga(Long lgaId) throws NotFoundException {
        Lga lga = getLga(lgaId);
        List<Incident> incidentList = incidentRepository.findByLga(lga);
        Integer totalIncidents = getLgaIncidents(lga);
        List<IncidentReport> incidentReports = new ArrayList<>();
        HashMap<String, Integer> incidentTypeMap = new HashMap<>();
        incidentList.stream()
            .forEach(incident -> {
                String incidentType = incident.getIncidentType().getName();
                Integer currentValue = incidentTypeMap.getOrDefault(incidentType, 0);
                incidentTypeMap.put(incidentType, currentValue+1);
            });
        incidentTypeMap.forEach((type, count)->{
            BigDecimal bd = BigDecimal.valueOf((count * 100.0)/totalIncidents);
            bd = bd.setScale(2,BigDecimal.ROUND_HALF_DOWN);
            double percent = bd.doubleValue();
            incidentReports.add(new IncidentReport(lga,type, count, percent));
        });
        return new IncidentDashboardResponse("00","Incident Report loaded.", incidentReports);
    }

    public Integer getLgaIncidents(Lga lga){
        List<Incident> incidentList = incidentRepository.findByLga(lga);
        return incidentList.size();
    }

    private List<IncidentReport> getIncidentReport(State state){
        List<Incident> incidentList = getStateIncidents(state);
        HashMap<String, Integer> incidentTypeMap = new HashMap<>();
        List<IncidentReport> incidentReports = new ArrayList<>();
        incidentList.stream()
                .forEach(incident -> {
                    String incidentType = incident.getIncidentType().getName();
                    Integer currentValue = incidentTypeMap.getOrDefault(incidentType, 0);
                    incidentTypeMap.put(incidentType, currentValue+1);
                });
        Integer totalIncident = getStateIncidentsCount(state);
        incidentTypeMap.forEach((type, count)->{
            Double percent = (count * 100.0)/totalIncident;
            incidentReports.add(new IncidentReport(type, count, percent));
        });
        return incidentReports;
    }

    private List<IncidentReport> getLgaReports(State state){
        List<Lga> lgas = lgaRepository.findByState(state);
        List<IncidentReport> incidentReports  = new ArrayList<>();

        List<Incident> incidentList = getStateIncidents(state);
        lgas.stream()
                .forEach(lga -> {
                    Integer totalIncidents = getLgaIncidents(lga);
                    HashMap<String, Integer> incidentTypeMap = new HashMap<>();
                    AtomicInteger totalIncident = new AtomicInteger(0);
                    AtomicInteger totalWeight = new AtomicInteger(0);
                    incidentList.stream()
                            .filter(incident -> incident.getLga().getId().equals(lga.getId()))
                            .forEach(incident -> {
                                String incidentType = incident.getIncidentType().getName();
                                Integer currentValue = incidentTypeMap.getOrDefault(incidentType, 0);
                                incidentTypeMap.put(incidentType, currentValue+1);
                            });
                    incidentList.stream()
                            .filter(incident -> incident.getLga().getId().equals(lga.getId()))
                            .filter(incident -> incident.getIncidentStatus().getName().equals("Unresolved"))
                                .forEach(incident -> {
                                    totalWeight.addAndGet(incident.getWeight());
                                    totalIncident.addAndGet(1);
                                });
                    incidentTypeMap.forEach((type, count)->{
                        Double percent = (count * 100.0)/totalIncidents;
                        int weight = totalWeight.get() / totalIncident.get();
                        incidentReports.add(new IncidentReport(lga,type, count, percent, totalIncident.get(), weight));
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

    private Lga getLgabyId(Long id) {
        Optional<Lga> currentLga = lgaRepository.findById(id);
        if(!currentLga.isPresent()){
           return new Lga();
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
