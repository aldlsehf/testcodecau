package com.cau.swtestcode.repository;

import com.cau.swtestcode.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}
