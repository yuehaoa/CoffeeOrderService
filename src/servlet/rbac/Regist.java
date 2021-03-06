﻿package servlet.rbac;


import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;

import java.util.UUID;
/**
 * Servlet implementation class regist
 */
@WebServlet("/api/usermanage/regist")
public class Regist extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Regist() {
        super();
        // TODO Auto-generated constructor stub
       
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setCharacterEncoding("UTF-8");
    	response.setHeader("Allow", "POST");
    	response.sendError(405);
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/json; charset=utf-8");
		PrintWriter out = response.getWriter();
		Connection conn = null;
		try {
			ServletInputStream is = request.getInputStream();
			int nRead = 1;
			int nTotalRead = 0;
			byte[] bytes = new byte[10240];
			while (nRead > 0) {
				nRead = is.read(bytes, nTotalRead, bytes.length - nTotalRead);
				if (nRead > 0)
					nTotalRead = nTotalRead + nRead;
			}
			String str = new String(bytes, 0, nTotalRead, "utf-8");
			JSONObject jsonObj = JSONObject.fromObject(str);
			if(!jsonObj.has("telephone")) {
				jsonObj.put("telephone", "");
			}
			if(!jsonObj.has("email")) {
				jsonObj.put("email", "");
			}
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://106.13.201.225:3306/coffee?useSSL=false&serverTimezone=GMT","coffee","TklRpGi1");
			Statement stmt = conn.createStatement();
			String userId = UUID.randomUUID().toString();
			String password = jsonObj.getString("password");
			String telephone = jsonObj.getString("telephone");
			String email = jsonObj.getString("email");
			String userName = jsonObj.getString("userName");
			String sql = "insert into user(userId, telephone, email, password, userName) values(?,?,?,?,?)";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, userId);
			ps.setString(2, telephone);
			ps.setString(3, email);
			ps.setString(4, password);
			ps.setString(5, userName);
			try {
				int rowCount = ps.executeUpdate();
				JSONObject jsonobj = new JSONObject();
				if(rowCount>0){
					jsonobj.put("success",true);
					jsonobj.put("msg","注册成功");
				}
				out = response.getWriter();
				out.println(jsonobj);
				stmt.close();
				conn.close();
			}
			catch(Exception e) {
				JSONObject jsonobj = new JSONObject();
				jsonobj.put("success",false);
				jsonobj.put("msg",e.getMessage());
				out = response.getWriter();
				out.println(jsonobj);
				stmt.close();
				conn.close();
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

}
