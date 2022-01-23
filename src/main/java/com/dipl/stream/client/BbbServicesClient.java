package com.dipl.stream.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "bbbSessionServices", url = "${bbbService.url}")
public interface BbbServicesClient {

	/**
	 * @param uri
	 * @return
	 */
	@GetMapping("/jwt/general/getPropertiesData")
	public ResponseEntity<?> getPropertiesData();

//	/**
//	 * @param parametersToReplace
//	 * @param userId
//	 * @param streamID
//	 * @return
//	 */
//	@GetMapping(value = "/nojwt/session/constructdockerfiles")
//	public Map<String, Map<String, String>> construtDockerFiles(@RequestParam Map<String, String> parametersToReplace,
//			@RequestParam long userId, @RequestParam long streamID);
}
