package com.edunge.srtool.service;

import com.edunge.srtool.exceptions.NotFoundException;
import com.edunge.srtool.model.VotingLevel;
import com.edunge.srtool.response.VotingLevelResponse;

public interface VotingLevelService {
    VotingLevelResponse saveVotingLevel(VotingLevel votingLevel) throws NotFoundException;
    VotingLevelResponse findVotingLevelById(Long id) throws NotFoundException;
    VotingLevelResponse findVotingLevelByCode(String code) throws NotFoundException;
    VotingLevelResponse editVotingLevel(Long id, VotingLevel votingLevel) throws NotFoundException;
    VotingLevelResponse deleteVotingLevelById(Long id) throws NotFoundException;
    VotingLevelResponse findAll() ;
}
