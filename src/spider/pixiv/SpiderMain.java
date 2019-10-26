package spider.pixiv;

import java.io.File;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONObject;

public class SpiderMain {

	public static void main(String[] args) {
		// 要存储的本地路径，不存在则创建
		String path = "D:/images/pixiv/";
		SpiderMain spider = new SpiderMain();
		Scanner s = null;
		System.out.println("输入1则根据图片id下载原图，输入2则根据画师id下载全部作品原图：");
		try {
			s = new Scanner(System.in);
			String option = s.next();
			long startTime = System.currentTimeMillis();
			if (option.equals("1")) {
				System.out.println("请输入图片id：");
				int pid = s.nextInt();
				spider.downloadByPictureId(pid, path);
			} else if (option.equals("2")) {
				System.out.println("请输入画师id：");
				int uid = s.nextInt();
				spider.downloadByUserId(uid, path);
			}
			System.out.println("保存完成");
			long endTime = System.currentTimeMillis();
			System.out.println("程序运行时间：" + (endTime - startTime) + "ms");
		} catch (InputMismatchException e) {
			System.out.println("请输入正确的数字！");
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (s != null) {
				s.close();
			}
		}
	}

	public void downloadByPictureId(int pid, String path) {
		GetOriginPictureUrl getPicUrl = new GetOriginPictureUrl();

		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		String url = getPicUrl.getPictureUrl(pid);
		if (url.equals("")) {
			System.out.println("未找到图片！");
			return;
		}
		String pictureType = getPicUrl.getPictureType(url);
		String fileName = path + pid + "_p0." + pictureType;
		// 判断是否已有同名文件，减少下载次数
		File file = new File(fileName);
		if (file.exists()) {
			System.out.println("文件已存在！");
			return;
		}
		GetPicture getPic = new GetPicture();
		try {
			getPic.downloadPicture(url, file);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

	public void downloadByUserId(int uid, String path) throws ClientProtocolException, IOException {
		int pid;
		String url = String.format("https://www.pixiv.net/ajax/user/%d/profile/all", uid);
		HttpClientUtil client = HttpClientUtil.getInstance();
		CloseableHttpResponse response = client.doGet(url);
		HttpEntity entity = response.getEntity();
		String jsonString = EntityUtils.toString(entity, "utf-8");
		jsonString = StringUtils.substringBetween(jsonString, "\"illusts\":", ",\"manga\":");
		JSONObject jsonObject = JSONObject.parseObject(jsonString);
		Map<String, Object> map = (Map<String, Object>) jsonObject;
		Set<String> pictures = map.keySet();
		String userPath = path + uid + "/";
		File dir = new File(userPath);
		if (dir.exists()) {
			System.out.println("已存在！");
			return;
		}
		for (String pictureId : pictures) {
			pid = Integer.parseInt(pictureId);
			downloadByPictureId(pid, userPath);
		}
	}

}
