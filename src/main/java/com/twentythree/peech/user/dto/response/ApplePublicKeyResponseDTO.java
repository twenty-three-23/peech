package com.twentythree.peech.user.dto.response;

import com.twentythree.peech.user.dto.ApplePublicKey;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ApplePublicKeyResponseDTO {
    private List<ApplePublicKey> applePublicKeys;
}
