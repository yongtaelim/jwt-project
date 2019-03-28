package hyperledger_fabric.platform.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class HttpConnection {
	private RequestConfig config;
	private int timeout = 300;
	
	public HttpConnection() {
		config = RequestConfig.custom().setConnectTimeout(timeout * 1000)
				.setConnectionRequestTimeout(timeout * 1000).setSocketTimeout(timeout * 1000).build();
	}
	
	public Map<String, Object> send(String method, Map<String, Object> paramMap) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config)
				.setConnectionManagerShared(true).build();
		String url = PropertiesUtil.getString("send.url");
		
		ObjectMapper objectMapper = new ObjectMapper();
		String execute = "{}";
		
		HttpPost httpPost = new HttpPost(url+method+".do");
		StringEntity reqEntity;
		try {
			reqEntity = new StringEntity(objectMapper.writeValueAsString(paramMap), ContentType.APPLICATION_JSON);
		} catch (Exception e1) {
			e1.printStackTrace();
			throw new RuntimeException(e1);
		}
		httpPost.setEntity(reqEntity);
		httpPost.setHeaders(buildHeaders());
		
		try {
			execute = httpClient.execute(httpPost, new BasicResponseHandler());
			resultMap = objectMapper.readValue(execute, new TypeReference<Map<String, Object>>() {});
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			try {
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
		return resultMap;
	}
	
	private Header[] buildHeaders() {
		List<Header> headers = new ArrayList<>();
		headers.add(new BasicHeader("Content-Type", "application/json; charset=UTF-8"));
		return headers.toArray(new Header[0]);
	}
}
