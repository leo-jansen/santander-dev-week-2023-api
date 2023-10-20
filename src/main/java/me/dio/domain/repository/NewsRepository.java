package me.dio.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import me.dio.domain.model.News;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {

}
