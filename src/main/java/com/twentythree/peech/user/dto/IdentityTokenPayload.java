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
public class IdentityTokenPayload {
     private String iss;
     private Long iat;
     private Long exp;
     private String aud;
     private String sub;
     private String email;

     @Override
     public boolean equals(Object o) {
          if (this == o) return true;
          if (o == null || getClass() != o.getClass()) return false;
          IdentityTokenPayload that = (IdentityTokenPayload) o;
          return Objects.equals(iss, that.iss) && Objects.equals(iat, that.iat) && Objects.equals(exp, that.exp) && Objects.equals(aud, that.aud) && Objects.equals(sub, that.sub) && Objects.equals(email, that.email);
     }

     @Override
     public int hashCode() {
          return Objects.hash(iss, iat, exp, aud, sub, email);
     }
}
