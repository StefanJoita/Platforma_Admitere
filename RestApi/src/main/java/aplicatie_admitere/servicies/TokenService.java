package aplicatie_admitere.servicies;

import aplicatie_admitere.models.User;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
/* servicul de generare a tokenului jwt
* aceste serviciu NU SE VA MODIFICA(DONT TOUCH IF IT WORKS)*/
@Service
public class TokenService {
    private final JwtEncoder encoder;
    public TokenService(JwtEncoder encoder) {
        this.encoder = encoder;
    }
    /*Metoda de generare a token-ului
    * tokenul contine issuer-ul, timestampul la care a fost generat, timestampul cat este valabil(elibarat+8ore)
    * email ul destinatarului si si rolul*/
    public String generateToken(User user) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("Platforma-AdmitereATM")
                .issuedAt(now)
                .expiresAt(now.plus(8, ChronoUnit.HOURS))
                .subject(user.getEmail())
                .claim("roles", user.getRoles())
                .build();
        return this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
