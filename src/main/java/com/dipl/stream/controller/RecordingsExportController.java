package com.dipl.stream.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dipl.stream.service.RecordingsExportToMP4;

/**
 * 
 * @author Rajeshwar.y
 *
 */
@RestController
@RequestMapping("/recordingsexport")
public class RecordingsExportController {

	/**
	 * Attribute streamingService
	 */
	@Autowired
	private RecordingsExportToMP4 recordingsExportToMP4;
	/**
	 * Atrribute logger for current class
	 */
	final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * @param request
	 * @return
	 */
	@PostMapping(value = "convert2mp4/")
	public ResponseEntity<?> convert2mp4(@RequestBody String request, HttpServletRequest httprequest) {
		logger.info("request at ##exporttomp4# with request body request {}", request);
		return this.recordingsExportToMP4.processRecordingsExportToMP4(request);
	}
	
	/**
	 * @param request
	 * @param response
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	@GetMapping("getfile/{fileName}")
	public ResponseEntity<?> getMP4Files(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("fileName") String fileName) throws Exception {
		this.logger.debug("request at ##getMP4Files# with request body " + fileName);
		return this.recordingsExportToMP4.getMP4Files(request, response, fileName);
	}

}
