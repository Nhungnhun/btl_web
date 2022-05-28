package hotel.dao;

import hotel.entity.Role;

public interface RoleDao {

	public Role findRoleByName(String roleName);
	
}
