package com.am.bookplus.service.repository;

import com.am.bookplus.service.entity.EntityRegister;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegisterRepository extends JpaRepository<EntityRegister, Integer>
{

}
