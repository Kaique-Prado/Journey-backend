package com.journey_back.repository;

import com.journey_back.model.LinkModel;
import com.journey_back.model.TripModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface LinkRepository extends JpaRepository<LinkModel, UUID> {
    @Query("SELECT p FROM LinkModel p WHERE p.tripId =:tripId")
    public List<LinkModel> findByTripId(TripModel tripId);
}
