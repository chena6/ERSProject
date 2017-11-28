package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import util.ConnectionUtil;

public class ReimbursementStatusDaoJdbc implements ReimbursementStatusDao {
	private Logger log = Logger.getRootLogger();
	private ConnectionUtil conUtil = ConnectionUtil.getconnectionUtil();

	@Override
	public int getStatus(String stat) throws SQLException {
		Connection con = conUtil.getConnection();
		PreparedStatement findStatus = null;
		int statusid = 0;
		String status = stat;

		String findStatusStatement = "SELECT reimbstatusid FROM reimbursement_status WHERE reimbstatus = ?";

		try {
			findStatus = con.prepareStatement(findStatusStatement);

			switch (status) {
			case "Pending":
				findStatus.setString(1, "Pending");
				break;
			case "Approved":
				findStatus.setString(1, "Approved");
				break;
			case "Denied":
				findStatus.setString(1, "Denied");
				break;
			default: throw new SQLException();
			}

			ResultSet rs = findStatus.executeQuery();
			statusid = rs.getInt("reimbid");

		} catch (SQLException e) {
			e.printStackTrace();
			log.debug("failed to get statusid");
		}

		return statusid;
	}

}
