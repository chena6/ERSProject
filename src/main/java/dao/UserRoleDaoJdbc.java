package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import util.ConnectionUtil;

public class UserRoleDaoJdbc implements UserRoleDao {
	private Logger log = Logger.getRootLogger();
	private ConnectionUtil conUtil = ConnectionUtil.getconnectionUtil();
	@Override
	public int getRole(String r) throws SQLException {
		Connection con = conUtil.getConnection();
		PreparedStatement findStatus = null;
		int roleid = 0;
		String role = r;

		String findStatusStatement = "SELECT reimbstatusid FROM reimbursement_status WHERE reimbstatus = ?";

		try {
			findStatus = con.prepareStatement(findStatusStatement);

			switch (role) {
			case "Employee":
				findStatus.setString(1, "Employee");
				break;
			case "Admin":
				findStatus.setString(1, "Admin");
				break;
			default: throw new SQLException();
			}

			ResultSet rs = findStatus.executeQuery();
			roleid = rs.getInt("userroleid");

		} catch (SQLException e) {
			e.printStackTrace();
			log.debug("failed to get statusid");
		}

		return roleid;
	}
	
}
