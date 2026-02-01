package com.oj_cpp.judge.domain.service;

public interface FileService {

    String saveCodeToFile(String code, String typeFile, Long submissionId);

    void deleteWorkDir(String path);
}
