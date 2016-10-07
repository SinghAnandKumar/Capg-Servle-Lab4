package com.seed;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.seed.beans.Product;
import com.seed.beans.ShoppingCart;

//TODO:0	Modification required
@WebServlet("DisplayCartDetailsServlet")
public class DisplayCartDetailsServlet extends HttpServlet {
	private Map<Integer, Product> productEntries;
	private PreparedStatement insertStatement;

	@Override
	public void init() throws ServletException {
		ServletContext application;

		// TODO:1 Get a reference to servletContext and store it in
		// "application" local variable
		application = getServletConfig().getServletContext();

		// TODO:2 The member variable "productEntries" should point to
		// the object in APPLICATION_SCOPE named as "entries.products"
		productEntries = (Map<Integer, Product>) application.getAttribute("entries.prodeucts");

		Connection dbConnectionRef = null;
		// TODO:3 The local variable "dbConnectionRef" should point to
		// the object in APPLICATION_SCOPE named as "database.connection"
		dbConnectionRef = (Connection) application.getAttribute("database.connection");

		try {
			String insertQuery = "insert into orders values(?,?,?)";
			insertStatement = dbConnectionRef.prepareStatement(insertQuery);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		ShoppingCart cartRef = null;

		Set<Integer> ids = null;
		ArrayList<Product> products = null;

		HttpSession session = request.getSession(false);

		if (session == null) {
			out.println("<h1>Session expired!</h1>");
			return;
		}

		// TODO:4 Retrieve attribute named as "shoppingCart" from session scope,
		// and assign it to "cartRef" local variable
		cartRef = (ShoppingCart) session.getAttribute("shoppingCart");

		// TODO:5 If the attribute(shoppingCart) exists, retrieve product ids
		// from it.
		if (cartRef != null) {
			ids = cartRef.getProductSet();
		}

		// TODO:6 Scan all productids retrieved from Cart and maintain a list of
		// corresponding products
		// Note: You can get details of product(value) based on productid(key)
		// using member variable "productEntries"
		if (ids != null) {
			products = new ArrayList<Product>();
			for (Integer id : ids) {
				products.add(productEntries.get(id));
			}
		}

		// TODO:7 display product details in tabular format as HTTP
		// response(text/html) to the web-client
		out.println("<html><body>");
		
		out.println("<table border=1>");
		
		out.println("<th>Product Id</th>");
		out.println("<th>Name</th>");
		out.println("<th>Price</th>");
		
		if(products!=null)
			for(Product p: products){
				out.println("<tr>");
				out.println("<td>"+p.getId()+"</td>");
				out.println("<td>"+p.getName()+"</td>");
				out.println("<td>"+p.getPrice()+"</td>");
				out.println("</tr>");
			}
		
		out.println("</table>");
		
		out.println("</body></html>");

		
		// TODO:8 insert set of product ids in a table named as "orders" with
		// client id(use session id)
		// refer /Resources/sqlqueries_sql.txt for table structure
		// use member variable "insertStatement" for insertion,only.
		
			try {
				int rows=0;
				for(Product p : products){
					insertStatement.setInt(1,p.getId());
					insertStatement.setString(2, request.getSession().getId());
					insertStatement.setInt(3, 1);
					
					rows+=insertStatement.executeUpdate();
					
					
				}
				
				System.out.println(rows+" rows inserted");
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				if(e instanceof SQLIntegrityConstraintViolationException)
					System.out.println("Integrity Violation");
				else
					e.printStackTrace();
			}
		

	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);
	}

}
