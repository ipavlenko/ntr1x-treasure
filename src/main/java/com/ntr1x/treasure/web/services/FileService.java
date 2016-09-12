package com.ntr1x.treasure.web.services;

import java.io.File;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

@Service
public class FileService implements IFileService {

	@Configuration
	public static class Config {
	    
	    @Value("${app.files.root}")
		public String root;
	}
	
	@Inject
    private Config config;

	@Override
	public File resolve(String path) {
		return new File(config.root, path);
	}
}
