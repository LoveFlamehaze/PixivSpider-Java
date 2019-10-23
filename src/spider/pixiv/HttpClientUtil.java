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
		// ����������ʹ�ô���ķ�ʽ����ʹ�ô����ע�͵�
		HttpHost proxy = new HttpHost("127.0.0.1", 1080, "http");
		RequestConfig defaultRequestConfig = RequestConfig.custom().setProxy(proxy).build();
		httpClient = HttpClients.custom().setDefaultRequestConfig(defaultRequestConfig).build();
		// ��ʹ�ô���ʹ���һ��
//		httpClient = HttpClients.createDefault();
	}

	// ˫�ؼ��������ģʽ����Լ��Դ
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

	// ����get����
	public CloseableHttpResponse doGet(String url) throws ClientProtocolException, IOException {
		// ��ֹ������
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
