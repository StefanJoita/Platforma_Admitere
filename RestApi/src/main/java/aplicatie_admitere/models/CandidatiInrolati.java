package aplicatie_admitere.models;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.validator.constraints.Length;

import java.util.List;


@Entity
@Table
public class CandidatiInrolati {

    @Id
    @SequenceGenerator(
            name="CandidatiInrolati_sequence",
            sequenceName = "CandidatiInrolati_sequence",
            allocationSize = 1
    )

    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(length = 1000)
    private String documente;
    private String validare;

    @OneToOne
    @JoinColumn(name="candidatinrolat_id")
    private User users;

    @OneToMany(mappedBy = "candidatiInrolati")
    private List<OptiuniCandidati> optiuniCandidatis;

    @OneToMany(mappedBy = "candidatiInrolati")
    private List<Ierarhizare> ierarhizares;
    public CandidatiInrolati() {
    }

    public CandidatiInrolati(Long id, String documente, String validare, User users) {
        this.id = id;
        this.documente = documente;
        this.validare = validare;
        this.users = users;
    }

    public CandidatiInrolati(String documente, String validare, User users) {
        this.documente = documente;
        this.validare = validare;
        this.users = users;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDocumente() {
        return documente;
    }

    public void setDocumente(String documente) {
        this.documente = documente;
    }

    public String getValidare() {
        return validare;
    }

    public void setValidare(String validare) {
        this.validare = validare;
    }

    public User getUsers() {
        return users;
    }

    public void setUsers(User users) {
        this.users = users;
    }

    public List<OptiuniCandidati> getOptiuniCandidatis() {
        return optiuniCandidatis;
    }

    public void setOptiuniCandidatis(List<OptiuniCandidati> optiuniCandidatis) {
        this.optiuniCandidatis = optiuniCandidatis;
    }

    public List<Ierarhizare> getIerarhizares() {
        return ierarhizares;
    }

    public void setIerarhizares(List<Ierarhizare> ierarhizares) {
        this.ierarhizares = ierarhizares;
    }
}
