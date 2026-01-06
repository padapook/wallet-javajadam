package com.example.walletja.features.user.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEventDto {
    // ไม่เอา pw มาใส่ใน user evemt dto
    private String username;
}
