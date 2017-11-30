package dao;

import java.sql.SQLException;
import java.util.List;

import beans.Reimbursement;
import beans.User;

public interface ReimbursementDao {
	void saveReimbursement(Reimbursement r, User u) throws SQLException;
	
	void manageReimbursement(List<Reimbursement> rl) throws SQLException;
	
	List<Reimbursement> findAll() throws SQLException;
	
	List<Reimbursement> findUserReimbursements(int uid) throws SQLException;
	
	List<Reimbursement> viewPending(int uid) throws SQLException;
	
}
