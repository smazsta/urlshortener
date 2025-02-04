package com.example.urlshortener.repository;

import com.example.urlshortener.model.UrlEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UrlRepository extends JpaRepository<UrlEntity, Long> {
  Optional<UrlEntity> findByShortUrl(String shortUrl);
}
