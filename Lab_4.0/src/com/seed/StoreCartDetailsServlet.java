package com.seed;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.seed.beans.ShoppingCart;

//TODO:0	Modification required
@WebServlet("storeDetails")
public class StoreCartDetailsServlet extends HttpServlet{
	
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		ShoppingCart cartRef = null;
		PrintWriter out = response.getWriter();
		
//		TODO:1	Obtain session created for client
//		if session is not created, ask container to create it.
		HttpSession session = request.getSession();
		
		if (session == null) {
			out.println("<h1>Session expired!</h1>");
			out.println("<h2><a href='categories.html'>See Categories, again</a></h2>");			
			return;
		}
		
//		TODO:2	Retrieve attribute named as "shoppingCart" in session scope
//		assign it to "cartRef" local variable (you need casting to ShoppingCart) 
		cartRef = (ShoppingCart) session.getAttribute("shoppingCart");
		
		
//		TODO:3 If "shoppingCart" does not exist, create object of com.seed.beans.ShoppingCart referenced by "cartRef"
//		and assign "shoppingCart" as attribute name for this newly created ShoppingCart
		if(cartRef == null){
			cartRef = new ShoppingCart();
		}

//		TODO:4	Read product ids from request parameter named as "bookID"
		String ids[] = request.getParameterValues("bookID");
		
//		TODO:5 add these product ids in the ShoppingCart object referenced by "cartRef" local variable
		cartRef.setProductList(ids);
		session.setAttribute("shoppingCart", cartRef);
		
//		TODO:6	if the user clicks "Shop More?" in "bookCatelogue.html", 
//		then redirect web-client to resource named as "categories.html"
		if(request.getParameter("continue")!=null){
			response.sendRedirect("categories.html");
		}
		
//		TODO:7	if the user clicks "Check Out" in "bookCatelogue.html", 
//		then instead of redirecting web-client to resource named as "displayDetails"
//		forward the current request to Servlet named as "DisplayCartDetailsServlet".
//		Please remember, in WEB.XML file there is no mapping for "DisplayCartDetailsServlet"
		else if(request.getParameter("checkout")!=null){
			RequestDispatcher rd = request.getRequestDispatcher("DisplayCartDetailsServlet"); 
			rd.forward(request, response);
		}
		
	}
	
}









