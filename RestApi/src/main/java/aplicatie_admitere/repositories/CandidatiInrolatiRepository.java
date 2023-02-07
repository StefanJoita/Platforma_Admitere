package aplicatie_admitere.repositories;

import aplicatie_admitere.models.CandidatiInrolati;
import aplicatie_admitere.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CandidatiInrolatiRepository extends JpaRepository<CandidatiInrolati, Long> {
    public Optional<CandidatiInrolati> findById(Long id);
    public Optional<CandidatiInrolati> findByUsers(User user);

    List<CandidatiInrolati> findByUsersId(Long userId);

   // List<CandidatiInrolati> findByUsersIdAndDocumenteIsNotNullAndValidareIsNull(Long userId);
}
