package com.ntr1x.treasure.web.services;

import org.springframework.stereotype.Service;

import lombok.Getter;
import lombok.Setter;

@Service
public class ProfilerService implements IProfilerService {

    @Getter
    @Setter
    private boolean securityDisabled;
}
