package com.edunge.srtool.service;

import com.edunge.srtool.exceptions.NotFoundException;
import com.edunge.srtool.response.PoliticalPartyCandidateResponse;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;


public interface PoliticalPartyCandidateService {
    PoliticalPartyCandidateResponse findAll();
    PoliticalPartyCandidateResponse findById(Long id) throws NotFoundException;
    PoliticalPartyCandidateResponse deleteById(Long id) throws NotFoundException;
    Resource loadPoliticalPartyImage(String fileName);
    PoliticalPartyCandidateResponse savePoliticalPartyCandidate(Long electionId, Long politicalPartyId, String firstName, String lastName, MultipartFile file)throws NotFoundException;
    PoliticalPartyCandidateResponse updatePoliticalPartyCandidate(Long id, Long electionId, Long politicalPartyId, String firstName, String lastName, MultipartFile file)throws NotFoundException;

    PoliticalPartyCandidateResponse findCandidateByName(String firstname, String lastname) throws NotFoundException;
}
