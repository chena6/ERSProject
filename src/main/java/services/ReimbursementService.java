package services;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import beans.Reimbursement;
import beans.User;
import dao.ReimbursementDao;
import dao.ReimbursementDaoJdbc;

public class ReimbursementService {
	private ReimbursementDao rd = new ReimbursementDaoJdbc();
	
	public void save(Reimbursement r, User u) throws SQLException {
		rd.saveReimbursement(r, u);
	}
	
	public List<Reimbursement> findAll() throws SQLException{
		return rd.findAll();
	}
	
	public List<Reimbursement> getReimb(int uid) throws SQLException {
		return rd.findUserReimbursements(uid);
	}
	
	public void manageReimb(List<Reimbursement> rl) throws SQLException {
		for (Reimbursement r : rl) {
			if (r.getStatusID() == 2 || r.getStatusID() == 3 ) {
				LocalDateTime now = LocalDateTime.now();
				r.setResolved(now);
			}
				
		}
		rd.manageReimbursement(rl);
	}
	
}
