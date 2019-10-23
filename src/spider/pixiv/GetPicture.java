package spider.pixiv;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;

public class GetPicture {

	public void downloadPicture(String url, File file) throws ClientProtocolException, IOException, InterruptedException {
		HttpClientUtil hcu = HttpClientUtil.getInstance();
		CloseableHttpResponse response = hcu.doGet(url);
		HttpEntity entity = response.getEntity();
		byte[] picture = EntityUtils.toByteArray(entity);
		savePicture(picture, file);
	}

	public void savePicture(byte[] picture, File file) {
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		try {
			fos = new FileOutputStream(file);
			bos = new BufferedOutputStream(fos);
			bos.write(picture);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bos != null)
				try {
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			if (fos != null)
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}

	}

}
