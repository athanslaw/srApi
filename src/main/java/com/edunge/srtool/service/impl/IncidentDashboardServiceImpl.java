package com.edunge.srtool.service.impl;

import com.edunge.srtool.exceptions.NotFoundException;
import com.edunge.srtool.model.Incident;
import com.edunge.srtool.model.Lga;
import com.edunge.srtool.model.SenatorialDistrict;
import com.edunge.srtool.model.State;
import com.edunge.srtool.repository.*;
import com.edunge.srtool.response.IncidentDashboardResponse;
import com.edunge.srtool.response.IncidentReport;
import com.edunge.srtool.service.IncidentDashboardService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class IncidentDashboardServiceImpl implements IncidentDashboardService {
    private final IncidentRepository incidentRepository;
    private final LgaRepository lgaRepository;
    private final PollingUnitRepository pollingUnitRepository;
    private final StateRepository stateRepository;
    private final SenatorialDistrictRepository senatorialDistrictRepository;
    public IncidentDashboardServiceImpl(IncidentRepository incidentRepository, PollingUnitRepository pollingUnitRepository,
                                        SenatorialDistrictRepository senatorialDistrictRepository, LgaRepository lgaRepository, StateRepository stateRepository) {
        this.incidentRepository = incidentRepository;
        this.lgaRepository = lgaRepository;
        this.stateRepository = stateRepository;
        this.pollingUnitRepository = pollingUnitRepository;
        this.senatorialDistrictRepository = senatorialDistrictRepository;
    }

    @Override
    public IncidentDashboardResponse getDashboardByState() throws NotFoundException {

        State state = this.getState();
        Integer totalIncidents = getStateIncidentsCount(state);
        if(totalIncidents == 0) {
            new IncidentDashboardResponse("00","Incident Report loaded.",totalIncidents, null, null);
        }
        try {
            List<IncidentReport> incidentReports = getIncidentReport(state);
            List<IncidentReport> lgaIncidentReport = getLgaReports(state);
        return new IncidentDashboardResponse("00","Incident Report loaded.",totalIncidents, incidentReports, lgaIncidentReport);
        }catch (Exception e){
            System.out.println("Exception: "+e.getStackTrace());
            return new IncidentDashboardResponse("00","Incident Report loaded.",totalIncidents, null, null);
        }
    }

    @Override
    public IncidentDashboardResponse getDashboardBySenatorialDistrict(Long id) throws NotFoundException {

        State state = getState();
        SenatorialDistrict senatorialDistrict = senatorialDistrictRepository.findById(id).get();
        Integer totalIncidents = getDistrictIncidentsCount(state, senatorialDistrict);
        if(totalIncidents == 0) {
            new IncidentDashboardResponse("00","Incident Report loaded.",totalIncidents, null, null);
        }
        try {
            List<IncidentReport> incidentReports = getIncidentReportDistrict(state, senatorialDistrict);
            List<IncidentReport> lgaIncidentReport = getLgaReportsByDistrict(state, senatorialDistrict);
        return new IncidentDashboardResponse("00","Incident Report loaded.",totalIncidents, incidentReports, lgaIncidentReport);
        }catch (Exception e){
            System.out.println("Exception: "+e.getStackTrace());
            return new IncidentDashboardResponse("00","Incident Report loaded.",totalIncidents, null, null);
        }
    }

    public Integer getStateIncidentsCount(State state){
        return incidentRepository.findByStateId(state.getId()).size();
    }

    private Integer getLgaIncidentsCount(Lga lga){
        return incidentRepository.findByLga(lga).size();
    }

    private Integer getDistrictIncidentsCount(State state, SenatorialDistrict senatorialDistrict){
        return (int)incidentRepository.findByStateId(state.getId()).stream()
                .filter(incident -> incident.getLga().getSenatorialDistrict().equals(senatorialDistrict))
                .count();
    }

    public List<Incident> getStateIncidents(State state){
        List<Incident> incidentList = incidentRepository.findByStateId(state.getId());
        return incidentList.stream()
                .filter(incident -> getLgabyId(incident.getLga().getId()).getState().equals(state)).collect(Collectors.toList());
    }

    public List<Incident> getDistrictIncidents(State state, SenatorialDistrict senatorialDistrict){
        List<Incident> incidentList = incidentRepository.findByStateId(state.getId());
        return incidentList.stream()
                .filter(incident -> incident.getLga().getSenatorialDistrict().equals(senatorialDistrict)).collect(Collectors.toList());
    }

    public List<Incident> getLgaIncidents(Lga lga){
        return incidentRepository.findByLga(lga);
    }

    private State getState() {
        State state = stateRepository.findByDefaultState(true);
        return state;
    }

    @Override
    public IncidentDashboardResponse getDashboardByLga(Long lgaId) throws NotFoundException {
        Lga lga = getLga(lgaId);
        Integer totalIncidents = getLgaIncidentsCount(lga);
        if(totalIncidents == 0) {
            new IncidentDashboardResponse("00","Incident Report loaded.",totalIncidents, null, null);
        }
        try {
            List<IncidentReport> incidentReports = getIncidentReport(lga);
            List<IncidentReport> lgaIncidentReport = getLgaReports(lga);
            return new IncidentDashboardResponse("00","Incident Report loaded.",totalIncidents, incidentReports, lgaIncidentReport);
        }catch (Exception e){
            System.out.println("Exception: "+e.getStackTrace());
            return new IncidentDashboardResponse("00","Incident Report loaded.",totalIncidents, null, null);
        }
    }

    private List<IncidentReport> getIncidentReport(State state){
        List<Incident> incidentList = getStateIncidents(state);
        HashMap<String, Integer> incidentTypeMap = new HashMap<>();
        List<IncidentReport> incidentReports = new ArrayList<>();
        incidentList
                .forEach(incident -> {
                    String incidentType = incident.getIncidentType().getName();
                    Integer currentValue = incidentTypeMap.getOrDefault(incidentType, 0);
                    incidentTypeMap.put(incidentType, currentValue+1);
                });
        Integer totalIncident = getStateIncidentsCount(state);
        if(totalIncident < 1){
            return new ArrayList<IncidentReport>();
        }
        incidentTypeMap.forEach((type, count)->{
            Double percent = (count * 100.0)/totalIncident;
            incidentReports.add(new IncidentReport(type, count, percent));
        });
        return incidentReports;
    }

    private List<IncidentReport> getIncidentReport(Lga lga){
        List<Incident> incidentList = getLgaIncidents(lga);
        HashMap<String, Integer> incidentTypeMap = new HashMap<>();
        List<IncidentReport> incidentReports = new ArrayList<>();
        incidentList
                .forEach(incident -> {
                    String incidentType = incident.getIncidentType().getName();
                    Integer currentValue = incidentTypeMap.getOrDefault(incidentType, 0);
                    incidentTypeMap.put(incidentType, currentValue+1);
                });
        Integer totalIncident = getLgaIncidentsCount(lga);
        if(totalIncident < 1){
            return new ArrayList<IncidentReport>();
        }
        incidentTypeMap.forEach((type, count)->{
            Double percent = (count * 100.0)/totalIncident;
            incidentReports.add(new IncidentReport(type, count, percent));
        });
        return incidentReports;
    }

    private List<IncidentReport> getIncidentReportDistrict(State state, SenatorialDistrict senatorialDistrict){
        List<Incident> incidentList = getDistrictIncidents(state, senatorialDistrict);
        HashMap<String, Integer> incidentTypeMap = new HashMap<>();
        List<IncidentReport> incidentReports = new ArrayList<>();
        incidentList
                .forEach(incident -> {
                    String incidentType = incident.getIncidentType().getName();
                    Integer currentValue = incidentTypeMap.getOrDefault(incidentType, 0);
                    incidentTypeMap.put(incidentType, currentValue+1);
                });
        Integer totalIncident = getStateIncidentsCount(state);
        if(totalIncident < 1){
            return new ArrayList<IncidentReport>();
        }
        incidentTypeMap.forEach((type, count)->{
            Double percent = (count * 100.0)/totalIncident;
            incidentReports.add(new IncidentReport(type, count, percent));
        });
        return incidentReports;
    }

    private List<IncidentReport> getIncidentReportLga(Lga lga){
        List<Incident> incidentList = getLgaIncidents(lga);
        HashMap<String, Integer> incidentTypeMap = new HashMap<>();
        List<IncidentReport> incidentReports = new ArrayList<>();
        incidentList
                .forEach(incident -> {
                    String incidentType = incident.getIncidentType().getName();
                    Integer currentValue = incidentTypeMap.getOrDefault(incidentType, 0);
                    incidentTypeMap.put(incidentType, currentValue+1);
                });
        Integer totalIncident = getLgaIncidentsCount(lga);
        if(totalIncident < 1){
            return new ArrayList<IncidentReport>();
        }
        incidentTypeMap.forEach((type, count)->{
            Double percent = (count * 100.0)/totalIncident;
            incidentReports.add(new IncidentReport(type, count, percent));
        });
        return incidentReports;
    }

    private int totalPUsByLga(Lga lga){
        return pollingUnitRepository.findByLga(lga).size();
    }

    private List<IncidentReport> getLgaReports(Lga lga){
        List<IncidentReport> incidentReports  = new ArrayList<>();
        List<Incident> incidentList = getLgaIncidents(lga);

        Integer totalIncidents = getLgaIncidentsCount(lga);
        if(totalIncidents > 0) {
            HashMap<String, Integer> incidentTypeMap = new HashMap<>();
            AtomicInteger totalIncident = new AtomicInteger(0);
            AtomicInteger totalWeight = new AtomicInteger(0);
            // no of PU with incidents
            Set<Long> distinctPUs = new LinkedHashSet<>();

            incidentList.forEach(incident -> {
                String incidentType = incident.getIncidentType().getName();
                Integer currentValue = incidentTypeMap.getOrDefault(incidentType, 0);
                incidentTypeMap.put(incidentType, currentValue + 1);
            });
            incidentList.stream()
                    .filter(incident -> incident.getIncidentStatus().getName().equals("Unresolved"))
                    .forEach(incident -> {
                        distinctPUs.add(incident.getPollingUnit().getId());
                        totalWeight.addAndGet(incident.getWeight());
                        totalIncident.addAndGet(1);
                    });
            if(totalIncident.get() > 0) {
                incidentTypeMap.forEach((type, count) -> {
                    Double percent = (count * 100.0) / totalIncident.get();
                    incidentReports.add(new IncidentReport(lga, type, count, percent, totalIncident.get(), 0));
                });
            }

            int weight = (int) Math.ceil((totalWeight.get() * distinctPUs.size() * 2.0) / (totalIncidents * this.totalPUsByLga(lga)));
            incidentReports
                    .forEach(incidentReport -> {
                            incidentReport.setWeight(weight);
                    });
        }
        return incidentReports;
    }

    private List<IncidentReport> getLgaReports(State state){
        List<Lga> lgas = lgaRepository.findByState(state);
        List<IncidentReport> incidentReports  = new ArrayList<>();
        List<Incident> incidentList = getStateIncidents(state);
        lgas.forEach(lga -> {
                    Integer totalIncidents = getLgaIncidentsCount(lga);
                    if(totalIncidents > 0) {
                        HashMap<String, Integer> incidentTypeMap = new HashMap<>();
                        AtomicInteger totalIncident = new AtomicInteger(0);
                        AtomicInteger totalWeight = new AtomicInteger(0);
                        // no of PU with incidents
                        Set<Long> distinctPUs = new LinkedHashSet<>();

                        incidentList.stream()
                                .filter(incident -> incident.getLga().getId().equals(lga.getId()))
                                .forEach(incident -> {
                                    String incidentType = incident.getIncidentType().getName();
                                    Integer currentValue = incidentTypeMap.getOrDefault(incidentType, 0);
                                    incidentTypeMap.put(incidentType, currentValue + 1);
                                });
                        incidentList.stream()
                                .filter(incident -> incident.getLga().getId().equals(lga.getId()))
                                .filter(incident -> incident.getIncidentStatus().getName().equals("Unresolved"))
                                .forEach(incident -> {
                                    distinctPUs.add(incident.getPollingUnit().getId());
                                    totalWeight.addAndGet(incident.getWeight());
                                    totalIncident.addAndGet(1);
                                });
                        if(totalIncident.get() > 0) {
                            incidentTypeMap.forEach((type, count) -> {
                                Double percent = (count * 100.0) / totalIncident.get();
                                incidentReports.add(new IncidentReport(lga, type, count, percent, totalIncident.get(), 0));
                            });
                        }

                        int weight = (int) Math.ceil((totalWeight.get() * distinctPUs.size() * 2.0) / (totalIncidents * this.totalPUsByLga(lga)));
                        incidentReports
                                .forEach(incidentReport -> {
                                    if(incidentReport.getLga().equals(lga)){
                                        incidentReport.setWeight(weight);
                                    }
                                });
                    }
                });
        return incidentReports;
    }

    private List<IncidentReport> getLgaReportsByDistrict(State state, SenatorialDistrict senatorialDistrict){
        List<Lga> lgas = lgaRepository.findBySenatorialDistrict(senatorialDistrict);
        List<IncidentReport> incidentReports  = new ArrayList<>();
        List<Incident> incidentList = getDistrictIncidents(state, senatorialDistrict);
        lgas.forEach(lga -> {
                    Integer totalIncidents = getLgaIncidentsCount(lga);
                    if(totalIncidents > 0) {
                        HashMap<String, Integer> incidentTypeMap = new HashMap<>();
                        AtomicInteger totalIncident = new AtomicInteger(0);
                        AtomicInteger totalWeight = new AtomicInteger(0);
                        // no of PU with incidents
                        Set<Long> distinctPUs = new LinkedHashSet<>();

                        incidentList.stream()
                                .filter(incident -> incident.getLga().getId().equals(lga.getId()))
                                .forEach(incident -> {
                                    String incidentType = incident.getIncidentType().getName();
                                    Integer currentValue = incidentTypeMap.getOrDefault(incidentType, 0);
                                    incidentTypeMap.put(incidentType, currentValue + 1);
                                });
                        incidentList.stream()
                                .filter(incident -> incident.getLga().getId().equals(lga.getId()))
                                .filter(incident -> incident.getIncidentStatus().getName().equals("Unresolved"))
                                .forEach(incident -> {
                                    distinctPUs.add(incident.getPollingUnit().getId());
                                    totalWeight.addAndGet(incident.getWeight());
                                    totalIncident.addAndGet(1);
                                });
                        if(totalIncident.get() > 0) {
                            incidentTypeMap.forEach((type, count) -> {
                                Double percent = (count * 100.0) / totalIncident.get();
                                incidentReports.add(new IncidentReport(lga, type, count, percent, totalIncident.get(), 0));
                            });
                        }

                        int weight = (int) Math.ceil((totalWeight.get() * distinctPUs.size() * 2.0) / (totalIncidents * this.totalPUsByLga(lga)));
                        incidentReports
                                .forEach(incidentReport -> {
                                    if(incidentReport.getLga().equals(lga)){
                                        incidentReport.setWeight(weight);
                                    }
                                });
                    }
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

    private Lga getLgabyId(long id) {
        Optional<Lga> currentLga = lgaRepository.findById(id);
        if(!currentLga.isPresent()){
           return new Lga();
        }
        return currentLga.get();
    }

}
