package aplicatie_admitere.models;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;


import java.util.List;

@Entity
@Table
public class Specializari {

    @Id
    @SequenceGenerator(
            name="specializari_sequence",
            sequenceName = "specializari_sequence",
            allocationSize = 1
    )

    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long specializare_id;

    //@NotBlank
    private String denumire;

    //@NotBlank
    private Integer nr_locuri;


    @OneToMany(mappedBy = "specializare")
    private List<Ierarhizare>ierarhizares ;

    @OneToMany(mappedBy = "specializari")
    private List<OptiuniCandidati> optiuniCandidatis;

    public Specializari() {
    }

    public Specializari(Long specializare_id, String denumire, Integer nr_locuri, List<User> users, List<Ierarhizare> ierarhizares) {
        this.specializare_id = specializare_id;
        this.denumire = denumire;
        this.nr_locuri = nr_locuri;
      // this.users = users;
        this.ierarhizares = ierarhizares;
    }

    public Specializari(String denumire, Integer nr_locuri, List<User> users, List<Ierarhizare> ierarhizares) {
        this.denumire = denumire;
        this.nr_locuri = nr_locuri;
      //  this.users = users;
        this.ierarhizares = ierarhizares;
    }

    public Long getSpecializare_id() {
        return specializare_id;
    }

    public void setSpecializare_id(Long specializare_id) {
        this.specializare_id = specializare_id;
    }

    public String getDenumire() {
        return denumire;
    }

    public void setDenumire(String denumire) {
        this.denumire = denumire;
    }

    public Integer getNr_locuri() {
        return nr_locuri;
    }

    public void setNr_locuri(Integer nr_locuri) {
        this.nr_locuri = nr_locuri;
    }

   /* public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }*/

    public List<Ierarhizare> getIerarhizares() {
        return ierarhizares;
    }

    public void setIerarhizares(List<Ierarhizare> ierarhizares) {
        this.ierarhizares = ierarhizares;
    }

    public List<OptiuniCandidati> getOptiuniCandidatis() {
        return optiuniCandidatis;
    }

    public void setOptiuniCandidatis(List<OptiuniCandidati> optiuniCandidatis) {
        this.optiuniCandidatis = optiuniCandidatis;
    }
}
