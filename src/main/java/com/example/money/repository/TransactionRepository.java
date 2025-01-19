package com.example.money.repository;

import com.example.money.entity.UserTranscation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<UserTranscation,Integer> {

    List<UserTranscation> findBySenderId(Integer senderId);
    List<UserTranscation> findByReceiverId(Integer receiverId);
}
