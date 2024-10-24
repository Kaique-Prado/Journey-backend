package com.journey_back.controller;

import com.journey_back.model.ParticipantModel;
import com.journey_back.repository.ParticipantRepository;
import com.journey_back.request.ParticipantRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/participants")
public class ParticipantController {

    @Autowired
    private ParticipantRepository participantRepository;

    @PostMapping("/{id}/confirm")
    public ResponseEntity<ParticipantModel> confirmParticipant(@PathVariable UUID id, @RequestBody ParticipantRequest payload) {
        Optional<ParticipantModel> participant = this.participantRepository.findById(id);

        if (participant.isPresent()) {
            ParticipantModel rawParticipant = participant.get();
            rawParticipant.setIsConfirmed(true);
            rawParticipant.setName(payload.name());

            participantRepository.save(rawParticipant);

            return ResponseEntity.ok(rawParticipant);
        }

        return ResponseEntity.notFound().build();
    }
}
