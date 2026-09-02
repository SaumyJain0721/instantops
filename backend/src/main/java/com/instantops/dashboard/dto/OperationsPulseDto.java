package com.instantops.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OperationsPulseDto {

    private long pending;
    private long assigned;
    private long onTheWay;
    private long inProgress;
    private long completed;
    private long cancelled;
}
