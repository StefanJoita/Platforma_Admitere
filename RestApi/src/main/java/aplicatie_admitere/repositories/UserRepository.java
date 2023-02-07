package aplicatie_admitere.repositories;

import aplicatie_admitere.models.CandidatiInrolati;
import aplicatie_admitere.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    public Optional<User> findByEmail(String email);

    public Optional<User> findByActivationToken(String token);
    public Optional<User> findUserByRoles(String role);
    public boolean existsByEmail(String email);
    public Optional<User> findByOtp(String otp);
    public Optional<User> findByCnp(String cnp);
    public Optional<User> findByCandidatiInrolatis(CandidatiInrolati candidat);
}
