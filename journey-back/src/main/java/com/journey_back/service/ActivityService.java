package com.journey_back.service;

import com.journey_back.data.ActivityData;
import com.journey_back.model.ActivityModel;
import com.journey_back.model.TripModel;
import com.journey_back.repository.ActivityRepository;
import com.journey_back.request.ActivityRequest;
import com.journey_back.response.ActivityResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityService {
    @Autowired
    private ActivityRepository activityRepository;

    public ActivityResponse registerActivity(ActivityRequest payload, TripModel trip){
        ActivityModel newActivity = new ActivityModel(payload.title(), payload.occurs_at(), trip);

        this.activityRepository.save(newActivity);

        return new ActivityResponse(newActivity.getId());
    }

    public List<ActivityData> getAllActivitiesFromId(TripModel tripId){
        return this.activityRepository.findByTripId(tripId).stream().map(activities -> new ActivityData(activities.getId(), activities.getTitle(), activities.getOccursAt())).toList();
    }
}
