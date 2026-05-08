package com.Nhom20.DoAnPhamMem.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsDTO {
    private int totalBloodUnits;
    private int newVolunteers;
    private int activeCampaigns;
    private double screeningPassRate;
}
