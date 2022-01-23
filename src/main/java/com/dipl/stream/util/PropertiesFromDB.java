package com.dipl.stream.util;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dipl.stream.service.GeneralService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@SuppressWarnings("unchecked")
public class PropertiesFromDB {

	public static Map<String, String> propertyMap = new HashMap<>();

//	@Value("${sckey}")
//	private String sckey;

	@Autowired
	private GeneralService generalService;

	@Autowired
	private ObjectMapper objectMapper;

	/**
	 * 
	 * @throws Exception
	 */
	public void initRepositotyHashMap() {

		try {
			LinkedHashMap<String, Object> response = (LinkedHashMap<String, Object>) this.generalService
					.getPropertiesDataServ().getBody();
			List<Properties> propertiesList = this.objectMapper.convertValue(response.get("data"),
					new TypeReference<List<Properties>>() {
					});
			propertiesList.forEach(prop -> {
//				propertyMap.put(prop.getName(), prop.getValue());
			});
//			propertyMap.put("sckey", sckey);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param property
	 * @return
	 * @throws Exception
	 */
	public String getPropertiesValue(String property) {

		this.initRepositotyHashMap();
		return PropertiesFromDB.propertyMap.get(property);
	}
}
