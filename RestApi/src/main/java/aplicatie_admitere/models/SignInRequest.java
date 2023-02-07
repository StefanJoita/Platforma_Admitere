package aplicatie_admitere.models;
/*clasa de tip model, va retine emailul, parola userului pentru care se va elibera token-ul jwt
* ofera de asemenea metode de tip getter si setter pentru fiecare membru*/
public class SignInRequest {
    private String email;
    private String password;


    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
