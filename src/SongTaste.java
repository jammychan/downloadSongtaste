import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class SongsUrlDownload
{
	public static void main(String[] args) throws IOException
	{
		System.out.println("input url，enter to skip:");

		// 输入下载的url
		String url = null;
		Scanner input = new Scanner(System.in);
		url = input.nextLine();
		if (null == url || url.length() == 0)
			return;
		
		// 获取url的内容
		Capture c = new Capture();
		String htmlContent = c.getHtmlSource(url, "gb2312");
//		System.out.println(s);
		
		// 根据url特征判断开始key。
		// http://www.songtaste.com/singer/282751/ 					每一行，WL(
		// http://www.songtaste.com/singer.php?name=leaving...... 	每一行 WL(
		// http://www.songtaste.com/user/album/a414634 				只有一行，WS(
		// http://www.songtaste.com/user.php?uid=727193 			每一行 WS(
		// http://www.songtaste.com/user/8502497/ 					每一行 WS(
		// WL("1","2261263","★��一开头就听到鼻子酸酸的 每次＠听这歌听到哭 这女声太伤感了× ","120", "352482");
		// WS("1","2261263","★��一开头就听到鼻子酸酸的 每次＠听这歌听到哭 这女声太伤感了× ","120", "352482");
		String startKey = "WL(";
		if (url.startsWith("http://www.songtaste.com/user/album"))
			startKey = "<script>WS(";
		else if (url.startsWith("http://www.songtaste.com/user"))
			startKey = "WS(";
		
		//获取页面中所有歌曲的id和名字，主要是id
		String name, id;
		String[] idAndNames = null;
		ArrayList<String> idList = new ArrayList<String>();
		ArrayList<String> nameList = new ArrayList<String>();
		String[] lines = htmlContent.split("\n");
		int num = 0;
		for (int i=0; i<lines.length; i++)
		{
			if (lines[i].contains(startKey))
			{
				// 每一行都包含一首歌的id
				String split = startKey.substring(startKey.length()-3, startKey.length()-1);
				idAndNames = lines[i].split(split);
//				System.out.println(lines[i]);
				for (String idName : idAndNames)
				{
					if (!idName.startsWith("("))
						continue;
					
					String[] songInfo = idName.split("\"");
					if (songInfo.length > 5)
					{
						id = songInfo[3];
						name = songInfo[5];
						try{
							Integer.valueOf(id);
							idList.add(id);
							nameList.add(name);
							System.out.println(""+num+++" \t"+id+" \t"+name);
						}catch(Exception e){
							// do nothing
						}
					}
				}
			}
		}
		
		System.out.println("there are " + idList.size() + " songs to download.");
		for (int i=0; i<idList.size(); ++i)
		{
			System.out.println(""+i+" \t" + idList.get(i) + " \t" + nameList.get(i));
			SongIdDownload.downById(idList.get(i));
		}
	}
}
