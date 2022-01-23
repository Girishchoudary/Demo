package com.dipl.stream.controller;

import java.io.IOException;

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

import com.dipl.stream.service.StreamingService;

@RestController
@RequestMapping("/stream")
public class StreamingController {
	/**
	 * Attribute streamingService
	 */
	@Autowired
	private StreamingService streamingService;
	/**
	 * Atrribute logger for current class
	 */
	final Logger logger = LoggerFactory.getLogger(this.getClass());


	/**
	 * @param request
	 * @return
	 */
	@PostMapping(value = "goforbroadcastNew")
	public ResponseEntity<?> goForLiveStreamBroadCastNew(@RequestBody String request) {
		logger.info("request at ##goForLiveStreamBroadCastNew# with request body request {}", request);
		return this.streamingService.goForLiveStreamBroadCastServ(request);
	}
	
	/**
	 * @param request
	 * @return
	 */
	@PostMapping(value = "endstreaming")
	public ResponseEntity<?> endstreaming(@RequestBody String request) {
		logger.info("request at ##endstreaming# with request body request {}", request);
		return this.streamingService.endstreamingServ(request);
	}

	/**
	 * @param request
	 * @return
	 */
	@GetMapping("/runShellScriptForDockerUp/{meetingID}")
	public ResponseEntity<?> runShellScriptForDockerUp(@PathVariable String meetingID) throws IOException {
		this.logger.debug("runShellScriptForDockerUp::meetingID {}", meetingID);
		return this.streamingService.runShellScriptForDockerUp(meetingID);
	}

	/**
	 * @param request
	 * @return
	 */
	@GetMapping("/runShellScriptForDockerDown/{meetingID}")
	public ResponseEntity<?> runShellScriptForDockerDown(@PathVariable String meetingID) throws IOException {
		this.logger.debug("runShellScriptForDockerDown::meetingID {}", meetingID);
		return this.streamingService.runShellScriptForDockerDown(meetingID);
	}

}
