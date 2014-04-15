package service;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import bean.User;
public class LoadUser{
	public static void main(String[] args) {
		try {
			   Long dateline=new Date().getTime()/1000;
			   Connection conn = DbUtil.getDirectConnection("localhost",3306, "bbs", "root", "root");
			   Statement stmt=conn.createStatement();
			   ResultSet rs = stmt.executeQuery("select id,name from  bbs_user where id>0");
			   ObjectOutputStream os=new ObjectOutputStream(new FileOutputStream( "/home/lpf/h/discuz/src/mmb/forum/service/u.txt"));
			   User user;
			   Long oldId ,newId;
			   String name;
			   UserService service;
			   while (rs.next()){
				   oldId=rs.getLong(1);
				   System.out.println(oldId);
				   name=rs.getString(2);
				   service=new UserService();
				   newId=service.save(name, "3453453","",dateline);
				   user=new User(oldId,name,newId);
				   os.writeObject(user);
			   }
			   os.writeObject(null);
			   os.close();
			   conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
