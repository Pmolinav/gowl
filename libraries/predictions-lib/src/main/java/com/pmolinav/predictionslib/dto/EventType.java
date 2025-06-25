package com.pmolinav.predictionslib.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EventType {

    H2H("h2h", "Head to Head - winner of the match"),
    H2H_LAY("h2h_lay", "Head to Head - Lay bet against a team to win"),
    SPREADS("spreads", "Handicap betting - adjust score by a point spread"),
    TOTALS("totals", "Over/Under total points scored in the match");

    private final String name;
    private final String description;

    public static EventType fromName(String name) {
        for (EventType type : values()) {
            if (type.name.equalsIgnoreCase(name)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown event type name: " + name);
    }
}
