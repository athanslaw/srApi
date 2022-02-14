package com.edunge.srtool.service.impl;

import com.edunge.srtool.config.FileConfigurationProperties;
import com.edunge.srtool.exceptions.FileNotFoundException;
import com.edunge.srtool.exceptions.NotFoundException;
import com.edunge.srtool.model.Election;
import com.edunge.srtool.model.PoliticalParty;
import com.edunge.srtool.model.PoliticalPartyCandidate;
import com.edunge.srtool.repository.ElectionRepository;
import com.edunge.srtool.repository.PoliticalPartyCandidateRepository;
import com.edunge.srtool.repository.PoliticalPartyRepository;
import com.edunge.srtool.response.PoliticalPartyCandidateResponse;
import com.edunge.srtool.service.FileProcessingService;
import com.edunge.srtool.service.PoliticalPartyCandidateService;
import com.edunge.srtool.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
@Service
public class PoliticalPartyCandidateServiceServiceImpl implements PoliticalPartyCandidateService {
    private final PoliticalPartyCandidateRepository politicalPartyCandidateRepository;
    private final ElectionRepository electionRepository;
    private final PoliticalPartyRepository politicalPartyRepository;
    private static final Logger logger = LoggerFactory.getLogger(PoliticalPartyCandidateServiceServiceImpl.class);

    @Autowired
    FileProcessingService fileProcessingService;

    @Autowired
    public PoliticalPartyCandidateServiceServiceImpl(PoliticalPartyCandidateRepository politicalPartyCandidateRepository, ElectionRepository electionRepository, PoliticalPartyRepository politicalPartyRepository) {
        this.politicalPartyCandidateRepository = politicalPartyCandidateRepository;
        this.electionRepository = electionRepository;
        this.politicalPartyRepository = politicalPartyRepository;
    }
    private static final String SERVICE_NAME = "Political party candidate";
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


    @Override
    public PoliticalPartyCandidateResponse findAll() {
        List<PoliticalPartyCandidate> politicalPartyCandidates = politicalPartyCandidateRepository.findAll();
        return new PoliticalPartyCandidateResponse("00","All candidates retrieved.", politicalPartyCandidates);
    }

    @Override
    public PoliticalPartyCandidateResponse findById(Long id) throws NotFoundException {
        PoliticalPartyCandidate politicalPartyCandidate = getPoliticalPartyCandidate(id);
        return new PoliticalPartyCandidateResponse("00","Party candidate retrieved successfully.", politicalPartyCandidate);
    }

    @Override
    public PoliticalPartyCandidateResponse deleteById(Long id) throws NotFoundException {
        PoliticalPartyCandidate politicalPartyCandidate = getPoliticalPartyCandidate(id);
        politicalPartyCandidateRepository.deleteById(id);
        return new PoliticalPartyCandidateResponse("00",String.format(deleteTemplate, politicalPartyCandidate.getFirstname()));
    }

    @Override
    public PoliticalPartyCandidateResponse savePoliticalPartyCandidate(Long electionId, Long politicalPartyId, String firstName, String lastName, MultipartFile file) throws NotFoundException {
        Election election = getElection(electionId);
        PoliticalParty politicalParty = getPoliticalParty(politicalPartyId);

        PoliticalPartyCandidate politicalPartyCandidate = new PoliticalPartyCandidate();
        politicalPartyCandidate.setElection(election);
        politicalPartyCandidate.setPoliticalParty(politicalParty);
        politicalPartyCandidate.setFirstname(firstName);
        politicalPartyCandidate.setLastname(lastName);
        String filename = uploadFile(file);
        String imageUrl = getImageUrl(filename);
        politicalPartyCandidate.setImageUrl(imageUrl);
        politicalPartyCandidateRepository.save(politicalPartyCandidate);
        return new PoliticalPartyCandidateResponse("00","Political party candidate saved successfully.", politicalPartyCandidate);
    }

    @Override
    public PoliticalPartyCandidateResponse updatePoliticalPartyCandidate(Long id, Long electionId, Long politicalPartyId, String firstName, String lastName, MultipartFile file) throws NotFoundException {
        PoliticalPartyCandidate politicalPartyCandidate = getPoliticalPartyCandidate(id);
        Election election = getElection(electionId);
        PoliticalParty politicalParty = getPoliticalParty(politicalPartyId);
        politicalPartyCandidate.setElection(election);
        politicalPartyCandidate.setPoliticalParty(politicalParty);
        politicalPartyCandidate.setFirstname(firstName);
        politicalPartyCandidate.setLastname(lastName);
        String filename = uploadFile(file);
        String imageUrl = getImageUrl(filename);
        politicalPartyCandidate.setImageUrl(imageUrl);
        politicalPartyCandidateRepository.save(politicalPartyCandidate);
        return new PoliticalPartyCandidateResponse("00",String.format(updateTemplate, politicalPartyCandidate.getFirstname()), politicalPartyCandidate);

    }

    private String getImageUrl(String fileName){
        StringBuilder sb = new StringBuilder();
        sb.append("/uploads/img/");
        sb.append(fileName);
        return sb.toString();
    }

    private String uploadFile(MultipartFile file){
        return FileUtil.uploadFile(file, fileProcessingService.getFileStorageLocation());
    }

    private PoliticalParty getPoliticalParty(Long id) throws NotFoundException {
        Optional<PoliticalParty> currentPollingUnit = politicalPartyRepository.findById(id);
        if(!currentPollingUnit.isPresent()){
            throw new NotFoundException(String.format(notFoundTemplate,"Political Party"));
        }
        return currentPollingUnit.get();
    }

    private PoliticalPartyCandidate getPoliticalPartyCandidate(Long id) throws NotFoundException {
        Optional<PoliticalPartyCandidate> politicalPartyCandidate = politicalPartyCandidateRepository.findById(id);
        if(!politicalPartyCandidate.isPresent()){
            throw new NotFoundException(String.format(notFoundTemplate,"Political Party Candidate"));
        }
        return politicalPartyCandidate.get();
    }

    private Election getElection(Long id) throws NotFoundException {
        Optional<Election> election = electionRepository.findById(id);
        if(!election.isPresent()){
            throw new NotFoundException(String.format(notFoundTemplate,"Election"));
        }
        return election.get();
    }


    @Override
    public Resource loadPoliticalPartyImage(String fileName) {
        return FileUtil.loadResource(fileName, fileProcessingService.getFileStorageLocation());
    }

    @Override
    public PoliticalPartyCandidateResponse findCandidateByName(String firstname, String lastname) throws NotFoundException {
        List<PoliticalPartyCandidate> politicalPartyCandidates = politicalPartyCandidateRepository.findByFirstnameOrLastname(firstname, lastname);
        if(politicalPartyCandidates==null){
            throw new NotFoundException(String.format(notFoundTemplate,SERVICE_NAME));
        }
        return new PoliticalPartyCandidateResponse("00", String.format(fetchRecordTemplate, SERVICE_NAME), politicalPartyCandidates);
    }
}
