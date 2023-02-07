package aplicatie_admitere.repositories;



import aplicatie_admitere.models.CandidatiInrolati;
import aplicatie_admitere.models.OptiuniCandidati;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OptiuniCandidatiRepository extends JpaRepository<OptiuniCandidati, Long> {


    Optional<OptiuniCandidati> findByCandidatiInrolati(CandidatiInrolati candidatiInrolati);
    List<OptiuniCandidati> findAllByCandidatiInrolati(CandidatiInrolati candidatiInrolati);

    @Query("SELECT oc.prioritate FROM OptiuniCandidati oc INNER JOIN oc.candidatiInrolati ci ON ci.id = :idCan")
    List<Optional<Integer>> findByIdCandidat(@Param("idCan") Long idCan);

}
