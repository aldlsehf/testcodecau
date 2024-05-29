package com.cau.swtestcode.repository;

import com.cau.swtestcode.domain.Member;
import com.cau.swtestcode.domain.Project;
import com.cau.swtestcode.domain.Users;
import com.cau.swtestcode.domain.enumClass.UserType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findAllByProjectAndUserType(Project project, UserType userType);

    Optional<Member> findByProject_ProjectIdAndUser_UserId(Long projectId, Long userId);
    //used to get ticket list api, update status api

    List<Member> findByUserEmail(String email);
}
