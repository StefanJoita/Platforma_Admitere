package aplicatie_admitere.controllers;

import aplicatie_admitere.models.*;
import aplicatie_admitere.repositories.*;
import aplicatie_admitere.servicies.CandidatService;
import aplicatie_admitere.servicies.PdfGeneratorService;
import aplicatie_admitere.servicies.SecretarService;
import org.checkerframework.checker.nullness.Opt;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class SecretarController {

    private final UserRepository userRepo;
    private final CandidatiInrolatiRepository candidatRepo;

    private final CandidatService candidatService;
    private final IerarhizareRepository ierarhizareRepo;

    private final SecretarService secretarService;

    private final OptiuniCandidatiRepository optiuniCandidatiRepository;

    private final SpecializariRepository specializariRepository;

    private final PdfGeneratorService pdfGeneratorService;

    public SecretarController(UserRepository userRepo, CandidatiInrolatiRepository candidatRepo, CandidatService candidatService, IerarhizareRepository ierarhizareRepo, SecretarService secretarService, OptiuniCandidatiRepository optiuniCandidatiRepository, SpecializariRepository specializariRepository, PdfGeneratorService pdfGeneratorService) {

        this.userRepo = userRepo;
        this.candidatRepo=candidatRepo;
        this.candidatService = candidatService;
        this.ierarhizareRepo=ierarhizareRepo;
        this.secretarService = secretarService;
        this.optiuniCandidatiRepository = optiuniCandidatiRepository;
        this.specializariRepository = specializariRepository;
        this.pdfGeneratorService = pdfGeneratorService;
    }

    @PostMapping("/adauga-nota")
    @CrossOrigin("*")
    public ResponseEntity<String> adaugaNota(@RequestBody MedieModel json)
    {   //gasim userul de tip candidat dupa cnp
        Optional<User> user=userRepo.findByCnp(json.getCnp());
        System.out.println(user.get().getEmail());
        Optional<CandidatiInrolati> candidatiInrolati = candidatRepo.findByUsers(user.get());
        if(candidatiInrolati.isPresent())
        {
            Optional<Ierarhizare> candidat=ierarhizareRepo.findByCandidatiInrolati(candidatiInrolati.get());
            System.out.println(candidat.get().getId());
            if(candidat.isPresent())
            {
                Ierarhizare ierarhizare= candidat.get();
                ierarhizare.setNota(json.getNota());
                ierarhizare.setCandidatiInrolati(candidat.get().getCandidatiInrolati());
                ierarhizareRepo.save(ierarhizare);
                System.out.println("A fost adaugat nota candidatului"+candidat.get().getId());
                return ResponseEntity.status(HttpStatus.OK).body(null);
            }
            else
            {
                System.out.println("Utilizatorul cu acest cnp nu e candidat!");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        }
        else {
            System.out.println("Nu exista cnp-ul in baza de date");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

    }

    @PutMapping("/candidati-inrolati/{email}")
    @CrossOrigin("*")
    public ResponseEntity<Void> updateValidare(@PathVariable String email, @RequestBody CandidatiInrolati candidatInrolat) {
        candidatService.updateValidare(email, candidatInrolat.getValidare());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/vizualizare-candidati")
    @CrossOrigin("*")
    public List<VizualizareCandidatiValidati> vizualizareCandidati(@RequestParam(value="candidatiInrolati", required=false) Long userId)
    {
        List<VizualizareCandidatiValidati> responseList = new ArrayList<>();

        List<Ierarhizare> ierarhizareList;

        if(userId!=null)
        {

            ierarhizareList=ierarhizareRepo.findByCandidatiInrolatiId(userId);
        }else {
            ierarhizareList = ierarhizareRepo.findAll();
        }

        for(Ierarhizare ierarhizare : ierarhizareList)
        {
            CandidatiInrolati candidatiInrolati = ierarhizare.getCandidatiInrolati();
            User user = candidatiInrolati.getUsers();

            VizualizareCandidatiValidati response = new VizualizareCandidatiValidati(user.getEmail(), user.getCnp(), ierarhizare.getNota());

            responseList.add(response);

        }
        return responseList;
    }

   /* @GetMapping("/generare-ierarhizare")
    @CrossOrigin("*")
    public ResponseEntity<byte[]> generareIerarhizare(@RequestParam(value="candidatiInrolati", required=false) Long userId) {

        List<IerarhizareDetails> responseList = new ArrayList<>();
        List<Ierarhizare> ierarhizareList;
        List<OptiuniCandidati> optiuniCandidatiList;


        if (userId != null) {
            ierarhizareList = ierarhizareRepo.findByCandidatiInrolatiIdOrderByNotaDesc(userId);
        } else {
            ierarhizareList = ierarhizareRepo.findAllByNotaOrderByDesc();
        }

        for (Ierarhizare ierarhizare : ierarhizareList) {
            CandidatiInrolati candidatiInrolati = ierarhizare.getCandidatiInrolati();
            optiuniCandidatiList= optiuniCandidatiRepository.findAll();

            List<String>optiuni = new ArrayList<>();
                for (OptiuniCandidati optiuniCandidati : optiuniCandidatiList) {

                    if (candidatiInrolati.equals(optiuniCandidati.getCandidatiInrolati())) {

                        if (candidatiInrolati.getId().equals(optiuniCandidati.getCandidatiInrolati().getId())) {
                            Specializari specializare = optiuniCandidati.getSpecializari();
                            optiuni.add(specializare.getDenumire());

                        }

                    }
                }

            User user = candidatiInrolati.getUsers();

            //IerarhizareDetails response = new IerarhizareDetails(user.getName(), user.getCnp(), ierarhizare.getNota(),optiuni);
            //responseList.add(response);

        }

       // return responseList;
            byte[] pdf= pdfGeneratorService.generareIerarhizare(responseList);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            String filename =  "Ierarhizare.pdf";
            headers.setContentDispositionFormData(filename, filename);
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
            ResponseEntity<byte[]> responsee = new ResponseEntity<>(pdf, headers, HttpStatus.OK);
            return responsee;

        //return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }*/
    @GetMapping("/generare-ierarhizare")
    @CrossOrigin("*")
    public ResponseEntity<byte[]> generareIerarhizare() {
        //trebuie sa imi fac o lista cu toti candidatii
        List<CandidatiInrolati> candidatiList= candidatRepo.findAll();
        //map cu id ul candidatului si nota
        List<Map<Long,Float>> noteCandidati= new ArrayList<>();
        for(CandidatiInrolati candidat:candidatiList)
        {
            //System.out.println(candidat.getId());
            Optional<Ierarhizare> ierarhizare=ierarhizareRepo.findByCandidatiInrolati(candidat);
            if (ierarhizare.isPresent()) {
                //System.out.println(candidat.getId());
                //System.out.println(ierarhizare.get().getNota());
                Map<Long, Float> map = new HashMap<>();
                map.put(candidat.getId(), ierarhizare.get().getNota());
                noteCandidati.add(map);
            }
        }
        //sortez lista in functie de nota
       Collections.sort(noteCandidati, Collections.reverseOrder(new Comparator<Map<Long, Float>>() {
            @Override
            public int compare(Map<Long, Float> o1, Map<Long, Float> o2) {
                return o1.values().iterator().next().compareTo(o2.values().iterator().next());
            }
        }));
       // System.out.println("s-a sortat");
        for(Map<Long,Float> detaliiCandidat:noteCandidati)
        {
           // System.out.println("intra");
            for (Map.Entry<Long, Float> entry : detaliiCandidat.entrySet()) {
                System.out.println("Canddidat:Medie  "+entry.getKey() + " : " + entry.getValue());
                //TREBUIE SA IAU OPTIUNILE FIECARUI CANDIDAT
                Optional<CandidatiInrolati> candidat=candidatRepo.findById(entry.getKey());
                if(candidat.isPresent())
                {
                    List<OptiuniCandidati> optiuni=optiuniCandidatiRepository.findAllByCandidatiInrolati(candidat.get());//lista cu optiunile fiecarui candidat
                    //parcurg optiunile
                    int ok=0;
                    for(OptiuniCandidati opt:optiuni)
                    {   if(ok==0)
                        {
                            System.out.println("Optiune:Prioritate" +opt.getId()+" "+opt.getPrioritate());
                            //iau fiecare optiune si o verific in specializari
                            List<Optional<Specializari>> specializari=specializariRepository.findByOptiuniCandidatis(opt);
                           for(Optional<Specializari> specializare:specializari) {
                               System.out.println(specializare.get().getDenumire() + " " + specializare.get().getNr_locuri());
                               if (specializare.get().getNr_locuri() > 0) {
                                   Optional<Ierarhizare> ierarhizare = ierarhizareRepo.findByCandidatiInrolati(candidat);
                                   if (ierarhizare.isPresent()) {
                                       if(ierarhizare.get().getNota()>5) {
                                           ierarhizare.get().setSpecializare(specializare.get());
                                           specializare.get().setNr_locuri(specializare.get().getNr_locuri() - 1);
                                           ierarhizare.get().setStatus("ADMIS");
                                           ierarhizareRepo.save(ierarhizare.get());
                                           specializariRepository.save(specializare.get());
                                           System.out.println("Candidatul cu idul: " + candidat.get().getId() + "a fost repartizat la specializarea: " + specializare.get().getDenumire());
                                           ok = 1;
                                           break;
                                       }
                                       else
                                       {
                                           System.out.println("Candidatul"+candidat.get().getId()+"nu a obtinut nota minim");
                                           ierarhizare.get().setStatus("RESPINS");
                                           ierarhizareRepo.save(ierarhizare.get());
                                           break;
                                       }
                                   }

                               } else
                                   System.out.println("Nu mai sunt locuri la specializare" + specializare.get().getDenumire());
                           }
                       }

                    }
                }

            }
        }

        System.out.println("SE CONSTURUIESTE PDF-UL");
        //construiesc pdf ul
        List<Ierarhizare> ierarhizareList;
        ierarhizareList = ierarhizareRepo.findAllByNotaOrderByDesc();//am luat tot din tabelul de ierarhizare
        List<IerarhizareDetails> responseList = new ArrayList<>();//tot ce am nevoie in pdf
        for(Ierarhizare ierarhizare:ierarhizareList)
        {
            Specializari specializare=ierarhizare.getSpecializare();
            String denumire;
            if(specializare!=null) {
                System.out.println(ierarhizare.getCandidatiInrolati().getId() + "   " + ierarhizare.getNota() + " " + ierarhizare.getStatus() + " " + specializare.getDenumire());
                denumire=specializare.getDenumire();
            }
            else denumire="NEREPARTIZAT";

           IerarhizareDetails response = new IerarhizareDetails(ierarhizare.getCandidatiInrolati().getId(), ierarhizare.getNota(), ierarhizare.getStatus(),denumire);
           responseList.add(response);
        }



        byte[] pdf= pdfGeneratorService.generareIerarhizare(responseList);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        String filename =  "Ierarhizare.pdf";
        headers.setContentDispositionFormData(filename, filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        ResponseEntity<byte[]> responsee = new ResponseEntity<>(pdf, headers, HttpStatus.OK);
        return responsee;
    }

}
