package aplicatie_admitere.controllers;


import aplicatie_admitere.models.*;
import aplicatie_admitere.repositories.CandidatiInrolatiRepository;
import aplicatie_admitere.repositories.SpecializariRepository;
import aplicatie_admitere.repositories.UserRepository;
import aplicatie_admitere.servicies.EmailPasswordService;
import aplicatie_admitere.servicies.TokenService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.context.annotation.Role;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Optional;

/*Rest Controller ce va expune endpointuri pentru creare de conturi si log in*/
@RestController
public class AuthController {

    private static final Logger LOG = LoggerFactory.getLogger(AuthController.class);

    private final TokenService tokenService;

    private final EmailPasswordService emailPasswordService;

    private final UserRepository userRepo;

    private PasswordEncoder passwordEncoder;
    private CandidatiInrolatiRepository candidatRepo;


    public AuthController(TokenService tokenService, UserRepository userRepo, PasswordEncoder passwordEncoder, EmailPasswordService emailPasswordService, CandidatiInrolatiRepository candidatRepo) {
        this.tokenService = tokenService;
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.emailPasswordService = emailPasswordService;
        this.candidatRepo = candidatRepo;
    }


    private boolean checkUserRole(User user, String role) {
        if (Arrays.stream(user.getRoles().split(",")).anyMatch(role::equals)) {
            return true;
        } else return false;
    }

