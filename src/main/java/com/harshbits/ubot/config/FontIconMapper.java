package com.harshbits.ubot.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This method maps country 
 * <p>
 * @author harshbhavsar
 * @since 1.0.0
 */
@Component
@ConfigurationProperties
@NoArgsConstructor
@Data
public class FontIconMapper {
	
	private Map<Integer, String> fontIconMap = new HashMap<>();

	/**
	 * This map returns, weather font icons
	 * 
	 * @param condition
	 * 
	 * @return, appropriate font icon
	 */
	public String getFontIcon(Integer conditionCode) {
		return fontIconMap.get(conditionCode).split(",")[1].trim();
	}
	
}