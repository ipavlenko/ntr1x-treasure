package com.ntr1x.treasure.files;

import java.io.File;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;

import lombok.Data;

@ApplicationScoped
public class FileService {
	
	@Resource(lookup = "java:app/config/FileService")
	private Config config;
	
	@Data
	public static class Config {
		private String root;
	}

	public File resolve(String path) {
		return new File(config.getRoot(), path);
	}
}