    //end point pentru realizare operatiunii de log-in;
    //authentication cuprinde parola si emailul, functia returneaza tokenul jwt care va fi folosit de catre user
    //functia verifica daca emailul exista in baza de date apoi verifica parola
    @PostMapping("/login")
    @CrossOrigin("*")
    public ResponseEntity<String> token(@RequestBody SignInRequest authentication) {
        Optional<User> user = userRepo.findByEmail(authentication.getEmail());
        if (user.isPresent()) {
            if (passwordEncoder.matches(authentication.getPassword(), user.get().getPassword())) {
                //String token = tokenService.generateToken(user.get());
                //LOG.debug("Token: {}", token);
                //user.get().setActivationToken(token);
                //serRepo.save(user.get());
                //System.out.println( "Token:" + token);
                String otp = emailPasswordService.generateOTP();
                // emailPasswordService.sendOtp(user.get().getEmail(),otp);
                Instant now = Instant.now();
                user.get().setOtp(otp);
                user.get().setOtpTime(now);
                userRepo.save(user.get());
                return ResponseEntity.status(HttpStatus.OK).body("OK! S-a trimis codul!");
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @PostMapping("/2fa-send")
    @CrossOrigin("*")

    public ResponseEntity<String> sendFaCode(@RequestBody String email) throws JSONException {
        JSONObject json = new JSONObject(email);
        // Get the value for the "email" field
        String emailValue = json.getString("email");
        Optional<User> user = userRepo.findByEmail(emailValue);
        System.out.println(email);
        if (user.isPresent()) {
            String otp = user.get().getOtp();
            emailPasswordService.sendOtp(user.get().getEmail(), otp);
            return ResponseEntity.status(HttpStatus.OK).body("OK! S-a trimis codul!");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("NOT FOUND! Userul nu a fost gasit! Nu s-a tr");
    }

    @PostMapping("/2fa-validate")
    @CrossOrigin("*")
    public ResponseEntity<String> validate2FaCode(@RequestBody String codeJson) throws JSONException {
        JSONObject json = new JSONObject(codeJson);
        // Get the value for the "code" field
        String code = json.getString("code");
        Optional<User> user = userRepo.findByOtp(code);
        System.out.println(user.get().getEmail());
        if (user.isPresent()) {
            Instant now = Instant.now();
            Duration tenMinutes = Duration.ofMinutes(10);
            Instant otpPlusTen = user.get().getOtpTime().plus(tenMinutes);
            int comparasion = otpPlusTen.compareTo(now);
            System.out.println(otpPlusTen);
            System.out.println(now);
            if (comparasion < 0) {
                return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body("OTP expirat!");
            }
            String token = tokenService.generateToken(user.get());
            return ResponseEntity.status(HttpStatus.OK).body(token);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nu ti-ai generat cod!");
    }

    //end point pentru crearea conturilor de admin, se va comenta ulterior avand in vedere ca adminii sunt predefiniti
    //si ei se vor ocupa de creare de conturi pentru celelalte categorii de useri
    @PostMapping("/create-admin")
    @CrossOrigin("*")
    public ResponseEntity<User> createAdmin(@RequestBody User user) {
        LOG.debug("Creating new admin");
        if (userRepo.existsByEmail(user.getEmail())) {
            LOG.debug("User ul exista");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        }
        Instant now = Instant.now();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles("ADMIN");
        User newUser = userRepo.save(user);
        LOG.debug("User: {} created", newUser.getEmail());

        return ResponseEntity.status(HttpStatus.OK).body(newUser);
    }


    @PostMapping("/create-rcz")
    @CrossOrigin("*")
    public ResponseEntity<User> createResponsabil(@RequestBody SignUpRequest request) {
        LOG.debug("Creating new Responsabil Centru Zonal");
        if (userRepo.existsByEmail(request.getEmail())) {
            LOG.debug("User ul exista");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        }
        User user = new User();
        user.setEmail(request.getEmail());
        user.setName(request.getName());
        user.setPhone(request.getPhone());
        user.setCnp(request.getCnp());
        user.setRoles("RCZ");
        String rawPass = emailPasswordService.generatePassword();
        user.setPassword(passwordEncoder.encode(rawPass));
        User newUser = userRepo.save(user);
        LOG.debug("User: {} created", newUser.getEmail());
        emailPasswordService.sendPassword(user.getEmail(), rawPass);
        return ResponseEntity.status(HttpStatus.OK).body(newUser);
    }

    @PostMapping("/create-secretar")
    @CrossOrigin("*")
    public ResponseEntity<User> createSecretar(@RequestBody SignUpRequest request) {
        LOG.debug("Creating new Secretar");
        if (userRepo.existsByEmail(request.getEmail())) {
            LOG.debug("User ul exista");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        User user = new User();
        user.setEmail(request.getEmail());
        user.setName(request.getName());
        user.setPhone(request.getPhone());
        user.setCnp(request.getCnp());
        user.setRoles("SECRETAR");
        String rawPass = emailPasswordService.generatePassword();
        user.setPassword(passwordEncoder.encode(rawPass));
        User newUser = userRepo.save(user);
        LOG.debug("User: {} created", newUser.getEmail());
        emailPasswordService.sendPassword(user.getEmail(), rawPass);
        return ResponseEntity.status(HttpStatus.OK).body(newUser);
    }

    @PostMapping("/create-candidat")
    @CrossOrigin("*")
    public ResponseEntity<User> createCandidat(@RequestBody SignUpRequest request) {
        LOG.debug("Creating new Candidat");
        if (userRepo.existsByEmail(request.getEmail())) {
            LOG.debug("Candidatul ul exista");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        }
        User user = new User();
        CandidatiInrolati candidat = new CandidatiInrolati();
        user.setEmail(request.getEmail());
        user.setName(request.getName());
        user.setPhone(request.getPhone());
        user.setCnp(request.getCnp());
        user.setRoles("CANDIDAT");
        String rawPass = emailPasswordService.generatePassword();
        user.setPassword(passwordEncoder.encode(rawPass));
        User newUser = userRepo.save(user);
        candidat.setUsers(user);
        candidatRepo.save(candidat);
        LOG.debug("User: {} created", newUser.getEmail());
        emailPasswordService.sendPassword(user.getEmail(), rawPass);
        return ResponseEntity.status(HttpStatus.OK).body(newUser);
    }



}
