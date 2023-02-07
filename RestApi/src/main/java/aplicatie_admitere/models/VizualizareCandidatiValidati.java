package aplicatie_admitere.models;

public class VizualizareCandidatiValidati {

    private String email;

    private String cnp;

    private Float medie;

    public VizualizareCandidatiValidati(String email, String cnp, Float medie) {
        this.email = email;
        this.cnp = cnp;
        this.medie = medie;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCnp() {
        return cnp;
    }

    public void setCnp(String cnp) {
        this.cnp = cnp;
    }

    public Float getMedie() {
        return medie;
    }

    public void setMedie(Float medie) {
        this.medie = medie;
    }
}
