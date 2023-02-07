
package aplicatie_admitere.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;



@Entity
@Table
public class OptiuniCandidati {

    @Id
    @SequenceGenerator(
            name="OptiuniCandidati_sequence",
            sequenceName = "OptiuniCandidati_sequence",
            allocationSize = 1
    )

    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;



    private Integer prioritate;

    @ManyToOne
    @JoinColumn(name="candidat_id")
    private CandidatiInrolati candidatiInrolati;

    @ManyToOne
    @JoinColumn(name="specializari_id")
    private Specializari specializari;

    public OptiuniCandidati() {
    }

    public OptiuniCandidati(Long id,  Integer prioritate) {
        this.id = id;

        this.prioritate = prioritate;
    }

    public OptiuniCandidati( Integer prioritate) {
     
        this.prioritate = prioritate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }



    public Integer getPrioritate() {
        return prioritate;
    }

    public void setPrioritate(Integer prioritate) {
        this.prioritate = prioritate;
    }

    public CandidatiInrolati getCandidatiInrolati() {
        return candidatiInrolati;
    }

    public void setCandidatiInrolati(CandidatiInrolati candidatiInrolati) {
        this.candidatiInrolati = candidatiInrolati;
    }

    public Specializari getSpecializari() {
        return specializari;
    }

    public void setSpecializari(Specializari specializari) {
        this.specializari = specializari;
    }
}
