package spider.pixiv;

import java.io.File;


public class SpiderMain {

	// ͼƬid
	private static String pictureId = "60095408";
	// Ҫ�洢�ı���·�����������򴴽�
	private static String path = "pictures/";

	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		GetOriginPictureUrl getPicUrl = new GetOriginPictureUrl();
		try {
			Integer.parseInt(pictureId);
		} catch (NumberFormatException e) {
			System.out.println("������ͼƬ����id��");
			return;
		}
		
		File dir = new File(path);
		if(!dir.exists()) {
			dir.mkdirs();
		}
		String url = getPicUrl.getPictureUrl(pictureId);
		if(url.equals("")) {
			System.out.println("δ�ҵ�ͼƬ��");
			return;
		}
		String pictureType = getPicUrl.getPictureType(url);
		// �ж��Ƿ�����ͬ���ļ����������ش���
		File file = new File(path + pictureId + "_p0." + pictureType);
		if(file.exists()) {
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
		System.out.println("�������");
		long endTime = System.currentTimeMillis();
		System.out.println("��������ʱ�䣺" + (endTime - startTime) + "ms");
	}

}
