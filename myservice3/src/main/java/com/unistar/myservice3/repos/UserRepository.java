package com.unistar.myservice3.repos;

import com.unistar.myservice3.model.UserDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import com.unistar.myservice3.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer>
{
	static String FIND_ALLUSERS = "SELECT DISTINCT new com.unistar.myservice3.model.UserDTO(u.id, u.name, u.email, u.disabled) FROM User u";
	static String BY_EMAIL = " WHERE u.email = :email";

	@Query(FIND_ALLUSERS + BY_EMAIL)
	List<UserDTO> findByEmail(@Param("email")String email);
}

