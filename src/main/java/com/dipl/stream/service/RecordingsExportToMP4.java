package com.dipl.stream.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLConnection;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.dipl.stream.util.PropertiesFromDB;
import com.dipl.stream.util.ResponseBean;
import com.dipl.stream.util.Utility;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * 
 * @author Rajeshwar.y
 *
 */
@Service
public class RecordingsExportToMP4 {

	final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private Utility utility;

	@Autowired
	private PropertiesFromDB propertiesFromDB;

	/**
	 * @param request
	 * @return
	 */
	public ResponseEntity<ResponseBean> processRecordingsExportToMP4(String request) {

		ResponseBean response = new ResponseBean();
		response.setData(null);
		response.setMessage("Export recording file to MP4 will be started soon..!");
		response.setStatus(HttpStatus.OK);
		try {
			JsonObject requestJsonObject = JsonParser.parseString(request).getAsJsonObject();
			String getPlaybackUrl = requestJsonObject.get("getPlaybackUrl").getAsString();
			String recordingID = requestJsonObject.get("recordingID").getAsString();
			String status = null;

			if (requestJsonObject.get("status").isJsonNull())
				status = "";
			else
				status = requestJsonObject.get("status").getAsString();

			boolean checkMP4File = this.checkMP4File(recordingID);

			if (checkMP4File) {
				response.setMessage("File is ready to download");
				return ResponseEntity.status(response.getStatus()).body(response);
			}

			if ("File conversion is in progress".equalsIgnoreCase(status)) {
				response.setMessage(status);
				return ResponseEntity.status(response.getStatus()).body(response);
			}

			this.runShellScriptExportNew(getPlaybackUrl, recordingID);

		} catch (Exception e) {
			this.logger.error("error at ##processRecordingsExportToMP4# " + e.getMessage());
			e.printStackTrace();
			response.setMessage("Oops! Something went wrong. Try again." + e.getMessage());
			response.setStatus(HttpStatus.EXPECTATION_FAILED);
			response.setData("");
		}
		return ResponseEntity.status(response.getStatus()).body(response);
	}

