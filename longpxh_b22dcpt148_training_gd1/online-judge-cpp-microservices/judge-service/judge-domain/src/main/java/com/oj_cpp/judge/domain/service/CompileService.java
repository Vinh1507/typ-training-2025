package com.oj_cpp.judge.domain.service;

import com.oj_cpp.judge.domain.dto.CompileResult;
import com.oj_cpp.judge.domain.enums.LanguageJudgeEnum;

public interface CompileService {

    CompileResult compile(String workDir, LanguageJudgeEnum language);
}
