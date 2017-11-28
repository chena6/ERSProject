package dao;

import java.sql.SQLException;

public interface ReimbursementTypeDao {
	public int getType(String t) throws SQLException;
}
