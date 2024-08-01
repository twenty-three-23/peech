package com.twentythree.peech.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Objects;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class IdentityTokenHeader {

    private String alg;
    private String kid;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IdentityTokenHeader that = (IdentityTokenHeader) o;
        return Objects.equals(alg, that.alg) && Objects.equals(kid, that.kid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(alg, kid);
    }
}
