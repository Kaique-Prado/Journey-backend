package com.journey_back.repository;

import com.journey_back.model.TripModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TripRepository extends JpaRepository<TripModel, UUID> {
}
