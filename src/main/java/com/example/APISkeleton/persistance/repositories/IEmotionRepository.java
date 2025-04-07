package com.example.APISkeleton.persistance.repositories;

import com.example.APISkeleton.persistance.entities.Emotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IEmotionRepository extends JpaRepository<Emotion, Long> {

    Optional<Emotion> findByName(String name);

    List<Emotion> findAllByOrderByNameAsc();
}