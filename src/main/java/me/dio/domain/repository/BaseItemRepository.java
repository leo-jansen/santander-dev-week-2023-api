package me.dio.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import me.dio.domain.model.BaseItem;

@Repository
public interface BaseItemRepository extends JpaRepository<BaseItem, Long>{
  
}
