package multi.client;

import java.awt.BorderLayout;

import java.awt.Choice;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;




public class ClientMain extends JFrame implements ItemListener, ActionListener{
	JPanel p_north;
	Choice choice;
	JTextField t_port, t_input;
	JButton bt_connect;
	JTextArea area;
	JScrollPane scroll;
	int port=7777;
	DBManager manager;
	ArrayList<Chat> list=new ArrayList<Chat>();
	String ip;
	Socket socket;
	ClientThread ct;
	String nickName="jaewook";
	ClientMain main;
	
	public ClientMain() {
		p_north = new JPanel();
		choice = new Choice();
		t_port = new JTextField(Integer.toString(port),5);
		t_input = new JTextField();
		bt_connect = new JButton("접속");
		area = new JTextArea();
		scroll = new JScrollPane(area);
		manager = DBManager.getInstance();
		
		p_north.add(choice);
		p_north.add(t_port);
		p_north.add(bt_connect);
		
		add(p_north, BorderLayout.NORTH);
		add(scroll);
		add(t_input, BorderLayout.SOUTH);
		
		loadIP();
		
		for(int i=0;i<list.size();i++){
			choice.add(list.get(i).getName());
		}
		
		//리스너와 연결!!
		choice.addItemListener(this);
		bt_connect.addActionListener(this);
		
		t_input.addKeyListener(new KeyAdapter(){
			public void keyReleased(KeyEvent e) {
				int key=e.getKeyCode();
				
				if(key == KeyEvent.VK_ENTER){
					//메세지 전송 후 입력값 초기화
					String msg = t_input.getText();
					ct.send(msg);//서버에 전송
					t_input.setText("");
				}
			}
		});
		/*
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				obj = e.getSource();
				JOptionPane.showMessageDialog(main, "나가시겠습니까??");
				System.out.println(JOptionPane.OK_OPTION);
				
			}
		});
		*/
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setBounds(300, 100, 300, 400);
		setVisible(true);

	}
	
	//데이터베이스 가져오기!!
	public void loadIP(){
		Connection con=manager.getConnection();
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		String sql="select * from chat order by chat_id asc";
		try {
			pstmt=con.prepareStatement(sql);
			rs=pstmt.executeQuery();
			
			//rs의 모든 데이터를 dto 로 옮기는 과정..
			while(rs.next()){
				Chat dto = new Chat();
				dto.setChat_id(rs.getInt("chat_id"));
				dto.setName(rs.getString("name"));
				dto.setIp(rs.getString("ip"));
				
				list.add(dto);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(rs!=null){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(pstmt!=null){
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			manager.disConnect(con);
		}
	}
	
	public void itemStateChanged(ItemEvent e) {
		Choice ch=(Choice)e.getSource();
		int index=ch.getSelectedIndex();
		Chat chat=list.get(index);
		this.setTitle(chat.getIp());
		ip=chat.getIp(); //멤버변수에도 대입!!
	}
	
	//접속 메서드 정의
	public void connect(){
		try {
			port=Integer.parseInt(t_port.getText());
			socket = new Socket(ip, port);
			
			//대화를 담당할 클라이언트 측의 쓰레드
			//생성 및 가동

			ct = new ClientThread(socket,this);
			ct.start();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void actionPerformed(ActionEvent e) {
		connect();
	}
	
	public static void main(String[] args) {
		new ClientMain();
	}

}








