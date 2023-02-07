package aplicatie_admitere.repositories;


import aplicatie_admitere.models.CandidatiInrolati;
import aplicatie_admitere.models.Ierarhizare;
import aplicatie_admitere.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IerarhizareRepository extends JpaRepository<Ierarhizare, Long> {


    List<Ierarhizare> findByCandidatiInrolatiId(Long userId);

    Optional<Ierarhizare> findByCandidatiInrolati(CandidatiInrolati candidatiInrolati);

    Optional<Ierarhizare> findByCandidatiInrolati(Optional<CandidatiInrolati> candidatInrolat);


    @Query("SELECT i FROM Ierarhizare i WHERE i.candidatiInrolati.id = :userId ORDER BY i.nota DESC")
    List<Ierarhizare> findByCandidatiInrolatiIdOrderByNotaDesc(Long userId);
    @Query("SELECT i FROM Ierarhizare i ORDER BY i.nota DESC")
    List<Ierarhizare> findAllByNotaOrderByDesc();
}
