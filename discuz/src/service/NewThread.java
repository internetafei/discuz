package service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;

public class NewThread{
	public Connection conn=DbUtil.getConnection(DbUtil.MY);
	private ResultSet rs;
	public Long insertPRE_FORUM_THREAD(Integer fid,String author,Long authorid,String subject,Long dateline) throws Exception{
		String sql="insert into pre_forum_thread (fid ,author,authorid,subject,dateline,lastpost,lastposter,status) values(?,?,?,?,?,?,?,32)";
		PreparedStatement ps=conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		ps.setInt(1, fid);
		ps.setString(2, author);
		ps.setLong(3, authorid);
		ps.setString(4, subject);
		ps.setLong(5, dateline);
		ps.setLong(6, dateline);
		ps.setString(7, author);
		ps.executeUpdate();
		rs = ps.getGeneratedKeys();
        if (rs.next()) {
                return  rs.getLong(1);
        }
        return null;
	}
	public void insertPRE_FORUM_SOFA(Long tid,Integer fid ) throws Exception{
		String sql="insert into pre_forum_sofa (tid,fid  ) values(?,?)";
		PreparedStatement ps=conn.prepareStatement(sql);
		ps.setLong(1, tid);
		ps.setInt(2, fid);
		ps.execute();
	}
	public void insertPRE_FORUM_NEWTHREAD(Long tid,Integer fid ,Long dateline) throws Exception{
		String sql="insert into pre_forum_newthread (tid,fid ,dateline ) values(?,?,?)";
		PreparedStatement ps=conn.prepareStatement(sql);
		ps.setLong(1, tid);
		ps.setInt(2, fid);
		ps.setLong(3, dateline);
		ps.execute();
	}
	public void insertPRE_FORUM_POST(Long pid,Integer fid,Long tid,String author,Long authorid,String subject,String message,Long dateline) throws Exception{
		String sql="insert into pre_forum_post(pid,fid,tid ,first, author,authorid,subject,dateline,message,useip, port) values(?,?,?,?,?,?,?,?,?,?,? )";
		PreparedStatement ps=conn.prepareStatement(sql);
		int index=1;
		ps.setLong(index++,pid);
		ps.setInt(index++,fid);
		ps.setLong(index++, tid);
		ps.setInt(index++,1);
		ps.setString(index++, author);
		ps.setLong(index++, authorid);
		ps.setString(index++, subject);
		ps.setLong(index++, dateline);
		ps.setString(index++, message);
		ps.setString(index++, "127.0.0.1");
		ps.setInt(index++, 51561);
		ps.execute();
	}
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
	public void updatePRE_FORUM_FORUM(Integer fid) throws Exception{
		String sql="update pre_forum_forum set threads =(select count(1) from pre_forum_thread where fid=?),posts=(select count(1) from pre_forum_post where fid=?) where fid=?";
		PreparedStatement ps=conn.prepareStatement(sql);
		ps.setInt(1,fid);
		ps.setInt(2,fid);
		ps.setInt(3,fid);
		ps.execute();
		
	}
	public Long save(Integer fid,String author,Long authorid,String subject,String message,Long dateline) throws Exception{
		try {
			this.conn.setAutoCommit(false);
			Long pid=insertPRE_FORUM_POST_TABLEID();
			Long tid= insertPRE_FORUM_THREAD(fid,author, authorid, subject, dateline);
			insertPRE_FORUM_NEWTHREAD(tid, fid, dateline);
			insertPRE_FORUM_SOFA(tid, fid);
			insertPRE_FORUM_POST(pid,fid, tid, author, authorid, subject, message, dateline);
			updatePRE_FORUM_FORUM(fid);
			conn.commit();
			return tid;
		} catch (Exception e) {
			e.printStackTrace();
			conn.rollback();
		}finally{
			close();
		}
		return null;
	}
	public void close(){
		try {
			conn.close();
		} catch (Exception e) {
		}
	}
	public static void main(String[] args) throws Exception {
		NewThread newThread=new NewThread();
		int fid=49;
		Long pid=newThread.insertPRE_FORUM_POST_TABLEID();
		Long dateline=new Date().getTime()/1000;
		try {
			newThread.conn.setAutoCommit(false);
			Long tid=newThread.insertPRE_FORUM_THREAD(fid, "admin", 1l, "测试", dateline);
			newThread.insertPRE_FORUM_NEWTHREAD(tid, fid, dateline);
			newThread.insertPRE_FORUM_SOFA(tid, fid);
			newThread.insertPRE_FORUM_POST(pid,49, tid, "admin", 1l, "测试", "测试", dateline);
			newThread.conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
			newThread.conn.rollback();
		}finally{
			newThread.close();
		}
	}
}
