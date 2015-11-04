package org.geeksexception.project.talent.dao;

import java.util.List;

import org.geeksexception.project.talent.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
	
	@Query("SELECT u FROM User u WHERE u.email = ?1")
	User findUserByEmail(String email);
	
	@Query("SELECT u FROM User u WHERE u.userRole = 'ROLE_USER' AND u.talent.status = 'APPROVED'")
	List<User> findTalentUsers(Pageable pageable);
	
	@Query("SELECT u FROM User u WHERE u.userRole = 'ROLE_AGENCY' AND u.agency.status = 'APPROVED'")
	List<User> findAgencyUsers(Pageable pageable);
	
	@Query("SELECT COUNT(f) FROM User u JOIN u.followers f WHERE u = ?1")
	Integer countFollowers(User user);
	
	@Query("SELECT COUNT(f) FROM User u JOIN u.following f WHERE u = ?1")
	Integer countFollowing(User user);
	
}