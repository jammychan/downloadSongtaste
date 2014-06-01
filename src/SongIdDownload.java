import java.io.File;
import java.util.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Set;

public class SongIdDownload
{	
	public static void downById(String id) throws IOException
	{
		Capture c = new Capture();
		String base = "http://huodong.duomi.com/songtaste/?songid=";
		String varmp3 = "var mp3url = \"";
		
		String mp3Url = base + id;
			
		//var mp3url = "http://mc.songtaste.com/201107120042/4a2d70251b92d3053f11ad9945309fd8/c/c2/c2eb5d84796df69a87fa80e01ee45975.mp3";

		String downloadContent = c.getHtmlSource(mp3Url);
			
		int start = downloadContent.indexOf(varmp3)+varmp3.length();
		int end = start+downloadContent.substring(start).indexOf('"');
		String url = downloadContent.substring(start, end);
					
		start = downloadContent.indexOf("<title>")+7;
		end = start+downloadContent.substring(start).indexOf("SongTaste");
		String name = downloadContent.substring(start, end);
		name = name.replaceAll("\n", "");
		name = name.replaceAll("\t", "");
		name = name.replaceAll("    ", "");
		name = name.replaceAll("- ", "");
			
		System.out.println(url+" "+name);
		System.out.println("downloading...");
		File f = null;
		f = new File(name+".mp3");
		if (!f.exists())
			Download.down(f, url);
		System.out.println("Ok");
	}
	
	public static void main(String[] args) throws IOException
	{
		System.out.println("input id:");
		
		int id = 0;
		Scanner input = new Scanner(System.in);
		id = input.nextInt();
	
		SongIdDownload.downById(""+id);

		/*
		for (int i=0; i<args.length; i++)
		{
			System.out.println(""+i+" ");
			SongIdDownload.downById(args[i]);
		}
		*/
	}
}
