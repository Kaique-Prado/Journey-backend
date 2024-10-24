package com.journey_back.service;

import com.journey_back.data.ActivityData;
import com.journey_back.data.LinkData;
import com.journey_back.model.ActivityModel;
import com.journey_back.model.LinkModel;
import com.journey_back.model.TripModel;
import com.journey_back.repository.LinkRepository;
import com.journey_back.request.ActivityRequest;
import com.journey_back.request.LinkRequest;
import com.journey_back.response.ActivityResponse;
import com.journey_back.response.LinkResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LinkService {

    @Autowired
    private LinkRepository linkRepository;

    public LinkResponse registerLink(LinkRequest payload, TripModel trip){
        LinkModel newLink = new LinkModel(payload.title(), payload.url(), trip);

        this.linkRepository.save(newLink);

        return new LinkResponse(newLink.getId());
    }

    public List<LinkData> getAllLinksFromTrip(TripModel tripId){
        return this.linkRepository.findByTripId(tripId).stream().map(link -> new LinkData(link.getId(), link.getTitle(), link.getUrl())).toList();
    }
}
