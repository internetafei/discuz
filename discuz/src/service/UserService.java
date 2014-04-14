package service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

import com.mysql.jdbc.Statement;

public class UserService {
	public Connection conn=DbUtil.getConnection(DbUtil.MY);
	private ResultSet rs;
	PreparedStatement ps;
	public static void main(String[] args) throws Exception{
		Long dateline=new Date().getTime()/1000;
		UserService service=new UserService();
		String user="a3";
		String psw="a3";
		String email="aaa@sf.com";
		service.save(user,psw,email,dateline);
	}
	private Long insert_PRE_UCENTER_MEMBERS(String user,String psw,String email,Long dateline) throws Exception{
		String salt=randstr(6);
		Long uid=0l;
		psw=toMD5(toMD5(psw)+salt );
		String sql="INSERT INTO  pre_ucenter_members SET secques='', username=?,password=?, email=?, regip='127.0.0.1', regdate=?,salt=?";
		ps=conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		int index=1;
		ps.setString(index++, user);
		ps.setString(index++, psw);
		ps.setString(index++, email);
		ps.setLong(index++, dateline);
		ps.setString(index++, salt);
		ps.executeUpdate();
		rs = ps.getGeneratedKeys();
        if (rs.next()) {
                  uid=  rs.getLong(1);
        }
        return uid;
	}
	private void insert_PRE_UCENTER_MEMBERFIELDS(Long uid) throws Exception{
		String sql="INSERT INTO  pre_ucenter_memberfields SET uid=?,blacklist=? ";
		ps=conn.prepareStatement(sql);
		ps.setLong(1, uid);
		ps.setString(2,"");
		ps.executeUpdate();
	}
	private void insert_PRE_COMMON_MEMBER (Long uid,String user,String psw,String email,Long dateline) throws Exception{
		String sql="REPLACE INTO pre_common_member SET uid=?, username=?,password=?,email=?, adminid=0 , groupid=10,regdate=?, emailstatus=0, credits=0, timeoffset=9999";
		ps=conn.prepareStatement(sql);
		int index=1;
		ps.setLong(index++, uid);
		ps.setString(index++, user);
		ps.setString(index++, psw);
		ps.setString(index++, email);
		ps.setLong(index++, dateline);
		ps.executeUpdate();
	}
	private void insert_PRE_COMMON_MEMBER_STATUS(Long uid,Long dateline) throws Exception{
		String sql="REPLACE INTO pre_common_member_status SET uid=?, regip='127.0.0.1', lastip='127.0.0.1', lastvisit=?, lastactivity=?, lastpost=0,lastsendmail=0";
		ps=conn.prepareStatement(sql);
		int index=1;
		ps.setLong(index++, uid);
		ps.setLong(index++, dateline);
		ps.setLong(index++, dateline);
		ps.executeUpdate();
	}
	
	private void insert_PRE_COMMON_MEMBER_COUNT(Long uid,Long dateline) throws Exception{
		String sql="REPLACE INTO pre_common_member_status SET uid=?, regip='127.0.0.1' , `lastip`='127.0.0.1' ,lastvisit=?,lastactivity=?,lastpost=0,lastsendmail=0";
		ps=conn.prepareStatement(sql);
		int index=1;
		ps.setLong(index++, uid);
		ps.setLong(index++, dateline);
		ps.setLong(index++, dateline);
		ps.executeUpdate();
	}
	private void insert_PRE_COMMON_MEMBER_PROFILE(Long uid) throws Exception{
		String sql="REPLACE INTO pre_common_member_profile SET uid=?,bio='',interest='',field1='',field2='',field3='',field4='',field5='',field6='',field7='',field8=''";
		ps=conn.prepareStatement(sql);
		ps.setLong(1, uid);
		ps.executeUpdate();
	}
	private void insert_PRE_COMMON_MEMBER_FIELD_FORUM(Long uid) throws Exception{
		String sql="REPLACE INTO pre_common_member_field_forum SET uid=?,medals='',sightml='',groupterms='',groups=''";
		ps=conn.prepareStatement(sql);
		ps.setLong(1, uid);
		ps.executeUpdate();
	}

	private void insert_PRE_COMMON_MEMBER_FIELD_HOME(Long uid) throws Exception{
		String sql="REPLACE INTO pre_common_member_field_home SET uid=?,spacecss='',blockposition='',recentnote='',spacenote='',privacy='',feedfriend='',acceptemail='',magicgift='',stickblogs=''";
		ps=conn.prepareStatement(sql);
		ps.setLong(1, uid);
		ps.executeUpdate();
	}
	public Long save(String user,String psw,String email,Long dateline) throws Exception{
		try {
			conn.setAutoCommit(false);
			Long uid= insert_PRE_UCENTER_MEMBERS(user, psw, email, dateline);
			insert_PRE_UCENTER_MEMBERFIELDS(uid);
			insert_PRE_COMMON_MEMBER(uid,user, psw, email, dateline);
			insert_PRE_COMMON_MEMBER_STATUS(uid,dateline);
			insert_PRE_COMMON_MEMBER_COUNT(uid, dateline);
			insert_PRE_COMMON_MEMBER_PROFILE(uid);
			insert_PRE_COMMON_MEMBER_FIELD_FORUM(uid);
			insert_PRE_COMMON_MEMBER_FIELD_HOME(uid);
			conn.commit();
			return uid;
		} catch (Exception e) {
			e.printStackTrace();
			conn.rollback();
		}finally{
			conn.close();
		}
		return null;
	}
	public static String toMD5(String str){  
	    try {  
	        MessageDigest md = MessageDigest.getInstance("MD5");  
	        md.update(str.getBytes());  
	        byte[]byteDigest = md.digest();  
	        int i;  
	        StringBuffer buf = new StringBuffer("");  
	        for (int offset = 0; offset < byteDigest.length; offset++) {  
	            i = byteDigest[offset];  
	            if (i < 0)  
	                i += 256;  
	            if (i < 16)  
	                buf.append("0");  
	            buf.append(Integer.toHexString(i));  
	        }  
	        //32位加密  
	        return buf.toString();  
	        // 16位的加密  
	        //return buf.toString().substring(8, 24);   
	    } catch (NoSuchAlgorithmException e) {  
	        e.printStackTrace();  
	        return null;  
	    }  
	      
	}  
	public static String randstr(int len ){
		char res[]=new char[len];
		String str="abcdefghijklmnopqrstuvwxyz0123456789";
		int l=str.length();
		Long index=0l;
		for (int i = 0; i <  len; i++) {
			res[i]=str.charAt(new Double(  Math.floor(  Math.random()*l)).intValue());
		}
		return new String(res);
	}
	
}
