package com.harshbits.ubot.domain;

import java.io.Serializable;
import java.util.Map;

public class OriginalRequest implements Serializable {
	private static final long serialVersionUID = 1L;
	private String source;
	private Map<String, ?> data;

	/**
	 * Get source description string
	 * 
	 * @return <code>null</code> if source isn't defined in a request
	 */
	public String getSource() {
		return source;
	}

	/**
	 * Get data map
	 * 
	 * @return <code>null</code> if data isn't defined in a request
	 */
	public Map<String, ?> getData() {
		return data;
	}

}
