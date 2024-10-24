package com.journey_back.repository;

import com.journey_back.model.ActivityModel;
import com.journey_back.model.TripModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ActivityRepository extends JpaRepository<ActivityModel, UUID> {

    @Query("SELECT p FROM ActivityModel p WHERE p.tripId =:tripId")
    List<ActivityModel> findByTripId(TripModel tripId);

}
