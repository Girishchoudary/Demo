package com.dipl.stream.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.dipl.stream.client.BbbServicesClient;

@Service
public class GeneralService {

	@Autowired
	private BbbServicesClient bbbServicesClient;

	public ResponseEntity<?> getPropertiesDataServ() {
		return this.bbbServicesClient.getPropertiesData();
	}

}
