package service;
import java.io.UnsupportedEncodingException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
public class DiscuzHtml {
	public static void main(String[] args) throws UnsupportedEncodingException {
	}
	public static String delchar(String str){
		StringBuffer sb=new StringBuffer();
		for (int i = 0; i < str.length(); i++) {
			char c=str.charAt(i);
			int n=(int)c;
			if (n==55357 || n==56851) {
			}else{
				sb.append(c);
			}
		}
		return sb.toString();
	}
	public static String discuzHtml(String str) {
		Document doc = Jsoup.parse(str,"utf-8");
		return  delchar(  discuz( doc.getElementsByTag("body").get(0))) ;
	}
	public static String discuz(Element e1 ){
		StringBuffer sb=new StringBuffer();
		Elements es = e1.children();
		if (es.size()>0) {
			for (Element e : es){
				sb.append(discuz(e)+"\r\n");
	  		}
		}else{
			if (e1.tagName().equals("p") || e1.tagName().equals("div")){
				return e1.text();
			}else if (e1.tagName().equals("a")){
				Element parent = e1.parent() ;
				String a="[url="+e1.attr("href")+"]"+e1.text()+"[/url]";
				e1.remove();
				return  parent.text()+a;
			}else if (e1.tagName().equals("img")){
				Element parent = e1.parent() ;
				e1.remove();
				return parent.text()+  "[img]"+e1.attr("src")+"[/img]";
			}else{
				return   e1.text();
			}
		}
		return new String(sb);
	}
	public static String discuzNoBrHtml(String str) {
		Document doc = Jsoup.parse(str,"utf-8");
		return delchar(  discuzNoBr( doc.getElementsByTag("body").get(0)));
	}
	public static String discuzNoBr(Element e1 ){
		StringBuffer sb=new StringBuffer();
		Elements es = e1.children();
		if (es.size()>0) {
			for (Element e : es){
				sb.append(discuzNoBr(e) );
	  		}
		}else{
			if (e1.tagName().equals("p") || e1.tagName().equals("div")){
				return e1.text();
			}else if (e1.tagName().equals("a")){
				Element parent = e1.parent() ;
				String a="[url="+e1.attr("href")+"]"+e1.text()+"[/url]";
				e1.remove();
				return  parent.text()+a;
			}else if (e1.tagName().equals("img")){
				Element parent = e1.parent() ;
				e1.remove();
				return parent.text()+  "[img]"+e1.attr("src")+"[/img]";
			}else{
				return   e1.text();
			}
		}
		return  new String(sb);
	}
	
	public static String toTrueAsciiStr(String str){
		StringBuffer sb = new StringBuffer();
		byte[] bt = str.getBytes();
		for(int i =0 ;i<bt.length;i++){
		if(bt[i]<0){
		//是汉字去高位1
		sb.append((char)(bt[i] & 0x7f));
		}else{//是英文字符 补0作记录
		sb.append((char)0);
		sb.append((char)bt[i]);
		}
		}
		return sb.toString();
		}


		public static String unToTrueAsciiStr(String str){
		byte[] bt = str.getBytes();
		int i,l=0,length = bt.length,j=0;
		for(i = 0;i<length;i++){
		if(bt[i] == 0){
		l++;
		}
		}
		byte []bt2 = new byte[length-l];
		for(i =0 ;i<length;i++){
		if(bt[i] == 0){
		i++;
		bt2[j] = bt[i];
		}
		else{
		bt2[j] = (byte)(bt[i]|0x80);
		}
		j++;
		}
		String tt = new String(bt2);
		return tt;
		}
	
}
