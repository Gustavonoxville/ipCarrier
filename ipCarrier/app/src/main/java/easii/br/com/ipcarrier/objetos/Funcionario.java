package easii.br.com.ipcarrier.objetos;

/**
 * Created by gustavo on 18/11/2016.
 */
public class Funcionario {
    String nome;
    String telefone;
    double latitude;
    double longitude;
    boolean adm;

    public Funcionario() {
    }

    public Funcionario(String nome, double latitude, String telefone, double longitude, boolean adm) {
        this.nome = nome;
        this.latitude = latitude;
        this.telefone = telefone;
        this.longitude = longitude;
        this.adm = adm;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public boolean isAdm() {
        return adm;
    }

    public void setAdm(boolean adm) {
        this.adm = adm;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
