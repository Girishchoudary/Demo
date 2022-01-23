package com.dipl.stream.util;

import java.io.IOException;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LiveStreamingDockerUpDown {

	/**
	 * Atrribute this.logger for current class
	 */
	final Logger logger = LoggerFactory.getLogger(this.getClass());


	/**
	 * 
	 * @param meetingID
	 * @return
	 */
	public String runShellScriptForDockerUp(String meetingID, String streamType) {
		// for unix commands
		try {
			logger.info("::runShellScriptForDockerUp().meetingID-> {} streamType {}", meetingID, streamType);

			String[] commandlist = new String[] { "bash", "-l", "-c",
					"docker-compose -f LiveStream/" + meetingID + "_" + streamType + "/docker-compose.yml up -d" };
			logger.info("commandlist {}", Arrays.asList(commandlist));
			Process exec = Runtime.getRuntime().exec(commandlist);

			try {
				exec.waitFor();
			} catch (InterruptedException e) {
				e.printStackTrace();
				return e.getMessage();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
			return e1.getMessage();
		}
		return "success";
	}

	/**
	 * 
	 * @param meetingID
	 * @return
	 */
	public String runShellScriptForDockerDown(String meetingID, String streamType) {
		// for unix commands
		logger.info("::runShellScriptForDockerDown().meetingID-> {} streamType {}", meetingID, streamType);
		try {
			String[] commandlist = new String[] { "bash", "-l", "-c",
					"docker-compose -f LiveStream/" + meetingID + "_" + streamType + "/docker-compose.yml down" };
			logger.info("commandlist {}", Arrays.asList(commandlist));
			Process exec = Runtime.getRuntime().exec(commandlist);
			try {
				exec.waitFor();
			} catch (InterruptedException e) {
				e.printStackTrace();
				return e.getMessage();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
			return e1.getMessage();
		}

//		try {
//			Optional<Meeting> meeting = this.meetingRepository.findBySessionAndDeletedFalse(meetingID);
//
//			if (meeting.isPresent()) {
//				meeting.get().setLiveStreaming(false);
//				this.meetingRepository.save(meeting.get());
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}

		return "success";
	}
	
	
	
	/**
	 * 
	 * @param meetingID
	 * @return
	 */
	public String runShellScriptForDockerUp(String meetingID) {
		System.out.println("ShellScriptLiveStreaming::runShellScriptForDockerUp().meetingID->" + meetingID);
		// for unix commands
		try {
//			Process exec = Runtime.getRuntime().exec(new String[] { "bash", "-l", "-c", "docker-compose up -d" });
			String docker = "docker-compose -f docker-compose-" + meetingID + ".yml up -d";
			Process exec = Runtime.getRuntime().exec(new String[] { "bash", "-l", "-c", docker });

			try {
				exec.waitFor();
			} catch (InterruptedException e) {
				e.printStackTrace();
				return e.getMessage();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
			return e1.getMessage();
		}
		return "success";
	}

	/**
	 * 
	 * @param meetingID
	 * @return
	 */
	public String runShellScriptForDockerDown(String meetingID) {
		System.out.println("ShellScriptLiveStreaming::runShellScriptForDockerDown().meetingID->" + meetingID);
		// for unix commands
		try {
//			Process exec = Runtime.getRuntime().exec(new String[] { "bash", "-l", "-c", "docker-compose down" });
			String docker = "docker-compose -f docker-compose-" + meetingID + ".yml down";
			Process exec = Runtime.getRuntime().exec(new String[] { "bash", "-l", "-c", docker });

			try {
				exec.waitFor();
			} catch (InterruptedException e) {
				e.printStackTrace();
				return e.getMessage();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
			return e1.getMessage();
		}

		return "success";
	}

}
