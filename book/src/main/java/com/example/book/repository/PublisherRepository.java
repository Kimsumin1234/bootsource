package com.example.book.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.book.entity.Publisher;

public interface PublisherRepository extends JpaRepository<Publisher, Long> {

}