	/**
	 * 
	 * @param request
	 * @param response
	 * @param recordID
	 * @return
	 */
	public boolean checkMP4File(String recordID) {
		try {
			this.propertiesFromDB.initRepositotyHashMap();
			String recordingsPath = PropertiesFromDB.propertyMap.get("recordingsPath");
			File uploadDir = new File(".");
			String absolutePath = uploadDir.getAbsolutePath();
			String finalPath = absolutePath + File.separator + recordingsPath + File.separator + recordID + ".mp4";
			System.out.println("finalPath-->" + finalPath);
			File file = new File(finalPath);
			if (file.exists()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			logger.debug("getFile# exception " + e.getMessage());
			e.printStackTrace();

		}
		return false;
	}

//	private synchronized Process runInScreen(String sessionName, String command) throws IOException {
//	    return new ProcessBuilder("screen", "-DmS", sessionName, "bash", "-c", command).inheritIO().start();
//	}

//	String[] args = new String[] {"/bin/bash", "-c", "your_command", "with", "args"};
//	Process proc = new ProcessBuilder(args).start();
//	

	/**
	 * 
	 * @param meetingID
	 * @param recordingID
	 * @return
	 */
	private String runShellScriptExportNew(String getPlaybackUrl, String recordingID) {
		// for unix commands
		System.out.println("runShellScriptExportNew()runShellScriptExport::begin->");
		try {
			String url = utility.decodeValue(getPlaybackUrl) + " " + recordingID + ".webm 0 true";
			System.out.println("url--->" + url);
//			String[] commandlist = new String[] {"screen","-dm", "bash", "-l", "-c", "ls /root/bbb-recorder", "node export.js " + url };
//	        Process process = new ProcessBuilder(commandlist).start();

			String[] commandlist = new String[] { "screen", "-dm", "bash", "-l", "-c", "node export.js " + url };
			System.out.println("commandlist-->" + Arrays.asList(commandlist));
			Process exec = Runtime.getRuntime().exec(commandlist, null, new File("/root/bbb-recorder"));
			printResults(exec);

		} catch (Exception e1) {
			System.out.println("RecordingsExportToMP4()runShellScriptExport::failed->");
			e1.printStackTrace();
			return "falied";
		}
		System.out.println("RecordingsExportToMP4()runShellScriptExport::end->");
		return "success";
	}

	public void printResults(Process process) throws IOException {
		System.out.println("printResults");
		BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		String line = "";
		while ((line = reader.readLine()) != null) {
			System.out.println(line);
		}
	}

	/**
	 * 
	 * @param meetingID
	 * @param recordingID
	 * @return
	 */
	private String runShellScriptExport_Old(String getPlaybackUrl, String recordingID) {
		// for unix commands
		System.out.println("RecordingsExportToMP4_old()runShellScriptExport::begin->");
		try {
			String url = utility.decodeValue(getPlaybackUrl) + " " + recordingID + ".webm 0 true";
			System.out.println("url--->" + url);
			String[] commandlist = new String[] { "screen", "-dm", "bash", "-l", "-c", "node export.js " + url };
			System.out.println("commandlist-->" + Arrays.asList(commandlist));
			Process exec = Runtime.getRuntime().exec(commandlist, null, new File("/root/bbb-recorder"));

			try {
				exec.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
			return "falied";
		}
		System.out.println("RecordingsExportToMP4()runShellScriptExport_old::end->");
		return "success";
	}

	/**
	 * @param recordingID
	 * @param request
	 * @return
	 */
//	public ResponseEntity<ResponseBean> processRecordingsConvert2mp4(String getPlaybackUrl, String recordingID) {
//
//		ResponseBean response = new ResponseBean();
//		response.setData(null);
//		response.setMessage("Converting a recording file to mp4 will be started soon..!");
//		response.setStatus(HttpStatus.OK);
//		try {
//			this.runShellScriptExportNew(getPlaybackUrl, recordingID);
//		} catch (Exception e) {
//			this.logger.error("error at ##processRecordingsExportToMP4# " + e.getMessage());
//			e.printStackTrace();
//			response.setMessage("Oops! Something went wrong. Try again." + e.getMessage());
//			response.setStatus(HttpStatus.EXPECTATION_FAILED);
//			response.setData("");
//		}
//		return ResponseEntity.status(response.getStatus()).body(response);
//	}

	/**
	 * 
	 * @param request
	 * @param response
	 * @param fileName
	 * @return
	 */
	public ResponseEntity<?> getMP4Files(HttpServletRequest request, HttpServletResponse response, String fileName) {
		ResponseBean responseBean = new ResponseBean();
		responseBean.setStatus(HttpStatus.OK);
		InputStream inputStream = null;
		String alertMessage = "";
		try {
			this.propertiesFromDB.initRepositotyHashMap();
			String recordingsPath = PropertiesFromDB.propertyMap.get("recordingsPath");
//			String path = property + File.separator + fileName;
//			File file = new File(path);
			File uploadDir = new File(".");
			String absolutePath = uploadDir.getAbsolutePath();
			String finalPath = absolutePath + File.separator + recordingsPath + File.separator + fileName;
			System.out.println("getMP4Files()finalPath-->" + finalPath);
			File file = new File(finalPath);
			if (file.exists()) {
				System.out.println(file.getAbsolutePath());
				System.out.println("is path exist " + file.exists());
//			if (file.exists()) {
				// get the mimetype
				String mimeType = URLConnection.guessContentTypeFromName(file.getName());
				if (mimeType == null) {
					// unknown mimetype so set the mimetype to application/octet-stream
					mimeType = "application/octet-stream";
				}
				System.out.println(mimeType);
				response.setContentType(mimeType);
				response.setHeader("Content-Disposition", "attachment; filename=" + fileName.replace(" ", "_"));
				inputStream = new FileInputStream(file);
				IOUtils.copy(inputStream, response.getOutputStream());
				response.flushBuffer();
			} else {
				alertMessage = "<script>alert('File not found');window.close();</script>";
			}
		} catch (Exception e) {
			logger.debug("getFile# exception " + e.getMessage());
			e.printStackTrace();
		} finally {
			if (inputStream != null)
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return ResponseEntity.status(responseBean.getStatus()).body(alertMessage);

	}

}
