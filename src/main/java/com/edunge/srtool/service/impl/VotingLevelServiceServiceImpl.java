package com.edunge.srtool.service.impl;

import com.edunge.srtool.exceptions.DuplicateException;
import com.edunge.srtool.exceptions.NotFoundException;
import com.edunge.srtool.model.VotingLevel;
import com.edunge.srtool.repository.VotingLevelRepository;
import com.edunge.srtool.response.VotingLevelResponse;
import com.edunge.srtool.service.VotingLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VotingLevelServiceServiceImpl implements VotingLevelService {
    private final VotingLevelRepository votingLevelRepository;

    private static final String SERVICE_NAME = "Voting Level";

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
    public VotingLevelServiceServiceImpl(VotingLevelRepository votingLevelRepository) {
        this.votingLevelRepository = votingLevelRepository;
    }

    @Override
    public VotingLevelResponse saveVotingLevel(VotingLevel votingLevel) throws NotFoundException {
        VotingLevel existingVotingLevel = votingLevelRepository.findByCode(votingLevel.getCode());
        if(existingVotingLevel==null){
            votingLevelRepository.save(votingLevel);
            return new VotingLevelResponse("00", String.format(successTemplate,SERVICE_NAME), existingVotingLevel);
        }
        throw new DuplicateException(String.format(duplicateTemplate, votingLevel.getCode()));
    }

    @Override
    public VotingLevelResponse findVotingLevelById(Long id) throws NotFoundException {
        VotingLevel votingLevel = getVotingLevel(id);
        return new VotingLevelResponse("00", String.format(fetchRecordTemplate, SERVICE_NAME), votingLevel);
    }

    @Override
    public VotingLevelResponse findVotingLevelByCode(String code) throws NotFoundException {
        VotingLevel votingLevel = votingLevelRepository.findByCode(code);
        if(votingLevel==null){
            throw new NotFoundException(String.format(notFoundTemplate, SERVICE_NAME+ " " +code));
        }
        return new VotingLevelResponse("00", String.format(fetchRecordTemplate, SERVICE_NAME), votingLevel);
    }

    @Override
    public VotingLevelResponse editVotingLevel(Long id, VotingLevel votingLevel) throws NotFoundException {
        VotingLevel currentVotingLevel =  getVotingLevel(id);
        currentVotingLevel.setId(id);
        currentVotingLevel.setCode(votingLevel.getCode());
        currentVotingLevel.setName(votingLevel.getName());
        votingLevelRepository.save(currentVotingLevel);
        return new VotingLevelResponse("00", String.format(updateTemplate, SERVICE_NAME), currentVotingLevel);
    }

    @Override
    public VotingLevelResponse deleteVotingLevelById(Long id) throws NotFoundException {
        VotingLevel currentVotingLevel = getVotingLevel(id);
        votingLevelRepository.deleteById(id);
        return new VotingLevelResponse("00",String.format(deleteTemplate,currentVotingLevel.getCode()));
    }

    @Override
    public VotingLevelResponse findAll() {
        List<VotingLevel> votingLevels = votingLevelRepository.findAll();
        return new VotingLevelResponse("00", String.format(fetchRecordTemplate, SERVICE_NAME), votingLevels);
    }

    private VotingLevel getVotingLevel(Long id) throws NotFoundException {
        Optional<VotingLevel> currentState = votingLevelRepository.findById(id);
        if(!currentState.isPresent()){
            throw new NotFoundException(String.format(notFoundTemplate, SERVICE_NAME));
        }
        return currentState.get();
    }
}
