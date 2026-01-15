package com.vn.Project_Trainning_Week2.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
abstract class BaseController {
    protected final Logger log = LoggerFactory.getLogger(getClass());
}
