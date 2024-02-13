package com.study.insert_and_select.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.study.insert_and_select.entity.Student;


@WebServlet("/data/addition")
public class DataInsertServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
   
    public DataInsertServlet() {
        super();
        
    }


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		StringBuilder builder = new StringBuilder();
		
		String readData = null;
		
		BufferedReader reader = request.getReader();
		
		while((readData = reader.readLine()) != null) {
			builder.append(readData);
		}
		
		System.out.println(builder.toString());

		// Json -> Map
		
		Gson gson = new Gson();
		Map<String, Object> map = gson.fromJson(builder.toString(), Map.class);
		
		System.out.println(map);
		System.out.println(map.get("name"));
		System.out.println(map.get("age"));
		
		// Json -> Entity 객체
		
		Student student = gson.fromJson(builder.toString(), Student.class);
		System.out.println(student);
		System.out.println(student.getName());
		System.out.println(student.getAge());
		
		Connection con = null;				//DB연결
		String sql = null;					//SQL 쿼리문 작성
		PreparedStatement pstmt = null; 	//SQL 쿼리문 입력
		int successCount = 0;				//SQL insert, update, delete 실행 결과(성공횟수)
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");	//데이터베이스 커넥터 드라이브 클래스 이름
			
			String url = "jdbc:mysql://mysql.cnodvtkrz1fq.ap-northeast-2.rds.amazonaws.com/db_study";
			String username = "aws";
			String password = "1q2w3e4r!!";
			
			con = DriverManager.getConnection(url, username, password);
			
			sql = "insert into student_tb(student_name, student_age) values(?, ?)";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, student.getName());
			pstmt.setInt(2, student.getAge());
			successCount = pstmt.executeUpdate();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(pstmt != null) {
				pstmt.close();
				}
				if(con != null) {
				con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		Map<String, Object> responseMap = new HashMap<>();
		responseMap.put("status", 201);
		responseMap.put("data", "응답데이터");
		responseMap.put("successCount", successCount);
		
		
		PrintWriter writer = response.getWriter();
		writer.println(gson.toJson(responseMap));
		
		
	}

}
