package sv.edu.catolica.awaminder;


public class Usuario {

    private String nombre;
    private String correo;
    private String contraseña;
    private int peso;
    private int diasSinFallar;

    public Usuario() {
        this.nombre = nombre;
        this.correo = correo;
        this.contraseña = contraseña;
        this.peso = peso;
        this.diasSinFallar = diasSinFallar;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public int getPeso() {
        return peso;
    }

    public void setPeso(int peso) {
        this.peso = peso;
    }

    public int getDiasSinFallar() {
        return diasSinFallar;
    }

    public void setDiasSinFallar(int diasSinFallar) {
        this.diasSinFallar = diasSinFallar;
    }
}

