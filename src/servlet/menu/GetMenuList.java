package servlet.menu;

import java.io.IOException;
import java.io.PrintWriter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class regist
 */
@WebServlet("/api/menu/getMenuList")
public class GetMenuList extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetMenuList() {
        super();
        // TODO Auto-generated constructor stub
       
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	doPost(request, response);
	}
    
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
    @Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	/* 设置响应头部 */
    	response.setCharacterEncoding("UTF-8");
		response.setContentType("text/json; charset=utf-8");
		PrintWriter out = response.getWriter();
			
		Connection conn = null;
		Statement stmt = null;
		try {
			/* 连接数据库 */
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://106.13.201.225:3306/coffee?useSSL=false&serverTimezone=GMT","coffee","TklRpGi1");
			stmt = conn.createStatement();
			
			/* 构建SQL语句  */
			String sql = "select * from meal;";
			PreparedStatement ps = conn.prepareStatement(sql);
			
			/* 执行SQL语句  */
			ResultSet rs = ps.executeQuery();
			
			/* 处理执行结果 */
			JSONObject responseJson = new JSONObject();
			JSONArray jsonarray = new JSONArray();
			while(rs.next()){
				JSONObject jsonobj = new JSONObject();
				jsonobj.put("userName",rs.getString("userName")==null?"":rs.getString("userName"));
				jsonobj.put("userId",rs.getString("userId")==null?"":rs.getString("userId"));
				jsonobj.put("telephone",rs.getString("telephone")==null?"":rs.getString("telephone"));
				jsonobj.put("email",rs.getString("email")==null?"":rs.getString("email"));
				jsonobj.put("password",rs.getString("password")==null?"":rs.getString("password"));
				jsonarray.add(jsonobj);
			}
			responseJson.put("success", true);
			responseJson.put("msg","");
			responseJson.put("data", jsonarray);
			out.println(responseJson);
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			/* 处理执行结果 */
			JSONObject responseJson = new JSONObject();
			responseJson.put("success",false);
			responseJson.put("msg", e.getMessage());
			out.println(responseJson);
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			e.fillInStackTrace();
		} finally {
			/* 无论如何关闭连接 */
			try {
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}	
	}

}