package aplicatie_admitere.controllers;

import aplicatie_admitere.models.*;
import aplicatie_admitere.repositories.CandidatiInrolatiRepository;
import aplicatie_admitere.repositories.OptiuniCandidatiRepository;
import aplicatie_admitere.repositories.SpecializariRepository;
import aplicatie_admitere.repositories.UserRepository;
import aplicatie_admitere.servicies.CandidatService;
import aplicatie_admitere.servicies.PdfGeneratorService;
import jdk.swing.interop.SwingInterOpUtils;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//aici vor fi toate endpointurile ce tin de candidat
@RestController
public class CandidatController {
    private final UserRepository userRepo;
    private final CandidatiInrolatiRepository candidatRepo;

    private final OptiuniCandidatiRepository optiuniRepo;

    private final PdfGeneratorService pdfGeneratorService;

    private final CandidatService candidatService;
    private final SpecializariRepository specializariRepository;

    public CandidatController(UserRepository userRepo, CandidatiInrolatiRepository candidatRepo,PdfGeneratorService pdfGeneratorService,CandidatService candidatservice,
                              SpecializariRepository specializariRepository,OptiuniCandidatiRepository optiuniRepo) {
        this.userRepo = userRepo;
        this.candidatRepo = candidatRepo;
        this.pdfGeneratorService=pdfGeneratorService;
        this.candidatService=candidatservice;
        this.specializariRepository = specializariRepository;
        this.optiuniRepo = optiuniRepo;
    }

    //endpoint pentru generarea de legitimatie de concurs
    @PostMapping("/generare-legitimatie")
    @CrossOrigin("*")
    public ResponseEntity<byte[]> genereazaLegitimatie(@RequestBody String emailJson) throws JSONException {

        JSONObject json = new JSONObject(emailJson);
        // Get the value for the "email" field
        String email = json.getString("email");
        Optional<User> user=userRepo.findByEmail(email);
        if(user.isPresent()) {
            if (user.get().getRoles().equals("CANDIDAT"))
            {
                Optional<CandidatiInrolati> candidat=candidatRepo.findByUsers(user.get());
                if(candidat.isPresent())
                {
                    String name = user.get().getName();
                    String cnp = user.get().getCnp();
                    String phone = user.get().getPhone();
                    Long id=candidat.get().getId();
                    byte[] pdf = pdfGeneratorService.genereazaLegitimatie(name, cnp, phone,id);
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_PDF);
                    String filename = email + ".pdf";
                    headers.setContentDispositionFormData(filename, filename);
                    headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
                    ResponseEntity<byte[]> response = new ResponseEntity<>(pdf, headers, HttpStatus.OK);
                    return response;
                }
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
    @GetMapping("/afisare-specializari")
    @CrossOrigin("*")
    public List<Object[]> getSpecializari()
    {
        return candidatService.getSpecializari();
    }

    @GetMapping("/get-arhive-candidati")
    @CrossOrigin("*")
    public List<CandidatiInrolatiDetails> getCandidatiInrolatiByUser(@RequestParam(value="users", required=false) Long userId) {
        List<CandidatiInrolatiDetails> responseList = new ArrayList<>();

        List<CandidatiInrolati> candidatiInrolatiList;
        if (userId != null) {
            candidatiInrolatiList = candidatRepo.findByUsersId(userId);
        } else {
            candidatiInrolatiList = candidatRepo.findAll();
        }

        for (CandidatiInrolati candidatiInrolati : candidatiInrolatiList) {
            User user = candidatiInrolati.getUsers();
            CandidatiInrolatiDetails response = new CandidatiInrolatiDetails(user.getEmail(), candidatiInrolati.getDocumente());
            if(candidatiInrolati.getDocumente()!=null && candidatiInrolati.getValidare() == null)
            {

                responseList.add(response);
            }
        }
        return responseList;
    }

    @GetMapping("/get-status-documente/{email}")
    @CrossOrigin("*")
    public ResponseEntity<Object> getStatus(@PathVariable String email)
    {
        Optional<User> user = userRepo.findByEmail(email);
        String statusDocumente = null;
        if (user.isPresent()) {
            long userId = user.get().getId();
            Optional<CandidatiInrolati> candidatInrolat = candidatRepo.findByUsers(user.get());
            statusDocumente=candidatInrolat.get().getValidare();
            System.out.println(candidatInrolat.get().getId());
            return ResponseEntity.status(HttpStatus.OK).body(statusDocumente);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @PutMapping("/adaugare-optiuni/{email}")
    @CrossOrigin("*")
    public ResponseEntity<Void> adaugareOptiuni(@PathVariable String email, @RequestBody List<Specializari> specializari) {
        candidatService.adaugareOptiuni(email, specializari);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/get-status-optiuni/{email}")
    @CrossOrigin("*")
    public ResponseEntity<Object> getStatusOptiuni(@PathVariable String email)
    {
        Optional<User> user = userRepo.findByEmail(email);
        String statusOptiuni = "null";
        if (user.isPresent()) {
            long userId = user.get().getId();
            Optional<CandidatiInrolati> candidatInrolat = candidatRepo.findByUsers(user.get());

            if (candidatInrolat.isPresent()) {
                Long idCandidatInrolat = candidatInrolat.get().getId();
                //System.out.println(idCandidatInrolat);

                List<Optional<Integer>> specializariAlese = optiuniRepo.findByIdCandidat(idCandidatInrolat);
                if(specializariAlese.isEmpty()) {
                    //System.out.println("Nu a fost validat");
                    statusOptiuni = "NOT VALID";
                } else {
                    for(Optional<Integer> specializare: specializariAlese) {
                        if(specializare.isPresent()){
                            //System.out.println(specializare.get());
                            statusOptiuni = "EXISTA";
                        }
                        else
                            statusOptiuni = "NU EXISTA";
                            //System.out.println("Nu au fost alese optionile!");
                    }
                }
            }
            return ResponseEntity.status(HttpStatus.OK).body(statusOptiuni);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

}
