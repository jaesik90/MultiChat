/*
 * 1. 데이터베이스 계정 정보를 중복해서 기재하지
 *     않기 위함(db연동을 하는 각각의 클래스에서...)
 * 
 * 2. 인스턴스의 갯수를 한개만 둬보기!!
 * -  어플리케이션 가동 중 생성되는 Connection
 *    객체를 하나로 통일하기 위함...
 * 
 * */
package multi.client;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBManager {
	static private DBManager instance;
	private String driver="oracle.jdbc.OracleDriver";
	private String url="jdbc:oracle:thin:@localhost:1521:XE";
	private String user="batman";
	private String password="1234";
	private Connection con;
	
	private DBManager(){
		/*
		 * 1. 드라이버 로드
		 * 2. 접속
		 * 3. 쿼리문 수행
		 * 4. 반납, 해제
		 * */
		try {
			Class.forName(driver);
			con=DriverManager.getConnection(url, user, password);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	static public DBManager getInstance() {
		if(instance==null){
			instance = new DBManager();
		}
		return instance;
	}
	public Connection getConnection() {
		return con;
	}
	public void disConnect(Connection con){
		if(con!=null){
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
		
}
