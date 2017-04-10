/*
 * 
 * 이 클래스는 데이터베이스 테이블 Chat이라는 테이블의
 * 레코드 1건을 담기 위한 클래스로서
 * 이러한 목적으로 객체를 가리켜 DTO,VO
 * 
 * 
 * <현실에 존재하는 사물에 대한 표현
 * 
 * 언어			DB	
 * class         테이블
 * instance     레코드
 * 속성 			칼럼
 * 
 * */
package multi.client;
public class Chat {// <--> 테이블
	private int chat_id;
	private String name;
	private String ip;
	
	public int getChat_id() {
		return chat_id;
	}
	public void setChat_id(int chat_id) {
		this.chat_id = chat_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
}
