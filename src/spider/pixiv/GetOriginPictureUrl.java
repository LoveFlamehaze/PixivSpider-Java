package spider.pixiv;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;

public class GetOriginPictureUrl {
	
	// 从HTML页面获取到原图链接
	public String getPictureUrl(String pictureId) {
		// 图片详情页链接，如https://www.pixiv.net/artworks/60095408
		String getTimeUrl = "https://www.pixiv.net/artworks/" + pictureId;
		// 原图链接，如https://i.pximg.net/img-original/img/2016/11/25/00/18/01/60095408_p0.jpg
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
		// 通过字符串切割获取时间
		String timeHtml = StringUtils.substringBetween(html, "\"original\":\"", "\"}");
		if (timeHtml == null || timeHtml.equals(""))
			return "";
		// 获取HTML网页中的时间部分，如2016\/11\/25\/00\/18\/01\/，并将其分割为字符串数组
		timeParts = StringUtils.substringBetween(timeHtml, "img\\/", "\\/" + pictureId).split("\\\\/");
		// 图片文件类型（jpg、png，暂未测试其他类型图片）
		String pictureType = getPictureType(timeHtml);
		// 拼接为原图链接
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
