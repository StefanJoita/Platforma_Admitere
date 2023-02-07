package aplicatie_admitere.models;

import java.util.List;

public class IerarhizareDetails {

    private long id_Candidat;

    private float nota;

    private String status;

    private String specializare;

    public IerarhizareDetails(long id_Candidat, float nota, String status, String specializare) {
        this.id_Candidat = id_Candidat;
        this.nota = nota;
        this.status = status;
        this.specializare = specializare;
    }

    public long getId_Candidat() {
        return id_Candidat;
    }

    public void setId_Candidat(long id_Candidat) {
        this.id_Candidat = id_Candidat;
    }

    public float getNota() {
        return nota;
    }

    public void setNota(float nota) {
        this.nota = nota;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSpecializare() {
        return specializare;
    }

    public void setSpecializare(String specializare) {
        this.specializare = specializare;
    }
}
