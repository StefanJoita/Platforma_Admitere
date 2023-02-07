package aplicatie_admitere.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.sql.Time;
import java.time.Instant;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Data
/*clasa users, de tip entity, ce va retine detaliile fiecarui user
si le va stoca in tabelul corespunzator in baza de date.
De asemenea pune la dispozitie metodele de tip getter si setter pentru fiecare membru al clasei*/
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user")
    private long id;

    @Email(message = "Email is mandatory")
    private String email;
    private String password;

    @JsonIgnore
    private String roles;

    @JsonIgnore
    private String activationToken;

    @NotBlank(message = "Name is mandatory")
    private String name;

    @NotBlank(message = "Phone is mandatory")
    private String phone;

    private String cnp;

    private String otp;

    private Instant otpTime;

  /*  @ManyToMany(mappedBy = "users")
    private List<Specializari> specializari;*/

    @OneToOne(mappedBy = "users")
    private CandidatiInrolati candidatiInrolatis;



    /*public List<aplicatie_admitere.models.Specializari> getSpecializari() {
        return specializari;
    }

    public void setSpecializari(List<aplicatie_admitere.models.Specializari> specializari) {
        this.specializari = specializari;
    }*/
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public String getActivationToken() {
        return activationToken;
    }

    public void setActivationToken(String activationToken) {
        this.activationToken = activationToken;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCnp() {
        return cnp;
    }

    public void setCnp(String cnp) {
        this.cnp = cnp;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public Instant getOtpTime() {
        return otpTime;
    }

    public void setOtpTime(Instant otpTime) {
        this.otpTime = otpTime;
    }

    public CandidatiInrolati getCandidatiInrolatis() {
        return candidatiInrolatis;
    }

    public void setCandidatiInrolatis(CandidatiInrolati candidatiInrolatis) {
        this.candidatiInrolatis = candidatiInrolatis;
    }


}