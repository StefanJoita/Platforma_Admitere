package aplicatie_admitere.repositories;

import aplicatie_admitere.models.CandidatiInrolati;
import aplicatie_admitere.models.OptiuniCandidati;
import aplicatie_admitere.models.Specializari;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpecializariRepository extends JpaRepository<Specializari,Long> {

    @Query("SELECT denumire, nr_locuri,specializare_id FROM Specializari")
    List<Object[]> getAllDenumireAndNrLocuri();


    Specializari findByDenumire(String denumire);

    List<Optional<Specializari>> findByOptiuniCandidatis(OptiuniCandidati optiune);

    @Query(value = "SELECT denumire FROM specializari WHERE specializare_id = :id", nativeQuery = true)
    String findDenumireById(@Param("id") int id);
}
