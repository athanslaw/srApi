package com.edunge.srtool.service.impl;

import com.edunge.srtool.dto.WardDto;
import com.edunge.srtool.exceptions.DuplicateException;
import com.edunge.srtool.exceptions.NotFoundException;
import com.edunge.srtool.model.*;
import com.edunge.srtool.repository.WardRepository;
import com.edunge.srtool.response.WardResponse;
import com.edunge.srtool.service.FileProcessingService;
import com.edunge.srtool.service.LgaService;
import com.edunge.srtool.service.WardService;
import com.edunge.srtool.util.Constants;
import com.edunge.srtool.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class WardServiceImpl implements WardService {

    @Autowired
    private LgaService lgaService;

    private final WardRepository wardRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(WardService.class);

    private static final String SERVICE_NAME = "Ward";

    @Value("${notfound.message.template}")
    private String notFoundTemplate;

    @Value("${success.message.template}")
    private String successTemplate;

    @Value("${duplicate.message.template}")
    private String duplicateTemplate;

    @Value("${update.message.template}")
    private String updateTemplate;

    @Value("${delete.message.template}")
    private String deleteTemplate;

    @Value("${fetch.message.template}")
    private String fetchRecordTemplate;

    @Autowired
    FileProcessingService fileProcessingService;
    @Autowired
    public WardServiceImpl(WardRepository wardRepository) {
        this.wardRepository = wardRepository;
    }

    @Override
    public WardResponse saveWard(WardDto wardDto) throws NotFoundException {
        Lga lga = getLga(wardDto.getLgaId());
        Ward ward = new Ward();
        ward.setSenatorialDistrict(lga.getSenatorialDistrict());
        ward.setState(lga.getState());
        ward.setCode(wardDto.getCode());
        ward.setName(wardDto.getName());
        ward.setLga(lga);
        wardRepository.save(ward);
        return new WardResponse("00", String.format(successTemplate,SERVICE_NAME), ward);
    }
    @Override
    public WardResponse findWardById(Long id) throws NotFoundException {
        Ward currentWard = getWard(id);
        return new WardResponse("00", String.format(fetchRecordTemplate,SERVICE_NAME), currentWard);
    }

    @Override
    public List<Ward> findWardByState(Long id) {
        return wardRepository.findByState(new State(){{setId(id);}});
    }

    @Override
    public long countWardByState(State state) {
        if(TerritorialDataCount.get(Constants.WARD+Constants.ZONE+state.getId()) != -1){
            return TerritorialDataCount.get(Constants.WARD+Constants.ZONE+state.getId());
        }
        long data = wardRepository.countByState(state);
        TerritorialDataCount.set(Constants.WARD+Constants.STATE+state.getId(), data);
        return data;
    }

    @Override
    public long countWardByLga(Lga lga) {
        if(TerritorialDataCount.get(Constants.WARD+Constants.LGA+lga.getId()) != -1){
            return TerritorialDataCount.get(Constants.WARD+Constants.LGA+lga.getId());
        }
        long data = wardRepository.countByLga(lga);
        TerritorialDataCount.set(Constants.WARD+Constants.LGA+lga.getId(), data);
        return data;
    }

    @Override
    public long countWard() {
        if(TerritorialDataCount.get(Constants.WARD) != -1){
            return TerritorialDataCount.get(Constants.WARD);
        }
        long data = wardRepository.count();
        TerritorialDataCount.set(Constants.WARD, data);
        return data;
    }

    @Override
    public void updateWardLga(Long lgaOld, Lga lga) throws NotFoundException {
        List<Ward> wards = wardRepository.findByLga(new Lga(){{setId(lgaOld);}});
        wards.forEach(ward -> {
            ward.setLga(lga);
            ward.setSenatorialDistrict(lga.getSenatorialDistrict());
            ward.setState(lga.getState());
            wardRepository.save(ward);
        });
    }

    @Override
    public void updateWardDistrict(Long districtOld, SenatorialDistrict senatorialDistrict) throws NotFoundException {
        List<Ward> wards = wardRepository.findBySenatorialDistrict(new SenatorialDistrict(){{setId(districtOld);}});
        wards.forEach(ward -> {
            ward.setSenatorialDistrict(senatorialDistrict);
            ward.setState(senatorialDistrict.getState());
            wardRepository.save(ward);
        });
    }

    @Override
    public long countWardBySenatorialDistrict(SenatorialDistrict senatorialDistrict) {
        if(TerritorialDataCount.get(Constants.WARD+Constants.SENATORIAL_DISTRICT+senatorialDistrict.getId()) != -1){
            return TerritorialDataCount.get(Constants.WARD+Constants.SENATORIAL_DISTRICT+senatorialDistrict.getId());
        }
        long data = wardRepository.countBySenatorialDistrict(senatorialDistrict);
        TerritorialDataCount.set(Constants.WARD+Constants.SENATORIAL_DISTRICT+senatorialDistrict.getId(), data);
        return data;
    }

    @Override
    public WardResponse findWardByCode(String code) throws NotFoundException {
        Ward currentWard = wardRepository.findByCode(code);
        if(currentWard==null){
            throw new NotFoundException(String.format(notFoundTemplate,SERVICE_NAME));
        }
        return new WardResponse("00", String.format(fetchRecordTemplate, SERVICE_NAME), currentWard);
    }

    @Override
    public WardResponse updateWard(Long id, WardDto wardDto) throws NotFoundException {
        Lga lga = getLga(wardDto.getLgaId());
        Ward currentWard = getWard(id);
        currentWard.setId(id);
        currentWard.setCode(wardDto.getCode());
        currentWard.setName(wardDto.getName());
        currentWard.setState(lga.getState());
        currentWard.setSenatorialDistrict(lga.getSenatorialDistrict());
        currentWard.setLga(lga);
        wardRepository.save(currentWard);
        return new WardResponse("00", String.format(successTemplate, SERVICE_NAME), currentWard);
    }

    @Override
    public WardResponse deleteWardById(Long id) throws NotFoundException {
        Ward currentWard = getWard(id);
        wardRepository.delete(currentWard);
        return new WardResponse("00",String.format(deleteTemplate,currentWard.getCode()));
    }

    @Override
    public WardResponse findAll() {
        List<Ward> wards = wardRepository.findAll();
        return new WardResponse("00", String.format(fetchRecordTemplate,SERVICE_NAME), wards);
    }

    public WardResponse searchWardByFilter(Long stateId, Long senatorialDistrictId, Long lgaId) {
        List<Ward> filter;
            if (lgaId > 0) {
                filter = wardRepository.findByLgaOrderByCodeAsc(new Lga(){{setId(lgaId);}});
            } else if (senatorialDistrictId > 0) {
                filter = wardRepository.findBySenatorialDistrict(new SenatorialDistrict(){{setId(senatorialDistrictId);}});
            } else if (stateId > 0) {
                filter = wardRepository.findByState(new State(){{setId(stateId);}});
            }
            else{
                filter = wardRepository.findAll();
            }
        return new WardResponse("00", String.format(fetchRecordTemplate,SERVICE_NAME), filter);
    }

    @Override
    public WardResponse filterByName(String name) throws NotFoundException {
        List<Ward> ward = wardRepository.findByNameStartingWithOrderByCodeAsc(name);
        if(ward!=null){
            return new WardResponse("00", String.format(fetchRecordTemplate,SERVICE_NAME), ward);
        }
        throw new NotFoundException("Ward not found.");
    }

    @Override
    public WardResponse findByLga(Long lgaCode) throws NotFoundException {
        List<Ward> ward = wardRepository.findByLgaOrderByCodeAsc(new Lga(){{setId(lgaCode);}});
        if(ward!=null){
            return new WardResponse("00", String.format(fetchRecordTemplate,SERVICE_NAME), ward);
        }
        throw new NotFoundException("Ward not found.");
    }


    private Lga getLga(Long id) throws NotFoundException {
        Lga currentLga = lgaService.findLgaById(id).getLga();
        if(currentLga == null){
            throw new NotFoundException("Lga not found.");
        }
        return currentLga;
    }

    private Ward getWard(Long id) throws NotFoundException {
        Optional<Ward> currentWard = wardRepository.findById(id);
        if(!currentWard.isPresent()){
            throw new NotFoundException(String.format(notFoundTemplate,SERVICE_NAME));
        }
        return currentWard.get();
    }

    private void saveWard(String stateCode,String senatorialDistrictCode,String lgaCode, String code, String name)  {
        try{
            Optional<Ward> ward = wardRepository.findById(Long.parseLong(code));
            Lga lga = lgaService.findLgaById(Long.parseLong(lgaCode)).getLga();
            if(!ward.isPresent()){
                Ward ward1 = ward.get();
                ward1.setState(lga.getState());
                ward1.setLga(lga);
                ward1.setSenatorialDistrict(lga.getSenatorialDistrict());
                ward1.setCode(code);
                ward1.setName(name);
                wardRepository.save(ward1);
            }
        }
        catch (Exception ex){
            LOGGER.info("State could not be saved");
        }
    }

    @Override
    public WardResponse uploadWard(MultipartFile file){
        List<String> csvLines = FileUtil.getCsvLines(file, fileProcessingService.getFileStorageLocation());
        return processUpload(csvLines);
    }


    private WardResponse processUpload(List<String> lines){
        for (String line:lines) {
            String[] state = line.split(",");
            saveWard(state[0].trim(), state[1].trim(), state[2].trim(), state[3].trim(),state[4].trim());
        }
        return new WardResponse("00", "File Uploaded.");
    }
}
