package com.dipl.stream.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import com.google.common.io.Files;

@Component
@EnableAsync
public class LiveStreamingModifyDockerFile {

	/**
	 * Atrribute logger for current class
	 */
	final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * Attribute propertiesFromDB
	 */
	@Autowired
	private PropertiesFromDB propertiesFromDB;

	@Autowired
	private LiveStreamingDockerUpDown liveStreamingDockerUpDown;

	@Autowired
	private LiveStreamingDeleteOldFoldersFiles liveStreamingDeleteOldFoldersFiles;

	/**
	 * @param meetingID
	 * @throws IOException
	 */
	@Async
	public ResponseEntity<?> modifyFileForLiveStreaming(String meetingID) throws IOException {
		InputStream inputStream = null;
		this.logger.info("General Service ##runShellScriptForDockerUp# ");
		ResponseBean response = new ResponseBean();
		response.setData(null);
		response.setMessage("runShellScriptForDockerUp successfully ");
		response.setStatus(HttpStatus.OK);

		String dockerFilepath = this.propertiesFromDB.getPropertiesValue("dockerFilepath");

		synchronized (this) {
			try {
				File directory = new File(dockerFilepath);
				if (directory.exists()) {
					inputStream = new FileInputStream(directory);
					String content = null;
//							new String(inputStream.readAllBytes());
					String property = this.getPropertyFromDockerFile(dockerFilepath, "- BBB_MEETING_ID");
					if (property != null) {
						String replacedContent = content.replaceAll(property, "      - BBB_MEETING_ID=" + meetingID);
						Files.write(replacedContent.getBytes(), directory);
						// return ResponseEntity.ok("successfully updated");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				this.logger.error("error at ##modifyFileAndAppendMeetingID# " + e.getMessage());
			} finally {
				if (inputStream != null)
					inputStream.close();
			}

			try {
				response.setMessage(liveStreamingDockerUpDown.runShellScriptForDockerDown(meetingID));
				Thread.sleep(60000);
				response.setMessage(liveStreamingDockerUpDown.runShellScriptForDockerUp(meetingID));
			} catch (Exception e) {
				this.logger.error("error at ##runShellScriptForDockerUp# " + e.getMessage());
				e.printStackTrace();
				response.setMessage("Oops! Something went wrong. Try again.");
				response.setStatus(HttpStatus.EXPECTATION_FAILED);
			}
		} // synchronized (this) end
		return ResponseEntity.status(response.getStatus()).body(response);
	}

	/**
	 * @param path
	 * @param property
	 * @return
	 * @throws IOException
	 */
	private String getPropertyFromDockerFile(String path, String property) {
		BufferedReader reader = null;
		try {
			System.out.println("getPropertyFromDockerFile::"+property.trim()+"");
			reader = new BufferedReader(new FileReader(path));
			String line = null;
//			String[] parts = null;
			while ((line = reader.readLine()) != null) {
				if (line.startsWith("#")) {
					// skip comments
					continue;
				}
//				System.out.println(property);
//				parts = line.split("=");
//				if(parts!=null && parts.length>0 && parts[0].strip().equalsIgnoreCase(property) ) {
//					System.out.println(property);
//				}
				
				if(line.indexOf(property.trim())>0) {
					return line;
				}
				
//				if (parts.length >= 2) {
//					if (parts[0] != null && parts[0].strip().equalsIgnoreCase(property.strip())) {
//						return line;
//					}
//				} else {
//					parts = line.split(":", 2);
//					if (parts.length >= 2) {
//						if (parts[0] != null && parts[0].strip().equalsIgnoreCase(property.strip())) {
//							return line;
//						}
//					}
//				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (reader != null)
				try {
					reader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		return null;
	}

	/**
	 * @param meetingID
	 * @param dockerFilepath 
	 * @throws IOException
	 */
	@Async
	public ResponseEntity<?> modifyFileForLiveStreamingMultiple(Map<String, Map<String, String>> replaceContent,
			String meetingID, String dockerFilepath) throws IOException {
		InputStream inputStream = null;
		this.logger.info("General Service ##runShellScriptForDockerUp# ");
		ResponseBean response = new ResponseBean();
		response.setData(null);
		response.setMessage("runShellScriptForDockerUp successfully ");
		response.setStatus(HttpStatus.OK);
//		String dockerFilepath = this.propertiesFromDB.getPropertiesValue("dockerFilepath");
		File directory = new File(dockerFilepath);

		synchronized (this) {
			try {
				if (directory.exists()) {
					inputStream = new FileInputStream(directory);
					String[] content = null;
//						{ new String(inputStream.readAllBytes()) };
					replaceContent.forEach((k, v) -> {
						this.constructDirectorys(meetingID, String.join(",", replaceContent.keySet()));
						File uploadDir = new File(".");
						String absolutePath = uploadDir.getAbsolutePath();
						String checkMainFolderExistance = absolutePath + File.separator + "LiveStream" + File.separator
								+ meetingID + "_" + k;

						System.out.println(checkMainFolderExistance);

//						String checkMainFolderExistance = uploadDir.getAbsolutePath() + File.separator +"LiveStream"+File.separator+ meetingID + "_" + k;

						File directoryToPlaceFile = new File(
								checkMainFolderExistance + File.separator + "docker-compose.yml");

						System.out.println(directoryToPlaceFile.getAbsolutePath());
						if (!directoryToPlaceFile.exists()) {
							try {
								directoryToPlaceFile.createNewFile();
							} catch (IOException e) {
							}
						}
						v.forEach((k1, v1) -> {
							this.logger.info("replace content key: {} value: {}", k1, v1);
							String returnedLine = this.getPropertyFromDockerFile(dockerFilepath, k1);
							this.logger.info("returnedLine: {} ", returnedLine);

							if (returnedLine != null) {
								System.out.println("---------in returnedLine "+returnedLine);
								if (k1.equals("container_name")) {
									content[0] = content[0].replaceAll(returnedLine,
											"    " + k1 + ": LS_" + meetingID + "_" + k);
								} else {
									System.out.println( "      " + k1 + "=" + v1);
									content[0] = content[0].replaceAll(returnedLine, "      " + k1 + "=" + v1);
								}
							}
						});
						try {
//							this.logger.info("absoluth path for file creation {}", checkMainFolderExistance);
							Files.write(content[0].getBytes(), directoryToPlaceFile);
						} catch (IOException e) {
							e.printStackTrace();
						}
					});
				}
			} catch (Exception e) {
				e.printStackTrace();
				this.logger.error("error at ##modifyFileAndAppendMeetingID# " + e.getMessage());
			} finally {
				if (inputStream != null)
					inputStream.close();
			}

			try {

				File uploadDir = new File(".");
				String absolutePath = uploadDir.getAbsolutePath();
				String checkMainFolderExistance = absolutePath + File.separator + "LiveStream";
				File directoryDele = new File(checkMainFolderExistance);
				liveStreamingDeleteOldFoldersFiles.removeDirectory(directoryDele);
			} catch (Exception e) {
				e.printStackTrace();
			}

			try {

				replaceContent.forEach((k, v) -> {
					response.setMessage(liveStreamingDockerUpDown.runShellScriptForDockerDown(meetingID, k));
					try {
						Thread.sleep(30000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					response.setMessage(liveStreamingDockerUpDown.runShellScriptForDockerUp(meetingID, k));
				});

			} catch (Exception e) {
				this.logger.error("error at ##runShellScriptForDockerUp# " + e.getMessage());
				e.printStackTrace();
				response.setMessage("Oops! Something went wrong. Try again.");
				response.setStatus(HttpStatus.EXPECTATION_FAILED);
			}
		} // synchronized (this) end
		return ResponseEntity.status(response.getStatus()).body(response);
	}

	/**
	 * @param meetingID
	 * @param commaSeparatedSocialStreams
	 */
	public void constructDirectorys(String meetingID, String commaSeparatedSocialStreams) {
		try {
			File uploadDir = new File(".");
			this.logger.info("absoluth path for bbb services {}", uploadDir.getAbsolutePath());
			String absolutePath = uploadDir.getAbsolutePath();
			String checkMainFolderExistance = absolutePath + File.separator + "LiveStream";
			File checkFolderExistance = new File(checkMainFolderExistance);
			this.logger.info("is path {} exists  {}", checkFolderExistance.getAbsolutePath(),
					checkFolderExistance.exists());
			if (!checkFolderExistance.exists()) {
				checkFolderExistance.mkdir();
			}
			List<String> streamsList = Arrays.asList(commaSeparatedSocialStreams.split(","));
			streamsList.forEach(streamType -> {
				String subFolderStream = checkFolderExistance.getAbsolutePath() + File.separator + meetingID + "_"
						+ streamType;
				this.logger.info("sub folder path {}", subFolderStream);
				File subFolderStreamFile = new File(subFolderStream);
				if (!subFolderStreamFile.exists()) {
					this.logger.info("created folder for first time path {}", subFolderStream);
					subFolderStreamFile.mkdir();
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
