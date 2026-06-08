public class Meta {

    String texto;
    boolean concluida;

    public Meta(String texto, boolean concluida) {
        this.texto     = texto;
        this.concluida = concluida;
    }

    public String getTexto()       { return texto; }
    public boolean isConcluida()   { return concluida; }
    public void setTexto(String texto) { this.texto = texto; }
    public void setConcluida(boolean concluida) { this.concluida = concluida; }
}