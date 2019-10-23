package spider.pixiv;

import java.io.File;


public class SpiderMain {

	// 图片id
	private static String pictureId = "60095408";
	// 要存储的本地路径，不存在则创建
	private static String path = "pictures/";

	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		GetOriginPictureUrl getPicUrl = new GetOriginPictureUrl();
		try {
			Integer.parseInt(pictureId);
		} catch (NumberFormatException e) {
			System.out.println("请输入图片数字id！");
			return;
		}
		
		File dir = new File(path);
		if(!dir.exists()) {
			dir.mkdirs();
		}
		String url = getPicUrl.getPictureUrl(pictureId);
		if(url.equals("")) {
			System.out.println("未找到图片！");
			return;
		}
		String pictureType = getPicUrl.getPictureType(url);
		// 判断是否已有同名文件，减少下载次数
		File file = new File(path + pictureId + "_p0." + pictureType);
		if(file.exists()) {
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
		System.out.println("保存完成");
		long endTime = System.currentTimeMillis();
		System.out.println("程序运行时间：" + (endTime - startTime) + "ms");
	}

}
