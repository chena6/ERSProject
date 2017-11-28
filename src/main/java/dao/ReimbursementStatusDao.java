package dao;

import java.sql.SQLException;

public interface ReimbursementStatusDao {
	public int getStatus(String stat) throws SQLException;
	
}
