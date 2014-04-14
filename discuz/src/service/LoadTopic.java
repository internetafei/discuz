package service;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import bean.Reply;
import bean.User;

public class LoadTopic {
	static List ls= UserList.readFile("/home/lpf/h/discuz/src/mmb/forum/service/u.txt");
	static String board[][]=Board.getBoardInfo();
	public static void main(String[] args) {
		Connection conn = DbUtil.getDirectConnection("localhost", 3306, "bbs",
				"root", "root");
		DateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String message="";
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("select title,`desc`,add_date,user_id,board_id ,id from bbs.bbs_topic where state in (0,1)  and board_id is not null");
			ResultSet rs2 ;
			String m="[quote][size=2][url=forum.php?mod=redirect&goto=findpost&pid=%d&ptid=%d][color=#999999]%s发表于%s[/color][/url][/size]%s[/quote]\r\n%s";
			while (rs.next()){
				String title=rs.getString(1);
				title=DiscuzHtml.discuzHtml(title);
				if (title.length()>80) {
					title=title.substring(0,80);
				}
				message=rs.getString(2);
				if (message!=null && !message.equals("")) {
					message=DiscuzHtml.discuzHtml(message);	
				}
				Date d=new Date( rs.getTimestamp(3).getTime()) ;
				Long dateline=d.getTime()/1000;
				Long userId=rs.getLong(4);
				String boardId=rs.getString(5);
				if (boardId==null) {
					continue;
				}
				Integer topicId=rs.getInt(6);
				System.out.println("topicId:"+topicId);
				if (getboardId (  boardId )==null) {
					 System.out.println("找不到boardId:"+boardId   );
					 System.out.println("找不到标题:"+title);
					 System.out.println("找不到topicId:"+topicId);
					 continue;
				}
				NewThread newThread=new NewThread();
				User u=getUserId(userId);
				Long tid=newThread.save(getboardId (  boardId ), u.getUserName(), u.getNewId(), title, message, dateline);
				List<Reply> replys=getReply(topicId);
				for (Reply r:replys) {
					if (r.getContent()==null || r.getContent().equals("")) {
						continue;
					}
					userId=r.getUserId();
					u=getUserId(userId);
  					dateline=r.getDateline();
  					if (r.getParentId()==null || r.getParentId()==0) {
  						message=  DiscuzHtml.discuzNoBrHtml( r.getContent());
					}else{
						Reply r2=null;
						for (Reply r3:replys) {
							if (r.getParentId().equals(r3.getId())) {
								r2=r3;
								break;
							}
						}
						if (r2!=null){
							u=getUserId(r2.getUserId());
							message=String.format(m, r2.getNewId(),tid,u.getUserName(),df.format(r2.getDate()),DiscuzHtml.discuzNoBrHtml(r2.getContent()), DiscuzHtml.discuzNoBrHtml( r.getContent()) );
						}
					}
  					u=getUserId(userId);
					PostService postService=new PostService();
					Long pid=postService.save(getboardId (  boardId ), tid, u.getUserName(), u.getNewId(), message, dateline);
					r.setNewId(pid);
				}
			}
			rs.close();
			stmt.close();
			conn.close();
		} catch (Exception e) {
			System.out.println(message);
			e.printStackTrace();
		}

	}
	public static Integer  getboardId(String oldId){
		for (int i = 0; i < board.length; i++) {
			if (oldId.equals(board[i][0])) {
				return Integer.valueOf(  board[i][2]);
			}
		}
		return null;
	}
	public static User  getUserId(Long oldId){
		for (Object o:ls) {
			 User u=(User)o;
			 if (oldId.equals(u.getOldId())) {
				return u;
			 }
		}
		return null;
	}
	public static List<Reply>  getReply(Integer topicId){
		
 		List<Reply> res=new ArrayList<Reply>(); 
		try {
			Connection conn = DbUtil.getDirectConnection("localhost", 3306, "bbs","root", "root");
			Statement stmt = conn.createStatement();
			ResultSet rs= stmt.executeQuery("select id, content,user_id,parent_id,reply_date from bbs.bbs_topic_reply where topic_id= "+topicId +" order by id ");
			Reply reply;
			while (rs.next()){
				reply=new Reply();
				reply.setId(rs.getInt(1));
				reply.setContent(rs.getString(2));
				reply.setUserId(rs.getLong(3));
				reply.setParentId(rs.getInt(4));
 				reply.setDate(new Date( rs.getTimestamp(5).getTime()) );
				reply.setDateline(rs.getTimestamp(5).getTime()/1000);
				res.add(reply);
	  		}
			rs.close();
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}
}
