package spider.pixiv;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;

public class GetOriginPictureUrl {
	
	// ��HTMLҳ���ȡ��ԭͼ����
	public String getPictureUrl(String pictureId) {
		// ͼƬ����ҳ���ӣ���https://www.pixiv.net/artworks/60095408
		String getTimeUrl = "https://www.pixiv.net/artworks/" + pictureId;
		// ԭͼ���ӣ���https://i.pximg.net/img-original/img/2016/11/25/00/18/01/60095408_p0.jpg
		String url = "https://i.pximg.net/img-original/img/";
		String[] timeParts;
		String html = "";
		HttpClientUtil hcu = HttpClientUtil.getInstance();
		try {
			CloseableHttpResponse response = hcu.doGet(getTimeUrl);
			HttpEntity entity = response.getEntity();
			html = EntityUtils.toString(entity, "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		// ͨ���ַ����и��ȡʱ��
		String timeHtml = StringUtils.substringBetween(html, "\"original\":\"", "\"}");
		if (timeHtml == null || timeHtml.equals(""))
			return "";
		// ��ȡHTML��ҳ�е�ʱ�䲿�֣���2016\/11\/25\/00\/18\/01\/��������ָ�Ϊ�ַ�������
		timeParts = StringUtils.substringBetween(timeHtml, "img\\/", "\\/" + pictureId).split("\\\\/");
		// ͼƬ�ļ����ͣ�jpg��png����δ������������ͼƬ��
		String pictureType = getPictureType(timeHtml);
		// ƴ��Ϊԭͼ����
		for (String string : timeParts) {
			url = url + string + "/";
		}
		url = url + pictureId + "_p0." + pictureType;
		return url;
	}

	public String getPictureType(String s) {
		String pictureType = s.substring(s.lastIndexOf('.') + 1);
		return pictureType;
	}
}
