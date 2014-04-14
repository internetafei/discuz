package service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;

public class PostService {
	public Connection conn=DbUtil.getConnection(DbUtil.MY);
	private ResultSet rs;
	PreparedStatement ps;
	public Long insertPRE_FORUM_POST_TABLEID() throws Exception{
		Long res=0l;
		String sql="INSERT INTO pre_forum_post_tableid SET pid=null";
		PreparedStatement ps=conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
		ps.executeUpdate();
		rs = ps.getGeneratedKeys();
        if (rs.next()) {
                  res=  rs.getLong(1);
        }
		return res;
	}
	public Long insertPRE_FORUM_POST(Long pid,Integer fid,Long tid,String author,Long authorid,String message,Long dateline) throws Exception{
		Long res=0l;
		String sql="insert into pre_forum_post(pid,fid,tid ,first, author,authorid,dateline,message,useip, port,tags) values(?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement ps=conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
		int index=1;
		ps.setLong(index++,pid);
		ps.setInt(index++,fid);
		ps.setLong(index++, tid);
		ps.setInt(index++,0);
		ps.setString(index++, author);
		ps.setLong(index++, authorid);
		ps.setLong(index++, dateline);
		ps.setString(index++, message);
		ps.setString(index++, "127.0.0.1");
		ps.setInt(index++, 51561);
		ps.setInt(index++, 0);
		ps.execute();
		rs = ps.getGeneratedKeys();
        if (rs.next()) {
                  res=  rs.getLong(1);
        }
        return res;
	}
	public void updatePRE_FORUM_THREAD(Long tid,Long maxposition, String author,Long dateline) throws Exception{
		String sql="update pre_forum_thread set maxposition=?,lastposter=?,replies=replies+1,lastpost=? where tid=?";
		PreparedStatement ps=conn.prepareStatement(sql);
		int index=1;
		ps.setLong(index++,maxposition);
		ps.setString(index++, author);
		ps.setLong(index++, dateline);
		ps.setLong(index++, tid);
		ps.executeUpdate();
	}
	public Long save( Integer fid,Long tid,String author,Long authorid,String message,Long dateline) throws Exception{
		Long pid=insertPRE_FORUM_POST_TABLEID();
		Long maxposition= insertPRE_FORUM_POST(pid,  fid,  tid,  author,  authorid,   message,  dateline);
		updatePRE_FORUM_THREAD(tid, maxposition, author, dateline);
		conn.close();
		return pid;
	}
	public static void main(String[] args) throws Exception {
		PostService postService=new PostService();
		Integer fid=49;
		Long tid=19l;
		String author="admin";
 		Long authorid=1l;
		Long dateline=new Date().getTime()/1000;
		String message="测试回复45345";
		postService.save(  fid,  tid,  author,  authorid,   message,  dateline);
	}
}
