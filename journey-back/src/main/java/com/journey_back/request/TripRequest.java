package com.journey_back.request;

import java.time.LocalDateTime;
import java.util.List;

public record TripRequest(String destination, String starts_at, String ends_at, List<String> emails_to_invite, String owner_email, String owner_name) {
}
