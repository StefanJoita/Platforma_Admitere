package aplicatie_admitere.servicies;


import aplicatie_admitere.models.CandidatiInrolati;
import aplicatie_admitere.models.Ierarhizare;
import aplicatie_admitere.models.OptiuniCandidati;
import aplicatie_admitere.models.Specializari;
import aplicatie_admitere.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SecretarService {

    private final OptiuniCandidatiRepository optiuniCandidatiRepository;

    private final UserRepository userRepository;

    private final CandidatiInrolatiRepository candidatiInrolatiRepository;

    private final IerarhizareRepository ierarhizareRepository;

    private final SpecializariRepository specializariRepository;

    @Autowired
    public SecretarService(OptiuniCandidatiRepository optiuniCandidatiRepository, UserRepository userRepository, CandidatiInrolatiRepository candidatiInrolatiRepository, IerarhizareRepository ierarhizareRepository, SpecializariRepository specializariRepository) {
        this.optiuniCandidatiRepository = optiuniCandidatiRepository;
        this.userRepository = userRepository;
        this.candidatiInrolatiRepository = candidatiInrolatiRepository;
        this.ierarhizareRepository = ierarhizareRepository;
        this.specializariRepository = specializariRepository;
    }


   /* public void generareIerarhizare(Ierarhizare status) {

        List<Ierarhizare> ierarhizareList = ierarhizareRepository.findAllByOrderByNotaDesc();
        List<Specializari> specializariList = specializariRepository.findAll();


        for (Ierarhizare ierarhizare : ierarhizareList) {
                CandidatiInrolati candidat= ierarhizare.getCandidatiInrolati();
                List<OptiuniCandidati>optiuniCandidatList=optiuniCandidatiRepository.findAllByCandidatiInrolati(candidat.getUsers().getCandidatiInrolatis());
                for(int i=0;i<optiuniCandidatList.size();i++)
                {
                    OptiuniCandidati optiuni = optiuniCandidatList.get(i);

                }

        }
    }

    */
}
