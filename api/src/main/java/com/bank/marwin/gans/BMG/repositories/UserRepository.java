package com.bank.marwin.gans.BMG.repositories;

import com.bank.marwin.gans.BMG.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
}
