package controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.StdDateFormat;

import beans.Reimbursement;
import beans.User;
import services.ReimbursementService;

public class ReimbursementController {
	private Logger log = Logger.getRootLogger();
	private ReimbursementService rs = new ReimbursementService();

	public void delegateGet(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
		log.debug("get request delegated to reimbursements controller");
		String reimburseURL = request.getRequestURI()
				.substring(request.getContextPath().length() + "/reimbursements".length());

		if ("/all".equals(reimburseURL)) {

			try {
				List<Reimbursement> allReimbs = rs.findAll();
				ObjectMapper om = new ObjectMapper();
				om.findAndRegisterModules();
		        om.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		        om.setDateFormat(new StdDateFormat());
				ObjectWriter ow = om.writer().withDefaultPrettyPrinter();
				String json = ow.writeValueAsString(allReimbs);
				PrintWriter writer = response.getWriter();
				writer.write(json);
				
				log.debug("wrote reimbursements to body of the response");
				return;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		if ("/user".equals(reimburseURL)) {
			User u = (User) request.getSession().getAttribute("user");
			try {
				List<Reimbursement> myReimb = rs.getReimb(u.getUserID());
				ObjectMapper om = new ObjectMapper();
				om.findAndRegisterModules();
		        om.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		        om.setDateFormat(new StdDateFormat());
				ObjectWriter ow = om.writer().withDefaultPrettyPrinter();
				String json = ow.writeValueAsString(myReimb);
				PrintWriter writer = response.getWriter();
				writer.write(json);

				log.debug("wrote user reimbursements to body of the response");
				return;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	public void delegatePost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, SQLException {
		log.debug("post request delegated to reimbursement  controller");
		String reimburseURL = request.getRequestURI()
				.substring(request.getContextPath().length() + "/reimbursements".length());

		if ("/addReimburse".equals(reimburseURL)) {
			try {
				String json = request.getReader().lines().reduce((acc, cur) -> acc + cur).get();
				log.trace("jason received = " + json);
				ObjectMapper om = new ObjectMapper();
				Reimbursement r = om.readValue(json, Reimbursement.class);
				User u = (User) request.getSession().getAttribute("user");
				log.trace("object created from json = " + r);
				log.trace("user u created from session = " + u);

				rs.save(r, u);
			} catch (JsonParseException e) {

				e.printStackTrace();
			} catch (JsonMappingException e) {

				e.printStackTrace();
			} catch (IOException e) {

				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if ("/approve".equals(reimburseURL)) {
			log.debug("request approve reimbursements received");
            
            String json = request.getReader().lines().reduce((acc, cur) -> acc + cur).get();
            log.trace("json received: " + json);
            
            ObjectMapper om = new ObjectMapper();
            List<Reimbursement> rl = om.readValue(json, new TypeReference<List<Reimbursement>>(){});
            log.trace(rl);
            rs.manageReimb(rl);
            
            
            
		}

	}

}
