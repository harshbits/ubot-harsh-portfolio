package com.harshbits.ubot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IntentConfiguration {

	@ConfigurationProperties(prefix = "intentTypeList")
	public static class IgnoreIntents {

		private String ignoreIntents;

		public String getIgnoreIntents() {
			return ignoreIntents;
		}

		public void setIgnoreIntents(String ignoreIntents) {
			this.ignoreIntents = ignoreIntents;
		}

	}

}
