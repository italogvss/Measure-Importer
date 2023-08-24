package itaipu.gss.sthimporter.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.client.WebClient;

import itaipu.gss.framework.commons.AppProperties;
import itaipu.gss.framework.exception.AppException;
import itaipu.gss.framework.log.AppLogger;
import itaipu.gss.sthimporter.dto.BDHConfiguracaoSTHDto;
import itaipu.gss.sthimporter.dto.BDHLoginByTokenDTO;
import itaipu.gss.sthimporter.dto.BDHMeasureListDto;

public class DAOBDHWebService {
	
	private static final String TIMESTAMP_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";

	private static final String BDH_BASE_URL = AppProperties.getString("bdh-baseURL");
	private static final String BDH_TOKEN = AppProperties.getString("bdh-token");
	
	static {
		AppLogger.logInfoMessage("BDH base URL: " + BDH_BASE_URL);
	}
	
	private Cookie cookie;

	private Cookie login() throws AppException {
		String sessionID = WebClient.create(BDH_BASE_URL).path("/startSession")
													.get(String.class);

		Cookie cookie = new Cookie("sessionID", sessionID);

		Response response = WebClient.create(BDH_BASE_URL).path("/loginByToken")
								.type("application/json")
								.post(new BDHLoginByTokenDTO(BDH_TOKEN, sessionID));
		
		handleHttpResponse(response, 200, "BDH logged in successfully", "BDH login failed");
				
		return cookie;
	}

	private Cookie getCookie() throws AppException {
		if (cookie == null) {
			this.cookie = login();
		}
		return cookie;
	}

	public List<BDHConfiguracaoSTHDto> getListConfiguracaoSTH(String importerId, Date importTimestamp) throws AppException {
		try {
			SimpleDateFormat timestampFormatter = new SimpleDateFormat(TIMESTAMP_PATTERN);
			
			@SuppressWarnings("unchecked")
			List<BDHConfiguracaoSTHDto> sthConfigDtoList = (List<BDHConfiguracaoSTHDto>) WebClient.create(BDH_BASE_URL)					
					.path("/configuracaoSTH/list")
					.cookie(getCookie())
					.query("importer-id", importerId)
					.query("import-timestamp", timestampFormatter.format(importTimestamp))
					.type("application/json")
					.getCollection(BDHConfiguracaoSTHDto.class);
			return sthConfigDtoList;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AppException("BDH failed to get ConfiguracaoSTH list from BDH. " + e.getMessage());
		}
	}
	
	public void logout() throws AppException {
		Response response = WebClient.create(BDH_BASE_URL).path("/logout").cookie(getCookie()).get();
		handleHttpResponse(response, 204, "Logout from BDH successfully.", "BDH logout failed");
	}
	
	public void remotePHPLog(BDHMeasureListDto measureListDto) {
		Response response = WebClient.create("http://chi351/post-test/teste.php?id=STH")
				.type("application/json")
				.post(measureListDto);
			
		System.out.println("Response PHP: " + response.getStatus());
	}
	
	public int save(BDHMeasureListDto measureListDto) throws AppException {
//		remotePHPLog(measureListDto);
		
		Response response = WebClient.create(BDH_BASE_URL).path("/post-measures").cookie(getCookie())
				.type("application/json").post(measureListDto);
		
		handleHttpResponse(response, 200, "Data sent to BDH successfully.",	"BDH post measures failed.");
		
		return Integer.valueOf(response.readEntity(String.class));
	}
	
	private void handleHttpResponse(Response response, Integer sucessCode, String sucessMessage, String errorMessage) throws AppException {
		if (response.getStatus() == sucessCode) {
			AppLogger.logInfoMessage(sucessMessage);
		} else {
			throw new AppException(errorMessage
					+ " Response status: " + response.getStatus()
					+ ". Response body: " + response.readEntity(String.class));
		}
	}
	

}
