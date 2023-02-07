package aplicatie_admitere.servicies;


import aplicatie_admitere.models.*;
import aplicatie_admitere.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CandidatService {

    private final SpecializariRepository specializariRepository;

    private final UserRepository userRepo;

    private final  CandidatiInrolatiRepository candidatInrolatRepo;

    private final OptiuniCandidatiRepository optiuniCandidatiRepository;

    private final IerarhizareRepository ierarhizareRepository;
    @Autowired
    public CandidatService(SpecializariRepository specializariRepository, UserRepository userRepo, CandidatiInrolatiRepository candidatInrolatRepo, OptiuniCandidatiRepository optiuniCandidatiRepository, IerarhizareRepository ierarhizareRepository) {
        this.specializariRepository = specializariRepository;
        this.userRepo = userRepo;
        this.candidatInrolatRepo = candidatInrolatRepo;
        this.optiuniCandidatiRepository = optiuniCandidatiRepository;
        this.ierarhizareRepository = ierarhizareRepository;
    }

    public List<Object[]> getSpecializari()
    {
        return specializariRepository.getAllDenumireAndNrLocuri();
    }

    public ResponseEntity<Object> updateValidare(String email, String validare) {
        Optional<User> user = userRepo.findByEmail(email);
        Optional<CandidatiInrolati> candidat = candidatInrolatRepo.findByUsers(user.get());
        System.out.println(validare);
        if(candidat.isPresent())
        {
            CandidatiInrolati candidatt = candidat.get();

            if(validare == "NULL") {
                candidatt.setValidare(null);
            }
            else {
                candidatt.setValidare(validare);
                candidatInrolatRepo.save(candidatt);
            }
            if(validare.equals("VALIDAT"))
            {//adaugam candidatii care au primit validare pozitiva in tabela de optiuni de atatea ori cate specializari exista.
                List<Specializari> nrSpecializari;
                nrSpecializari=specializariRepository.findAll();

                for(Specializari specializari : nrSpecializari) {
                    OptiuniCandidati idCandidatt = new OptiuniCandidati();
                    idCandidatt.setCandidatiInrolati(candidatt);
                    optiuniCandidatiRepository.save(idCandidatt);
                }

                Ierarhizare adaugareCandidatInIerarhizare = new Ierarhizare();
                adaugareCandidatInIerarhizare.setCandidatiInrolati(candidatt);
                ierarhizareRepository.save(adaugareCandidatInIerarhizare);

                System.out.println("Candidatul a fost validat dupa verificarea documentelor!");
            }
            else
            {
                System.out.println("Candidatul nu a fost validat dupa verificarea documentelor!");
            }

        }
        else
        {
            System.out.println("Utilizatorul nu a fost gasit!");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }


        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }



    public void adaugareOptiuni(String email, List<Specializari> specializari) {


        for(int i=0;i<specializari.size();i++)
        {

        Optional<User> user = userRepo.findByEmail(email);
        Optional<CandidatiInrolati> candidat = candidatInrolatRepo.findByUsers(user.get());
        List<OptiuniCandidati> candidatt = optiuniCandidatiRepository.findAllByCandidatiInrolati(candidat.get());

            for(int j=0;j<candidatt.size();j++) {
                OptiuniCandidati optiuni = candidatt.get(i);
                if (optiuni.getSpecializari()==null) {
                    optiuni.setPrioritate(i+1);
                    optiuni.setSpecializari(specializari.get(i));
                    optiuniCandidatiRepository.save(optiuni);
                }
            }
        }
    }



}
