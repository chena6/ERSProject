package front.controller;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.servlets.DefaultServlet;

import controllers.UserController;

public class FrontController extends DefaultServlet {
	private static final long serialVersionUID = 1L;
	private UserController uc = new UserController();

	public FrontController() {
		super();
	}

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setHeader("Access-Control-Allow-Origin", "http://localhost:4200");
		response.setHeader("Access-Control-Allow-Methods", "GET, PUT, POST, OPTIONS, DELETE");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		super.service(request, response);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String URL = request.getRequestURI().substring(request.getContextPath().length());

		if (URL.equals("/static")) {
			if (request.getSession().getAttribute("user") != null) {
				response.sendRedirect(request.getContextPath() + "/static/home.html");
			} else {
				super.doGet(request, response);
			}
			return;
		}

		if (URL.startsWith("/login")) {
			uc.delegateGet(request, response);
		}

		if ("/home".equals(URL)) {
			if (request.getSession().getAttribute("user") != null) {
				response.sendRedirect(request.getContextPath() + "/static/home.html");
			} else {
				request.getRequestDispatcher("/static").forward(request, response);
			}
			// redirecting
			// response.sendRedirect(request.getContextPath() + "/login");
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String URL = request.getRequestURI().substring(request.getContextPath().length());

		if (URL.startsWith("/reimbursements")) {

		}
		if (URL.startsWith("/login")) {
			try {
				uc.processPost(request, response);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
