package aplicatie_admitere.models;

import java.util.List;

public class CandidatiInrolatiDetails {
    private String email;
    private String documente;

    public CandidatiInrolatiDetails(String email, String documente) {
        this.email = email;
        this.documente = documente;
    }



    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDocumente() {
        return documente;
    }

    public void setDocumente(String documente) {
        this.documente = documente;
    }
}
