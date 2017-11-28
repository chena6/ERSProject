package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import util.ConnectionUtil;

public class ReimbursementTypeDaoJdbc implements ReimbursementTypeDao{
	private Logger log = Logger.getRootLogger();
	private ConnectionUtil conUtil = ConnectionUtil.getconnectionUtil();
	
	@Override
	public int getType(String t) throws SQLException {
		Connection con = conUtil.getConnection();
		PreparedStatement findType = null;
		int typeID = 0;
		String type = t;

		String findTypeStatement = "SELECT reimbtypeid FROM reimbursement_type WHERE reimbtype = ?";

		try {
			findType = con.prepareStatement(findTypeStatement);

			switch (type) {
			case "Lodging":
				findType.setString(1, "Lodging");
				break;
			case "Travel":
				findType.setString(1, "Travel");
				break;
			case "Food":
				findType.setString(1, "Food");
				break;
			case "Other":
				findType.setString(1, "Other");
				break;
			default: throw new SQLException();
			}

			ResultSet rs = findType.executeQuery();
			typeID = rs.getInt("reimbtype");

		} catch (SQLException e) {
			e.printStackTrace();
			log.debug("failed to get statusid");
		}

		return typeID;	
	}

}
