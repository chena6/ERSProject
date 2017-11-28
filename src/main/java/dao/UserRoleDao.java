package dao;

import java.sql.SQLException;

public interface UserRoleDao {
	public int getRole(String r) throws SQLException;
}
