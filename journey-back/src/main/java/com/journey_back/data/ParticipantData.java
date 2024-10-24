package com.journey_back.data;

import java.util.UUID;

public record ParticipantData(UUID id, String name, String email, Boolean isConfirmed){
}
