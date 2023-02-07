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
public class SpecializariController {

    private final SpecializariRepository specializariRepo;

    public SpecializariController(SpecializariRepository specializariRepo) {
        this.specializariRepo = specializariRepo;
    }


    @Transactional
    @PostMapping("/adauga-specializare")
    @CrossOrigin("*")
    public ResponseEntity<Specializari> adaugaSpecializare(@RequestBody Specializari request) {
        //if(specializariRepo.exists())
        //{

            //return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Specializarea deja exista");
        //}

        System.out.println(request.getDenumire());
        System.out.println(request.getNr_locuri());
        //printrare nume si nr locuri
        Specializari special = new Specializari();
        special.setDenumire(request.getDenumire());
        special.setNr_locuri(request.getNr_locuri());
        Specializari newSpecializare = specializariRepo.save(special);
        //Specializari specializare =new Specializari(request.getNume(),request.getNr_locuri());
        //Specializari specializare =new Specializari()
        //specilizare(delibas).setNume()
        //speciliaeRepo.save(speciliazre)
        return ResponseEntity.status(HttpStatus.OK).body(newSpecializare);
    }


    @Transactional
    @DeleteMapping("/sterge-specializare/{nume}")
    @CrossOrigin("*")
    public ResponseEntity<String> stergeSpecializare(@PathVariable("nume") String nume) {
        try {
            Specializari specializare = specializariRepo.findByDenumire(nume);
            if (specializare == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Specializarea nu a fost gasita!");
            }
            specializariRepo.delete(specializare);
            return ResponseEntity.status(HttpStatus.OK).body("Specializarea a fost stearsa cu succes!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("A aparut o eroare la stergerea specializarii!");
        }
    }
}
