import java.io.BufferedReader; 
import java.io.IOException; 
import java.io.InputStream; 
import java.io.InputStreamReader; 
import java.net.MalformedURLException; 
import java.net.URL; 
import java.net.URLConnection; 
import org.apache.commons.httpclient.methods.PostMethod; 

public class Capture { 
	
	public static boolean IS_BY_UTF_8 = true;
	
	
	public String getHtmlSource(String url, String charset) throws IOException
	{
		StringBuffer codeBuffer = null;
		BufferedReader in = null;
		try
		{
			URLConnection uc = new URL(url).openConnection();
			/**
			 * 为了限制客户端不通过网页直接读取网页内容, 就限制只能从浏览器提交请求.
			 * 但是我们可以通过修改http头的User-Agent来伪装, 这个代码就是这个作用
			 */
			uc.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows XP; DigExt)");

			InputStream is = uc.getInputStream();
			
 			if(charset.equals(""))
 				charset = "UTF-8";
			in = new BufferedReader(new InputStreamReader(is, charset));
			codeBuffer = new StringBuffer();
			String tempCode = "";
			// 把buffer内的值读取出来,保存到code中
			while ((tempCode = in.readLine()) != null)
			{
				codeBuffer.append(tempCode).append("\n");
//				System.out.println(tempCode);
			}
			in.close();
		}
		catch (MalformedURLException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		String temp = codeBuffer.toString();
		return temp;
	}
	
	
	/**
	 * url对应的html页面的内容
	 * @throws IOException 
	 * */
	public String getHtmlSource(String url) throws IOException
	{
		String charset = getCharSet(url);
//		System.out.println("the encode is " +charset);

		return getHtmlSource(url, charset);
	}
	
	/**
	 * 获取一个网页的编码形式
	 * by jammychan
	 * @param url
	 */
	public String getCharSet(String url) throws IOException
	{
		if (IS_BY_UTF_8)
			return "utf-8";

		URLConnection uc = new URL(url).openConnection();
		uc.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows XP; DigExt)");

		InputStream is = uc.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String str = new String();
		String temp = null;
		int i = 0;
		while ((temp = br.readLine()) != null)
		{
			str += temp;
			if (temp.length()>6 && temp.substring(0, 6).equals("<body>"))
				break;
			if (++i > 50)
				break;
		}
		br.close();
		
		//比较等于charset，然后找出charset的值
		for (i=0; i<str.length()-7; i++) 
		{
			if (str.substring(i, i+7).equalsIgnoreCase("charset"))
			{
				i = i+7;
				break;
			}
		}
		int begin = 0;
		while (true) 
		{
			if (i >= str.length())
				return new String();
			char c = str.charAt(i);
			if (c!=' ' && c!='=')
			{
				if (begin == 0)
					begin = i;
				if (c == '"')
					return str.substring(begin, i);
			}
			i++;
		}
	}

	
	/**
	 * Using the PostMethod
	 */
	public static String getCharsetWithMethod(String url)
	{
		PostMethod post = new PostMethod(url);
		post.setFollowRedirects(false);
		String charset = post.getResponseCharSet();
		System.out.println(charset);
		
		return charset;
	}
	
	public static String getCharsetWithURLConnect(String add) throws IOException
	{
		java.net.URL url = new java.net.URL(add);  
        java.net.URLConnection conn = url.openConnection();  
        conn.connect();  
        String s = conn.getContentEncoding();
        System.out.println("ContentType : " + conn.getContentType());  
        System.out.println("Encoding : " + s);//获取页面编码 ,不咋好用
       
        return conn.getContentEncoding();
	}
}
