/**
 * 
 */
package service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class DbUtil {
        public static final String MY = "my";//自己用的库
        /**
         * @see 得到主库连接
         * @return
         */
        public static synchronized Connection getConnection(String dbName) {
        	if (dbName.equals( MY)){
        		try {
					Class.forName("com.mysql.jdbc.Driver").newInstance();
					String jdbc="jdbc:mysql://localhost:3306/daqbbs?user=root&password=root&Unicode=true&characterEncoding=UTF-8";
					//jdbc="jdbc:mysql://192.168.0.212:3306/ultrax?user=dzUser&password=dzUser1qaz2wsx&Unicode=true&characterEncoding=UTF-8";
					return  DriverManager.getConnection(jdbc);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
            return null;
        }
        public static Connection getDirectConnection(String host, int port, String db, String user, String password) {
                try {
                        Class.forName("com.mysql.jdbc.Driver").newInstance();
                        String url = "jdbc:mysql://" + host + ":" + port + "/" + db;
                         return DriverManager.getConnection(url, user, password);
                } catch (Exception e) {
                        e.printStackTrace();
                }
                return null;
        }

        public final static void close(Statement stmt) {
                if (stmt == null)
                        return;
                try {
                        stmt.close();
                        stmt=null;
                } catch (SQLException e) {
                        e.printStackTrace();
                }
        }

        public final static void close(ResultSet rs) {
                if (rs == null)
                        return;
                try {
                        rs.close();
                        rs=null;
                } catch (SQLException e) {
                        e.printStackTrace();
                }
        }

        public final static void close(Connection conn) {
                if (conn == null)
                        return;
                try {
                        conn.close();
                        conn=null;
                } catch (SQLException e) {
                        e.printStackTrace();
                }
                conn = null;
        }

        public final static void close(ResultSet rs, Statement stmt) {
                close(rs);
                close(stmt);
        }

        public final static void close(ResultSet rs, Statement stmt, Connection conn) {
                close(rs);
                close(stmt);
                close(conn);
        }
        public static void main(String[] args) {
			try {
				Connection conn= getConnection(MY);
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
}
