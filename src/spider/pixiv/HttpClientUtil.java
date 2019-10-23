package spider.pixiv;

import java.io.IOException;

import org.apache.http.HttpHost;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class HttpClientUtil {

	private volatile static HttpClientUtil instance;
	private CloseableHttpClient httpClient;
	public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64 ) "
			+ "AppleWebKit/537.36 (KHTML, like Gecko) " + "Chrome/64.0.3282.186 Safari/537.36";
	public static final String REFERER = "https://accounts.pixiv.net/login?lang=zh&source=pc&view_type=page&ref=wwwtop_accounts_index";

	private HttpClientUtil() {
		// 以下三行是使用代理的方式，不使用代理就注释掉
		HttpHost proxy = new HttpHost("127.0.0.1", 1080, "http");
		RequestConfig defaultRequestConfig = RequestConfig.custom().setProxy(proxy).build();
		httpClient = HttpClients.custom().setDefaultRequestConfig(defaultRequestConfig).build();
		// 不使用代理就打开下一行
//		httpClient = HttpClients.createDefault();
	}

	// 双重检查锁单例模式，节约资源
	public static HttpClientUtil getInstance() {
		if (instance == null) {
			synchronized (HttpClientUtil.class) {
				if (instance == null) {
					instance = new HttpClientUtil();
				}
			}
		}
		return instance;
	}

	// 处理get请求
	public CloseableHttpResponse doGet(String url) throws ClientProtocolException, IOException {
		// 防止反爬虫
		String[] headers = {"User-Agent", USER_AGENT, "Referer", REFERER};
		return doGet(url, headers);
	}
	
	public CloseableHttpResponse doGet(String url, String[] headers) throws ClientProtocolException, IOException {
		CloseableHttpResponse response = null;
		HttpGet httpGet = new HttpGet(url);
		for(int i = 0; i < headers.length; i += 2) {
			httpGet.addHeader(headers[i], headers[i + 1]);
		}
		response = httpClient.execute(httpGet);
		return response;
	}

}
