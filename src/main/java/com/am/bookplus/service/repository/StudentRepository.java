package com.am.bookplus.service.repository;

import com.am.bookplus.service.entity.EntityStudent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<EntityStudent, String>
{
    
}
