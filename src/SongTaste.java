import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class SongTaste
{
	public static void main(String[] args) throws IOException
	{
		Capture c = new Capture();
		String s = c.getHtmlSource("http://www.songtaste.com/singer/282751/");
//		System.out.println(s);
		
		//获取页面中所有歌曲的id和名字
		HashMap<String, String> idToName = new HashMap<String, String>();
		String name, id;
		String[] ss = s.split("\n");
		
//		WL("1","2261263","★��一开头就听到鼻子酸酸的 每次＠听这歌听到哭 这女声太伤感了× ","120", "352482");
		for (int i=0; i<ss.length; i++)
		{
			String key = "WL(";
			if (ss[i].startsWith(key))
			{
				String[] songInfo = ss[i].split("\"");
				id = songInfo[3];
				name = songInfo[5];
				idToName.put(id, name);
//				System.out.println(id+" "+name);
			}
		}
		
		//下载所有歌曲,先再次抓取页面,获取mp3的url
		//http://huodong.duomi.com/songtaste/?songid=2095353
		String base = "http://huodong.duomi.com/songtaste/?songid=";
		String mp3Url = "";
		Set<String> keys = idToName.keySet();
		String varmp3 = "var mp3url = \"";
		int cou=0;
		for (String key : keys)
		{
			mp3Url = base + key;
//			System.out.println(mp3Url);
			
			//var mp3url = "http://mc.songtaste.com/201107120042/4a2d70251b92d3053f11ad9945309fd8/c/c2/c2eb5d84796df69a87fa80e01ee45975.mp3";
			String downloadContent = c.getHtmlSource(mp3Url);
			int start = downloadContent.indexOf(varmp3)+varmp3.length();
			String url = downloadContent.substring(start,
					start+downloadContent.substring(start).indexOf('"'));
			
			File f = null;
			f = new File(idToName.get(key)+".mp3");
//			f.exists();
			Download.down(f, url);
			System.out.println(""+cou+++" "+url);
		}
	}
	
	
}
