package com.journey_back.controller;

import com.journey_back.data.ActivityData;
import com.journey_back.data.LinkData;
import com.journey_back.model.TripModel;
import com.journey_back.repository.TripRepository;
import com.journey_back.request.LinkRequest;
import com.journey_back.request.ParticipantRequest;
import com.journey_back.request.TripRequest;
import com.journey_back.response.ActivityResponse;
import com.journey_back.response.LinkResponse;
import com.journey_back.response.ParticipantCreateResponse;
import com.journey_back.response.TripCreateResponse;
import com.journey_back.service.ActivityService;
import com.journey_back.service.LinkService;
import com.journey_back.service.ParticipantService;
import com.journey_back.data.ParticipantData;
import com.journey_back.request.ActivityRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/trips")
public class TripController {

    @Autowired
    private TripRepository tripRepository;
    @Autowired
    private ParticipantService participantService;
    @Autowired
    private ActivityService activityService;
    @Autowired
    private LinkService linkService;

    //Endpoints de VIAGENS

    @PostMapping
    public ResponseEntity<TripCreateResponse> createTrip(@RequestBody TripRequest payload) {
        TripModel newTrip = new TripModel(payload);

        this.tripRepository.save(newTrip);
        this.participantService.resgisterParcicipantsToEvent(payload.emails_to_invite(), newTrip);

        return ResponseEntity.ok(new TripCreateResponse(newTrip.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TripModel> getTripDetails(@PathVariable UUID id) {
        Optional<TripModel> trip = this.tripRepository.findById(id);
        return trip.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<TripModel> updateTrip(@PathVariable UUID id, @RequestBody TripRequest payload) {
        Optional<TripModel> trip = this.tripRepository.findById(id);

        if (trip.isPresent()) {
            TripModel rawTrip = trip.get();
            rawTrip.setStartsAt(LocalDateTime.parse(payload.starts_at(), DateTimeFormatter.ISO_DATE_TIME));
            rawTrip.setEndsAt(LocalDateTime.parse(payload.starts_at(), DateTimeFormatter.ISO_DATE_TIME));
            rawTrip.setDestination(payload.destination());

            this.tripRepository.save(rawTrip);

            return ResponseEntity.ok(rawTrip);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/confirm")
    public ResponseEntity<TripModel> confirmTrip(@PathVariable UUID id) {
        Optional<TripModel> trip = this.tripRepository.findById(id);

        if (trip.isPresent()) {
            TripModel rawTrip = trip.get();
            rawTrip.setIsConfirmed(true);

            this.tripRepository.save(rawTrip);
            this.participantService.triggerConfirmationEmailToParticipants(id);

            return ResponseEntity.ok(rawTrip);
        }
        return ResponseEntity.notFound().build();
    }

    //EndPoints de PARTICIPANTES

    @PostMapping("/{id}/invite")
    public ResponseEntity<ParticipantCreateResponse> inviteParticipant(@PathVariable UUID id, @RequestBody ParticipantRequest payload) {
        Optional<TripModel> trip = this.tripRepository.findById(id);

        if (trip.isPresent()) {
            TripModel rawTrip = trip.get();

            ParticipantCreateResponse participantResponse = this.participantService.registerParticipantToEvent(payload.email(), rawTrip);

            if (rawTrip.getIsConfirmed()) this.participantService.triggerConfirmationEmailToParticipant(payload.email());

            return ResponseEntity.ok(participantResponse);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/participants")
    public ResponseEntity<List<ParticipantData>> getAllParticipants(@PathVariable TripModel id) {
        List<ParticipantData> participantList = this.participantService.getAllParticipantsFromEvent(id);
        return ResponseEntity.ok(participantList);
    }

    //EndPoints de Atividades

    @PostMapping("/{tripId}/activities")
    public ResponseEntity<ActivityResponse> registerActivity(@PathVariable UUID tripId, @RequestBody ActivityRequest payload) {
        Optional<TripModel> trip = this.tripRepository.findById(tripId);

        if (trip.isPresent()) {
            TripModel rawTrip = trip.get();

            ActivityResponse activityResponse = this.activityService.registerActivity(payload, rawTrip);

            return ResponseEntity.ok(activityResponse);

        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/activities")
    public ResponseEntity<List<ActivityData>> getAllActivities(@PathVariable TripModel id) {
        List<ActivityData> activitiesList = this.activityService.getAllActivitiesFromId(id);
        return ResponseEntity.ok(activitiesList);
    }

    //EndPoints de Links

    @PostMapping("/{tripId}/links")
    public ResponseEntity<LinkResponse> registerLink(@PathVariable UUID tripId, @RequestBody LinkRequest payload) {
        Optional<TripModel> trip = this.tripRepository.findById(tripId);

        if (trip.isPresent()) {
            TripModel rawTrip = trip.get();

            LinkResponse linkResponse = this.linkService.registerLink(payload, rawTrip);

            return ResponseEntity.ok(linkResponse);

        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/links")
    public ResponseEntity<List<LinkData>> getAllLinks(@PathVariable TripModel id) {
        List<LinkData> linksList = this.linkService.getAllLinksFromTrip(id);
        return ResponseEntity.ok(linksList);
    }

}
