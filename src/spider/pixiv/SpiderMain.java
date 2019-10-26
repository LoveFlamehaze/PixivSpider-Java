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
		// Ҫ�洢�ı���·�����������򴴽�
		String path = "D:/images/pixiv/";
		SpiderMain spider = new SpiderMain();
		Scanner s = null;
		System.out.println("����1�����ͼƬid����ԭͼ������2����ݻ�ʦid����ȫ����Ʒԭͼ��");
		try {
			s = new Scanner(System.in);
			String option = s.next();
			long startTime = System.currentTimeMillis();
			if (option.equals("1")) {
				System.out.println("������ͼƬid��");
				int pid = s.nextInt();
				spider.downloadByPictureId(pid, path);
			} else if (option.equals("2")) {
				System.out.println("�����뻭ʦid��");
				int uid = s.nextInt();
				spider.downloadByUserId(uid, path);
			}
			System.out.println("�������");
			long endTime = System.currentTimeMillis();
			System.out.println("��������ʱ�䣺" + (endTime - startTime) + "ms");
		} catch (InputMismatchException e) {
			System.out.println("��������ȷ�����֣�");
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
			System.out.println("δ�ҵ�ͼƬ��");
			return;
		}
		String pictureType = getPicUrl.getPictureType(url);
		String fileName = path + pid + "_p0." + pictureType;
		// �ж��Ƿ�����ͬ���ļ����������ش���
		File file = new File(fileName);
		if (file.exists()) {
			System.out.println("�ļ��Ѵ��ڣ�");
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
			System.out.println("�Ѵ��ڣ�");
			return;
		}
		for (String pictureId : pictures) {
			pid = Integer.parseInt(pictureId);
			downloadByPictureId(pid, userPath);
		}
	}

}
