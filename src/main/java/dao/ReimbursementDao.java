package dao;

import java.sql.SQLException;
import java.util.List;

import beans.Reimbursement;

public interface ReimbursementDao {
	void saveReimbursement(Reimbursement r) throws SQLException;
	
	void manageReimbursement(int rid, String ad) throws SQLException;
	
	List<Reimbursement> findAll() throws SQLException;
	
	List<Reimbursement> findUserReimbursements(int uid) throws SQLException;
	
	List<Reimbursement> viewPending(int uid) throws SQLException;
	
}
