package com.dipl.stream.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.stereotype.Service;

@Service
public class Utility {

//	@Autowired
//	private RestTemplate restTemplate;
	
	// Encodes a URL dencoded string using `UTF-8`
	public String encodeValue(String value) {
		try {
			return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
		} catch (UnsupportedEncodingException ex) {
			throw new RuntimeException(ex.getCause());
		}
	}

	// Decodes a URL encoded string using `UTF-8`
	public  String decodeValue(String value) {
		try {
			return URLDecoder.decode(value, StandardCharsets.UTF_8.toString());
		} catch (UnsupportedEncodingException ex) {
			throw new RuntimeException(ex.getCause());
		}
	}
	
//	/**
//	 * 
//	 * @param createLink
//	 * @return
//	 */
//	public  String callBBBAPI(String createLink) {
//		URI uri = null;
//		try {
//			uri = new URI(createLink);
//		} catch (URISyntaxException e) {
//			e.printStackTrace();
//		}
////		System.out.println(createLink);
//		HttpHeaders headers = new HttpHeaders();
//		headers.add("Content-Type", "application/x-www-form-urlencoded");
//		return restTemplate.getForObject(uri, String.class);
//
//	}
	
}
