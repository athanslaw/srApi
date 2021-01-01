package com.edunge.srtool.service.impl;

import com.edunge.srtool.dto.ResultPerPartyDto;
import com.edunge.srtool.exceptions.DuplicateException;
import com.edunge.srtool.exceptions.NotFoundException;
import com.edunge.srtool.model.PoliticalParty;
import com.edunge.srtool.model.Result;
import com.edunge.srtool.model.ResultPerParty;
import com.edunge.srtool.repository.PoliticalPartyRepository;
import com.edunge.srtool.repository.ResultPerPartyRepository;
import com.edunge.srtool.repository.ResultRepository;
import com.edunge.srtool.response.ResultPerPartyResponse;
import com.edunge.srtool.service.ResultPerPartyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ResultPerPartyServiceImpl implements ResultPerPartyService {
    private final ResultRepository resultRepository;
    private final PoliticalPartyRepository politicalPartyRepository;
    private final ResultPerPartyRepository resultPerPartyRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(ResultPerPartyServiceImpl.class);

    private static final String SERVICE_NAME = "Result Per Party";

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
    public ResultPerPartyServiceImpl(ResultRepository resultRepository, PoliticalPartyRepository politicalPartyRepository, ResultPerPartyRepository resultPerPartyRepository) {
        this.resultRepository = resultRepository;
        this.politicalPartyRepository = politicalPartyRepository;
        this.resultPerPartyRepository = resultPerPartyRepository;
    }

    @Override
    public ResultPerPartyResponse saveResultPerParty(ResultPerPartyDto resultPerPartyDto) throws NotFoundException {
        Result result = getResult(resultPerPartyDto.getResultId());
        PoliticalParty politicalParty = getPoliticalParty(resultPerPartyDto.getPoliticalPartyId());

        ResultPerParty resultPerParty = resultPerPartyRepository.findByResultAndPoliticalParty(result, politicalParty);
        if(resultPerParty==null){
            resultPerParty = new ResultPerParty();
            resultPerParty.setPoliticalParty(politicalParty);
            resultPerParty.setResult(result);
            resultPerParty.setVoteCount(resultPerPartyDto.getVoteCount());
            resultPerPartyRepository.save(resultPerParty);
            return new ResultPerPartyResponse("00", String.format(successTemplate,SERVICE_NAME), resultPerParty);
        }
        throw new DuplicateException(String.format("Results for %s already exists", resultPerParty.getPoliticalParty().getName()));
    }

    @Override
    public ResultPerPartyResponse findResultPerPartyById(Long id) throws NotFoundException {
        ResultPerParty result = getResultPerParty(id);
        return new ResultPerPartyResponse("00", String.format(fetchRecordTemplate,SERVICE_NAME), result);
    }


    @Override
    public ResultPerPartyResponse updateResultPerParty(Long id, ResultPerPartyDto resultPerPartyDto) throws NotFoundException {
        ResultPerParty resultPerParty = getResultPerParty(id);
        Result result = getResult(resultPerPartyDto.getResultId());
        PoliticalParty politicalParty = getPoliticalParty(resultPerPartyDto.getPoliticalPartyId());

        resultPerParty.setPoliticalParty(politicalParty);
        resultPerParty.setResult(result);
        resultPerParty.setVoteCount(resultPerPartyDto.getVoteCount());
        resultPerPartyRepository.save(resultPerParty);
        return new ResultPerPartyResponse("00", String.format(successTemplate,SERVICE_NAME), resultPerParty);
    }

    @Override
    public ResultPerPartyResponse deleteResultPerPartyById(Long id) throws NotFoundException {
        ResultPerParty resultPerParty = getResultPerParty(id);
        resultRepository.deleteById(id);
        return new ResultPerPartyResponse("00",String.format(deleteTemplate,SERVICE_NAME));
    }

    @Override
    public ResultPerPartyResponse findAll() {
        List<ResultPerParty> resultPerParties = resultPerPartyRepository.findAll();
        return new ResultPerPartyResponse("00", String.format(fetchRecordTemplate,SERVICE_NAME), resultPerParties);
    }

    private ResultPerParty getResultPerParty(Long id) throws NotFoundException {
        Optional<ResultPerParty> partyAgent = resultPerPartyRepository.findById(id);
        if(!partyAgent.isPresent()){
            throw new NotFoundException(String.format(notFoundTemplate,SERVICE_NAME));
        }
        return partyAgent.get();
    }

    private PoliticalParty getPoliticalParty(Long id) throws NotFoundException {
        Optional<PoliticalParty> currentPollingUnit = politicalPartyRepository.findById(id);
        if(!currentPollingUnit.isPresent()){
            throw new NotFoundException(String.format(notFoundTemplate,"Political Party"));
        }
        return currentPollingUnit.get();
    }

    private Result getResult(Long id) throws NotFoundException {
        Optional<Result> result = resultRepository.findById(id);
        if(!result.isPresent()){
            throw new NotFoundException(String.format(notFoundTemplate,"Result"));
        }
        return result.get();
    }
    // @Todo
}
