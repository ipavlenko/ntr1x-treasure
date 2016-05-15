package com.ntr1x.treasure.globals;
//package com.example.treasure.globals;
//
//import javax.annotation.Resource;
//import javax.enterprise.context.ApplicationScoped;
//
//import lombok.Data;
//
//@ApplicationScoped
//public class GlobalService implements IGlobalService {
//	
//	@Resource(lookup = "java:app/config/GlobalService")
//	private Config config;
//	
//	@Data
//	public static class Config {
//		private String root;
//		private String cookieDomain;
//		private String cookieName;
//	}
//
//	@Override
//	public String root() {
//		return config.root;
//	}
//	
//	@Override
//	public String cookieDomain() {
//		return config.cookieDomain;
//	}
//	
//	@Override
//	public String cookieName() {
//		return config.cookieName;
//	}
//}
