package com.journey_back.repository;

import com.journey_back.model.ParticipantModel;
import com.journey_back.model.TripModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ParticipantRepository extends JpaRepository<ParticipantModel, UUID> {

    @Query("SELECT p FROM ParticipantModel p WHERE p.tripId =:tripId")
    List<ParticipantModel> findByTripId(TripModel tripId);
}
