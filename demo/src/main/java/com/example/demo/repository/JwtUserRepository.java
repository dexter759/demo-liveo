package com.example.demo.repository;

import com.example.demo.entity.JwtUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface JwtUserRepository extends JpaRepository<JwtUser, String> {


}
