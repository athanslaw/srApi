package com.edunge.srtool.service.impl;

import com.edunge.srtool.exceptions.NotFoundException;
import com.edunge.srtool.model.Incident;
import com.edunge.srtool.model.Lga;
import com.edunge.srtool.model.State;
import com.edunge.srtool.response.IncidentDashboardResponse;
import com.edunge.srtool.response.IncidentReport;
import com.edunge.srtool.service.IncidentDashboardService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class IncidentDashboardServiceImpl implements IncidentDashboardService {
    private final IncidentServiceImpl incidentService;
    private final LgaServiceImpl lgaService;
    private final PollingUnitServiceImpl pollingUnitService;
    private final StateServiceImpl stateService;


    private final SenatorialDistrictServiceImpl senatorialDistrictService;
    public IncidentDashboardServiceImpl(IncidentServiceImpl incidentService, PollingUnitServiceImpl pollingUnitService,
                                        SenatorialDistrictServiceImpl senatorialDistrictService, LgaServiceImpl lgaService, StateServiceImpl stateService) {
        this.incidentService = incidentService;
        this.pollingUnitService = pollingUnitService;
        this.senatorialDistrictService = senatorialDistrictService;
        this.lgaService = lgaService;
        this.stateService = stateService;
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
        Integer totalIncidents = getDistrictIncidentsCount(state, id);
        if(totalIncidents == 0) {
            new IncidentDashboardResponse("00","Incident Report loaded.",totalIncidents, null, null);
        }
        try {
            List<IncidentReport> incidentReports = getIncidentReportDistrict(state, id);
            List<IncidentReport> lgaIncidentReport = getLgaReportsByDistrict(state, id);
        return new IncidentDashboardResponse("00","Incident Report loaded.",totalIncidents, incidentReports, lgaIncidentReport);
        }catch (Exception e){
            System.out.println("Exception: "+e.getStackTrace());
            return new IncidentDashboardResponse("00","Incident Report loaded.",totalIncidents, null, null);
        }
    }

    public Integer getStateIncidentsCount(State state){
        return incidentService.findIncidentByStateId(state.getId(), "", "").getIncidents().size();
    }

    private Integer getLgaIncidentsCount(long lga){
        return incidentService.findIncidentByLga(lga, "", "").getIncidents().size();
    }

    private Integer getDistrictIncidentsCount(State state, long senatorialDistrict){
        return (int)incidentService.findIncidentByStateId(state.getId(), "", "").getIncidents().stream()
                .filter(incident -> incident.getLga().getSenatorialDistrict().getId() == senatorialDistrict)
                .count();
    }

    public List<Incident> getStateIncidents(State state){
        List<Incident> incidentList = incidentService.findIncidentByStateId(state.getId(), "", "").getIncidents();
        return incidentList.stream()
                .filter(incident -> incident.getLga().getState().equals(state)).collect(Collectors.toList());
    }

    public List<Incident> getDistrictIncidents(long state, long senatorialDistrict){
        List<Incident> incidentList = incidentService.findIncidentByStateId(state, "", "").getIncidents();
        return incidentList.stream()
                .filter(incident -> incident.getLga().getSenatorialDistrict().getId().equals(senatorialDistrict)).collect(Collectors.toList());
    }

    public List<Incident> getLgaIncidents(long lga){
        return incidentService.findIncidentByLga(lga, "", "").getIncidents();
    }

    private State getState() {
        try {
            State state = stateService.getDefaultState().getState();
            return state;
        }catch (NotFoundException e){
            return new State();
        }
    }

    @Override
    public IncidentDashboardResponse getDashboardByLga(Long lgaId) throws NotFoundException {
        Integer totalIncidents = getLgaIncidentsCount(lgaId);
        if(totalIncidents == 0) {
            new IncidentDashboardResponse("00","Incident Report loaded.",totalIncidents, null, null);
        }
        try {
            List<IncidentReport> incidentReports = getIncidentReport(lgaId);
            List<IncidentReport> lgaIncidentReport = getLgaReports(lgaId);
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

    private List<IncidentReport> getIncidentReport(long lga){
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

    private List<IncidentReport> getIncidentReportDistrict(State state, long senatorialDistrict){
        List<Incident> incidentList = getDistrictIncidents(state.getId(), senatorialDistrict);
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

    private List<IncidentReport> getIncidentReportLga(long lga){
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

    private int totalPUsByLga(long lga){
        return (int)pollingUnitService.findCountByLga(lga);
    }

    private List<IncidentReport> getLgaReports(long lga) throws NotFoundException{
        List<IncidentReport> incidentReports  = new ArrayList<>();
        List<Incident> incidentList = getLgaIncidents(lga);

        try {
            Lga lgaInfo = lgaService.findLgaById(lga).getLga();
            Integer totalIncidents = getLgaIncidentsCount(lga);
            if (totalIncidents > 0) {
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
                if (totalIncident.get() > 0) {
                    incidentTypeMap.forEach((type, count) -> {
                        Double percent = (count * 100.0) / totalIncident.get();
                        incidentReports.add(new IncidentReport(lgaInfo, type, count, percent, totalIncident.get(), 0));
                    });
                }

                int weight = (int) Math.ceil((totalWeight.get() * distinctPUs.size() * 2.0) / (totalIncidents * this.totalPUsByLga(lga)));
                incidentReports
                        .forEach(incidentReport -> {
                            incidentReport.setWeight(weight);
                        });
            }
        }catch (NotFoundException e){}
        return incidentReports;
    }

    private List<IncidentReport> getLgaReports(State state) throws NotFoundException{
        try {
            List<Lga> lgas = lgaService.findLgaByStateCode(state.getId()).getLgas();
            List<IncidentReport> incidentReports = new ArrayList<>();
            List<Incident> incidentList = getStateIncidents(state);
            lgas.forEach(lga -> {
                Integer totalIncidents = getLgaIncidentsCount(lga.getId());
                if (totalIncidents > 0) {
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
                    if (totalIncident.get() > 0) {
                        incidentTypeMap.forEach((type, count) -> {
                            Double percent = (count * 100.0) / totalIncident.get();
                            incidentReports.add(new IncidentReport(lga, type, count, percent, totalIncident.get(), 0));
                        });
                    }

                    int weight = (int) Math.ceil((totalWeight.get() * distinctPUs.size() * 2.0) / (totalIncidents * this.totalPUsByLga(lga.getId())));
                    incidentReports
                            .forEach(incidentReport -> {
                                if (incidentReport.getLga().equals(lga)) {
                                    incidentReport.setWeight(weight);
                                }
                            });
                }
            });
            return incidentReports;
        }catch (NotFoundException e){
            throw new NotFoundException("Not found");
        }
    }

    private List<IncidentReport> getLgaReportsByDistrict(State state, long senatorialDistrict) throws NotFoundException{
        List<Lga> lgas = lgaService.findLgaBySenatorialDistrictCode(senatorialDistrict).getLgas();
        List<IncidentReport> incidentReports  = new ArrayList<>();
        List<Incident> incidentList = getDistrictIncidents(state.getId(), senatorialDistrict);
        lgas.forEach(lga -> {
                    Integer totalIncidents = getLgaIncidentsCount(lga.getId());
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

                        int weight = (int) Math.ceil((totalWeight.get() * distinctPUs.size() * 2.0) / (totalIncidents * this.totalPUsByLga(lga.getId())));
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

}
