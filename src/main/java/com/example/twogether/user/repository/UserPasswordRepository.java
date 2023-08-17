package com.example.twogether.user.repository;

import com.example.twogether.user.entity.UserPassword;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserPasswordRepository extends JpaRepository<UserPassword, Long> {

    void deleteAllByUser_Id(Long id);

    @Query("select up from UserPassword up where up.user.id = :id order by up.createdAt desc limit 3")
    List<UserPassword> get3RecentPasswords(@Param("id") Long userId);
}
