package me.dio.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import me.dio.domain.model.Feature;

@Repository
public interface FeatureRepository extends JpaRepository<Feature, Long> {

}
