package com.unistar.myservice4.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.unistar.myservice4.entity.UserEntity;

import java.util.List;

@Repository
public interface UserRepository extends PagingAndSortingRepository<UserEntity, Long> {
    UserEntity findByEmail(String email);
    UserEntity findByUserId(String userId);

    @Query(value = "SELECT * FROM users u",
            countQuery = "SELECT count(*) FROM users u",
            nativeQuery = true)
    Page<UserEntity> findAllUsers(Pageable pageableRequest);

    @Query(value = "SELECT * FROM users u WHERE u.first_name = ?1 AND u.last_name = ?2", nativeQuery = true)
    List<UserEntity> findUserByUserName(String firstName, String lastName);

    @Query(value = "SELECT * FROM users u WHERE u.first_name = ?1", nativeQuery = true)
    List<UserEntity> findUserByFirstName(String firstName);

    @Query(value = "SELECT * FROM users u WHERE u.last_name = :lastName", nativeQuery = true)
    List<UserEntity> findUserByLastName(@Param("lastName") String lastName);

    @Query(value = "SELECT * FROM users u WHERE u.last_name LIKE %:keyword% OR u.first_name LIKE %:keyword%", nativeQuery = true)
    List<UserEntity> findUserByKeyword(@Param("keyword") String keyword);

    @Query(value = "SELECT u.first_name, u.last_name FROM users u WHERE u.last_name LIKE %:keyword% OR u.first_name LIKE %:keyword%", nativeQuery = true)
    List<Object[]> findUserNameByKeyword(@Param("keyword") String keyword);

    @Query("SELECT user FROM UserEntity user WHERE user.userId = :userId")
    UserEntity findUserEntityByUserId(@Param("userId") String userId);

    @Query("SELECT user.firstName, user.lastName FROM UserEntity user WHERE user.userId = :userId")
    List<Object[]> findUserFullNameByUserId(@Param("userId") String userId);
}
