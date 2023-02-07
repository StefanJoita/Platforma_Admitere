package aplicatie_admitere.controllers;

import aplicatie_admitere.models.*;
import aplicatie_admitere.repositories.*;
import aplicatie_admitere.servicies.CandidatService;
import aplicatie_admitere.servicies.PdfGeneratorService;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import jakarta.transaction.Transactional;
import org.apache.coyote.http11.upgrade.UpgradeServletOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.sql.SQLException;
import java.util.*;


@RestController
@CrossOrigin("*")
@RequestMapping("/arhivare-sesiune")
public class AdminController {

    private final UserRepository userRepo;
    private final CandidatiInrolatiRepository candidatRepo;

    private final PdfGeneratorService pdfGeneratorService;
    private final OptiuniCandidatiRepository optiuniRepo;
    private final IerarhizareRepository ierarhizareRepo;
    private final SpecializariRepository specializariRepo;



    @Autowired
    private JdbcTemplate jdbcTemplate;

    public AdminController(UserRepository userRepo, CandidatiInrolatiRepository candidatRepo, PdfGeneratorService pdfGeneratorService, CandidatService candidatService, PdfGeneratorService pdfGeneratorService1, OptiuniCandidatiRepository optiuniRepo, IerarhizareRepository ierarhizareRepo, SpecializariRepository specializariRepo) {
        this.userRepo = userRepo;
        this.candidatRepo = candidatRepo;
        this.pdfGeneratorService = pdfGeneratorService1;
        this.optiuniRepo = optiuniRepo;
        this.ierarhizareRepo = ierarhizareRepo;
        this.specializariRepo = specializariRepo;
    }



    @GetMapping
    public ResponseEntity arhivareSesiune() throws SQLException, IOException, DocumentException {

        List<List<String>> listaCandidati = new ArrayList<>();
        List<CandidatiInrolati> candidatInrolat = candidatRepo.findAll();
        for(CandidatiInrolati candidat: candidatInrolat) {
            List<String> detaliiCandidat = new ArrayList<>();
            Optional<User> user = userRepo.findByCandidatiInrolatis(candidat);
            if (user.isPresent()) {
                detaliiCandidat.add(user.get().getName());
                detaliiCandidat.add(user.get().getEmail());
                detaliiCandidat.add(user.get().getCnp());
                detaliiCandidat.add(user.get().getPhone());
            }
            List<OptiuniCandidati> optiuni = optiuniRepo.findAllByCandidatiInrolati(candidat);
            if (optiuni.isEmpty())
            {
                detaliiCandidat.add("Optiunea nu exista");
                detaliiCandidat.add("Optiunea nu exista");
                detaliiCandidat.add("Optiunea nu exista");
            }
            for(OptiuniCandidati optiune: optiuni)
            {
                Optional<Specializari> specializare = specializariRepo.findById(optiune.getSpecializari().getSpecializare_id());
                if(specializare.isPresent()) {
                    detaliiCandidat.add(("Optiunea " + optiune.getPrioritate() + " a fost " + specializare.get().getDenumire()));
                } else
                {
                    System.out.println("n-am intrat");
                    detaliiCandidat.add("Optiunea nu a fost selectata.");
                }
            }
            Optional<Ierarhizare> ierarhizare = ierarhizareRepo.findByCandidatiInrolati(candidat);
            if(ierarhizare.isPresent())
            {
                System.out.println("aici ba");
               // System.out.println( ierarhizare.get().getSpecializare().getSpecializare_id());
                if(ierarhizare.get().getSpecializare()!=null) {
                    Optional<Specializari> specializare = specializariRepo.findById(ierarhizare.get().getSpecializare().getSpecializare_id());

                    if (specializare.isPresent()) {
                        detaliiCandidat.add(("NOTA: " + ierarhizare.get().getNota() + " la specializarea: " + specializare.get().getDenumire()));
                    } else {
                        detaliiCandidat.add("Nicio nota sau specializare disponibile.");
                    }
                }
                else
                    detaliiCandidat.add(("NOTA: " + ierarhizare.get().getNota() +": NEREPARTIZAT"));
            } else {
                detaliiCandidat.add("Nicio ierarhizare disponibila.");
            }
            listaCandidati.add(detaliiCandidat);
        }
        pdfGeneratorService.genereazaArhivaSesiune(listaCandidati,"arhivare-sesiune.pdf");

        List<String> getLinks = jdbcTemplate.queryForList("SELECT documente FROM dbo.candidati_inrolati", String.class);
        String originalFileName = null;

        Iterator<String> iterator = getLinks.iterator();
        while (iterator.hasNext()) {
            String s = iterator.next();
            if (s == null || s.equals("null")) {
                iterator.remove();
            }
        }
        for (String link : getLinks) {
            try {
                URL url = new URL(link);
                URLConnection connection = url.openConnection();
                InputStream in = connection.getInputStream();

                // Create the new folder if it doesn't exist
                File folder = new File("arhivare sesiune");
                if (!folder.exists()) {
                    folder.mkdir();
                }

                originalFileName = url.toString().substring(25);
                int startIndex = originalFileName.indexOf("/");
                originalFileName = originalFileName.substring(0, startIndex);

                FileOutputStream fos = new FileOutputStream(folder.getAbsolutePath() + File.separator + originalFileName + ".zip");
                byte[] buffer = new byte[1024];
                int length;
                while ((length = in.read(buffer)) > 0) {
                    fos.write(buffer, 0, length);
                }
                fos.close();
                in.close();
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.OK).body("Pentru " + originalFileName + " link-ul a expirat!");
            }
        }
        return ResponseEntity.ok().body("ok");
    }




}



