import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;


public class Download
{
	public static void down(File f, String imgUrl) 
	{
            byte[] buffer = new byte[8 * 1024];
            URL u;
            URLConnection connection = null;
            try {
                    u = new URL(imgUrl);
                    connection = u.openConnection();
            } catch (Exception e) {
                    System.out.println("ERR:" + imgUrl);
                    return;
            }
            connection.setReadTimeout(100000);
            InputStream is = null;
            FileOutputStream fos = null;
            try {
                    f.createNewFile();
                    is = connection.getInputStream();
                    fos = new FileOutputStream(f);
                    int len = 0;
                    while ((len = is.read(buffer)) != -1) {
                            fos.write(buffer, 0, len);
                    }

            } catch (Exception e) {
                    f.delete();
            } finally {
                    if (fos != null) {
                            try {
                                    fos.close();
                            } catch (IOException e) {
                            }
                    }
                    if (is != null) {
                            try {
                                    is.close();
                            } catch (IOException e) {
                            }
                    }
            }
            buffer = null;
            // System.gc();
    }
	
	public static void main(String[] a) {
		System.out.print("fdsafds");
		File file = new File("g.html");
		down(file, "http://www.csdn.net/article/2012-10-29/2811228-Global-4G-LTE");
	}
}

