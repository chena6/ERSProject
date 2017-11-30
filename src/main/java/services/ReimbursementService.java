package services;

import java.sql.SQLException;
import java.util.List;

import beans.Reimbursement;
import dao.ReimbursementDao;
import dao.ReimbursementDaoJdbc;

public class ReimbursementService {
	private ReimbursementDao rd = new ReimbursementDaoJdbc();
	
	public void save(Reimbursement r) throws SQLException {
		rd.saveReimbursement(r);
	}
	
	public List<Reimbursement> findAll() throws SQLException{
		return rd.findAll();
	}
	
	public List<Reimbursement> getReimb(int uid) throws SQLException {
		return rd.findUserReimbursements(uid);
	}
}
