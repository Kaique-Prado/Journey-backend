package com.journey_back.service;

import com.journey_back.data.ParticipantData;
import com.journey_back.model.ParticipantModel;
import com.journey_back.model.TripModel;
import com.journey_back.repository.ParticipantRepository;
import com.journey_back.response.ParticipantCreateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ParticipantService {
    @Autowired
    ParticipantRepository participantRepository;

    public void resgisterParcicipantsToEvent(List<String> participantsToInvite, TripModel trip) {
        List<ParticipantModel> participants = participantsToInvite.stream().map(email -> new ParticipantModel(email, trip)).toList();

        this.participantRepository.saveAll(participants);
        System.out.println(participants.get(0).getId());

    }

    public ParticipantCreateResponse registerParticipantToEvent(String email, TripModel trip) {
        ParticipantModel newParticipant = new ParticipantModel(email, trip);
        participantRepository.save(newParticipant);

        return new ParticipantCreateResponse(newParticipant.getId());
    }

    public void triggerConfirmationEmailToParticipants(UUID tripId) {
    }

    public void triggerConfirmationEmailToParticipant(String email) {
    }

    public List<ParticipantData> getAllParticipantsFromEvent(TripModel tripId) {
        return this.participantRepository.findByTripId(tripId).stream().map(participant -> new ParticipantData(participant.getId(), participant.getName(), participant.getEmail(), participant.getIsConfirmed())).toList();
    }
}
