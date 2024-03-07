package com.findingError.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = "exp.properties")
public class Config {
	@Value("${LogLevel}")
	private String logLevel;
	@Value("${LogLevel1}")
	private String logLevel1;
	@Value("${LogLevel2}")
	private String logLevel2;
	@Value("${LogLevel3}")
	private String logLevel3;
	@Value("${LogLevel4}")
	private String logLevel4;

	public String getLogLevel() {
		return logLevel;
	}

	public String getLogLevel1() {
		return logLevel1;
	}

	public String getLogLevel2() {
		return logLevel2;
	}

	public String getLogLevel3() {
		return logLevel3;
	}

	public String getLogLevel4() {
		return logLevel4;
	}





}
