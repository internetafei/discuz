package service;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

public class UserList {
	public static void main(String[] args) {
		try {
			List ls=	readFile("/home/lpf/h/discuz/src/mmb/forum/service/u.txt");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static List readFile(String path)  {
		List ls=new ArrayList();
		try {
			ObjectInputStream in=new ObjectInputStream(new FileInputStream(path));
			Object o;
			while (  (o=in.readObject())!=null) {
				ls.add(o);
			}
			in.close();
		} catch ( Exception e) {
			e.printStackTrace();
		}
		return ls;
	}
}
