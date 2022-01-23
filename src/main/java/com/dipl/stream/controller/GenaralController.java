package com.dipl.stream.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dipl.stream.service.GeneralService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@RestController
@RequestMapping("/general")
public class GenaralController {
	/**
	 * Attribute generalService
	 */
	@Autowired
	private GeneralService generalService;

	/**
	 * @return
	 */
	@GetMapping("getproperties")
	public ResponseEntity<?> getPropertiesData() {
		return this.generalService.getPropertiesDataServ();
	}

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	@GetMapping
	public String ping() throws Exception {
		return "live Stream services --> pong";
	}

	@PostMapping("/modify")
	public Map<String, Object> getResponse(@RequestBody String request)
			throws JsonMappingException, JsonProcessingException {

//		 response = new HashMap<>();

		JsonObject jsonObject = JsonParser.parseString(request).getAsJsonObject();
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> response = mapper.readValue(request, new TypeReference<Map<String, Object>>() {
		});
		response.put("daysignoffID", System.currentTimeMillis());
		List<Map<String, Object>> listObject = (List<Map<String, Object>>) response.get("screeningDetailsList");
		jsonObject.get("screeningDetailsList").getAsJsonArray().forEach(ben -> {
			JsonObject bene = ben.getAsJsonObject();
			for (int i = 1; i <= 50; i++) {
				Map<String, Object> respon;
				try {
					respon = mapper.readValue(bene.toString(), new TypeReference<Map<String, Object>>() {
					});
					Map<String, Object> benefi = (Map<String, Object>) respon.get("beneficiary");
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					benefi.put("mobileBeneficiaryID", System.currentTimeMillis());
					respon.put("beneficiary", benefi);
					listObject.add(respon);
				} catch (JsonProcessingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		response.put("screeningDetailsList", listObject);

		return response;
	}
}
