package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import beans.Reimbursement;
import util.ConnectionUtil;

public class ReimbursementDaoJdbc implements ReimbursementDao {
	private Logger log = Logger.getRootLogger();
	private ConnectionUtil conUtil = ConnectionUtil.getconnectionUtil();

	@Override
	public void saveReimbursement(Reimbursement r) throws SQLException {
		log.debug("attempt to insert user to database");
		Connection con = conUtil.getConnection();
		PreparedStatement insertReimbursement = null;

		String insertStatement = "INSERT INTO Reimbursements"
				+ " (reimbID, amount, submitted, resolved, receipt, reimbAuthor, reimbResolver, statusID, typeID)"
				+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try {
			con.setAutoCommit(false);
			Timestamp submittedTS = Timestamp.valueOf(r.getSubmitted());
			Timestamp resolvedTS = Timestamp.valueOf(r.getResolved());
			insertReimbursement = con.prepareStatement(insertStatement);
			insertReimbursement.setInt(1, r.getReimbID());
			insertReimbursement.setDouble(2, r.getAmount());
			insertReimbursement.setTimestamp(3, submittedTS);
			insertReimbursement.setTimestamp(4, resolvedTS);
			insertReimbursement.setBlob(5, r.getReceipt());
			insertReimbursement.setInt(6, r.getReimbAuthor());
			insertReimbursement.setInt(7, r.getReimbResolver());
			insertReimbursement.setInt(8, r.getStatusID());
			insertReimbursement.setInt(9, r.getTypeID());

			insertReimbursement.executeUpdate();

			ResultSet keys = insertReimbursement.getGeneratedKeys();
			if (keys.next()) {
				log.info("successfully added user");
			}

			con.commit();

		} catch (SQLException e) {
			e.printStackTrace();
			if (con != null) {
				try {
					log.debug("failed to save user, roll back transaction");
					con.rollback();
				} catch (SQLException ex) {
					e.printStackTrace();
				}
			}

		} finally {
			if (insertReimbursement != null) {
				insertReimbursement.close();
			}
			con.setAutoCommit(true);
		}
	}

	@Override
	public void manageReimbursement(int rid, String ad) throws SQLException {
		Connection con = conUtil.getConnection();
		PreparedStatement manage = null;
		String setState = ad;

		String manageStatement = "UPDATE reimbursements SET reimbstatus = ?";
		try {
			manage = con.prepareStatement(manageStatement);
			manage.setString(1, setState);
			manage.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			log.debug("could not approve/disapprove reimbursement");
		}
	}

	public List<Reimbursement> findAll() throws SQLException {
		Connection con = conUtil.getConnection();
		List<Reimbursement> reimbursements = new ArrayList<Reimbursement>();
		PreparedStatement getAll = null;

		String getAllStatement = "SELECT * FROM reimbursements";

		try {
			getAll = con.prepareStatement(getAllStatement);
			ResultSet rs = getAll.executeQuery();

			log.debug("query executed");

			while (rs.next()) {
				Reimbursement r = extractReimbursement(rs);
				reimbursements.add(r);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return reimbursements;
	}

	private Reimbursement extractReimbursement(ResultSet rs) throws SQLException {
		Reimbursement r = new Reimbursement();
		r.setReimbID(rs.getInt("reimbid"));
		r.setAmount(rs.getDouble("amount"));
		r.setReceipt(rs.getBlob("receipt"));
		r.setReimbAuthor(rs.getInt("reimbauthor"));
		r.setStatusID(rs.getInt("statusid"));
		r.setSubmitted(rs.getTimestamp("submitted").toLocalDateTime());
		if (rs.getTimestamp("resolved") != null) {
			r.setResolved(rs.getTimestamp("resolved").toLocalDateTime());
		}
		r.setTypeID(rs.getInt("typeid"));
		r.setDescription(rs.getString("description"));

		return r;
	}

	@Override
	public List<Reimbursement> findUserReimbursements(int uid) throws SQLException {
		List<Reimbursement> reimbursements = new ArrayList<Reimbursement>();
		Connection con = conUtil.getConnection();
		PreparedStatement findReimbursement = null;

		String findReimbursementStatement = "SELECT * FROM reimbursements WHERE reimbauthor = ?";

		try {
			findReimbursement = con.prepareStatement(findReimbursementStatement);
			findReimbursement.setInt(1, uid);
			ResultSet rs = findReimbursement.executeQuery();

			while (rs.next()) {
				Reimbursement r = extractReimbursement(rs);
				reimbursements.add(r);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return reimbursements;
	}

	@Override
	public List<Reimbursement> viewPending(int uid) throws SQLException {
		List<Reimbursement> pendingReimbursements = new ArrayList<Reimbursement>();
		Connection con = conUtil.getConnection();
		PreparedStatement findPending = null;
		ReimbursementStatusDaoJdbc rsd = new ReimbursementStatusDaoJdbc();

		String findPendingStatement = "SELECT * FROM reimbursements WHERE reimbauthor = ? AND statusid = "
				+ rsd.getStatus("Pending");

		try {
			findPending = con.prepareStatement(findPendingStatement);

			findPending.setInt(1, uid);
			ResultSet rs = findPending.executeQuery();

			while (rs.next()) {
				Reimbursement r = extractReimbursement(rs);
				pendingReimbursements.add(r);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return pendingReimbursements;
	}

}
