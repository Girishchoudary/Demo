package com.dipl.stream.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.dipl.stream.util.LiveStreamingDockerUpDown;
import com.dipl.stream.util.LiveStreamingModifyDockerFile;
import com.dipl.stream.util.ResponseBean;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Service
public class StreamingService {
	/**
	 * Atrribute logger for current class
	 */
	final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private LiveStreamingDockerUpDown liveStreamingDockerUpDown;
	@Autowired
	private LiveStreamingModifyDockerFile liveStreamUpAndDown;

	/**
	 * @param request
	 * @return
	 */
	public ResponseEntity<ResponseBean> goForLiveStreamBroadCastServ(String request) {
		ResponseBean response = new ResponseBean();
		response.setData(null);
		response.setMessage("Live Stream will be started soon..!");
		response.setStatus(HttpStatus.OK);
		try {
			JsonObject requestJsonObject = JsonParser.parseString(request).getAsJsonObject();
			String meetingID = requestJsonObject.get("meetingID").getAsString();
			long streamID = requestJsonObject.get("streamID").getAsLong();
//			long userID = requestJsonObject.get("userID").getAsLong();
			String name = requestJsonObject.get("meetingName").getAsString();
			
			String BBB_STREAM_URL = requestJsonObject.get("BBB_STREAM_URL").getAsString();
			String BBB_USER_NAME = requestJsonObject.get("BBB_USER_NAME").getAsString();
			
			String dockerFilepath = requestJsonObject.get("dockerFilepath").getAsString();
			
			name = name.replaceAll("[^a-zA-Z0-9]", ".").trim();  
			
//			if (this.bbbServicesClient.isMeeting(meetingID)) {
//				this.propertiesFromDB.initRepositotyHashMap();
				//Map<String, String> propertiesMap = PropertiesFromDB.propertyMap;
				Map<String, String> parametersToReplace = new HashMap<>();
				parametersToReplace.put("container_name", "LS" + meetingID + "");
				//parametersToReplace.put("- BBB_URL", propertiesMap.get("bbburl"));
				//parametersToReplace.put("- BBB_SECRET", propertiesMap.get("sckey"));
				parametersToReplace.put("- BBB_MEETING_ID", meetingID + "");
				parametersToReplace.put("- BBB_MEETING_TITLE", name.toString().trim());
				parametersToReplace.put("- BBB_STREAM_URL", BBB_STREAM_URL.trim());
				parametersToReplace.put("- BBB_USER_NAME", BBB_USER_NAME.trim());
				
				Map<String, Map<String, String>> streamsMap = new HashMap<>();
				streamsMap.put(streamID+"", parametersToReplace);
				
//				Map<String, Map<String, String>> streamMapRequest = this.bbbServicesClient
//						.construtDockerFiles(parametersToReplace, userID, streamID);
//				
				this.liveStreamUpAndDown.modifyFileForLiveStreamingMultiple(streamsMap, meetingID + "",dockerFilepath);
//			} else {
//				response.setMessage("Meeting is not started yet");
//			}
		} catch (Exception e) {
			e.printStackTrace();
			this.logger.error("error at ##goForLiveStreamBroadCastServ# " + e.getMessage());
			response.setMessage("Oops! Something went wrong. Try again." + e.getMessage());
			response.setStatus(HttpStatus.EXPECTATION_FAILED);
			response.setData("");
		}
		return ResponseEntity.status(response.getStatus()).body(response);
	}
	
	/**
	 * @param request
	 * @return
	 */
	public ResponseEntity<?> endstreamingServ(String request) {
		ResponseBean response = new ResponseBean();
		response.setData(null);
		response.setMessage("Stream ended successfully");
		response.setStatus(HttpStatus.OK);
		try {
			JsonObject asJsonObject = JsonParser.parseString(request).getAsJsonObject();
			String meetingID = asJsonObject.get("meetingID").getAsString();
			String streamType = asJsonObject.get("streamType").getAsString();
			response.setMessage(
					this.liveStreamingDockerUpDown.runShellScriptForDockerDown(meetingID, streamType));
		} catch (Exception e) {
			this.logger.error("error at ##endstreamingServ# " + e.getMessage());
			response.setMessage("Oops! Something went wrong. Try again." + e.getMessage());
			response.setStatus(HttpStatus.EXPECTATION_FAILED);
			response.setData("");
		}
		return ResponseEntity.status(response.getStatus()).body(response);
	}

	
	/**
	 * 
	 * @param meetingID 
	 * @return
	 */
	public ResponseEntity<?> runShellScriptForDockerUp(String meetingID) {
		this.logger.info("General Service ##runShellScriptForDockerUp# ");
		ResponseBean response = new ResponseBean();
		response.setData(null);
		response.setMessage("runShellScriptForDockerUp successfully ");
		response.setStatus(HttpStatus.OK);
		try {
			response.setMessage(liveStreamingDockerUpDown.runShellScriptForDockerUp(meetingID));
		} catch (Exception e) {
			this.logger.error("error at ##runShellScriptForDockerUp# " + e.getMessage());
			e.printStackTrace();
			response.setMessage("Oops! Something went wrong. Try again.");
			response.setStatus(HttpStatus.EXPECTATION_FAILED);
		}
		return ResponseEntity.status(response.getStatus()).body(response);
	}
	
	/**
	 * 
	 * @param meetingID 
	 * @return
	 */
	public ResponseEntity<?> runShellScriptForDockerDown(String meetingID) {
		this.logger.info("General Service ##runShellScriptForDockerDown# ");
		ResponseBean response = new ResponseBean();
		response.setData(null);
		response.setMessage("runShellScriptForDockerDown successfully ");
		response.setStatus(HttpStatus.OK);
		try {
			response.setMessage(liveStreamingDockerUpDown.runShellScriptForDockerDown(meetingID));
		} catch (Exception e) {
			this.logger.error("error at ##runShellScriptForDockerDown# " + e.getMessage());
			e.printStackTrace();
			response.setMessage("Oops! Something went wrong. Try again.");
			response.setStatus(HttpStatus.EXPECTATION_FAILED);
		}
		return ResponseEntity.status(response.getStatus()).body(response);
	}
}
