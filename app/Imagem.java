import java.io.Serializable;

/**
 * Imagem.java
 * Modelo de dados que representa uma imagem do conteúdo.
 *
 * Estrutura esperada no Firebase (dentro do documento do conteúdo):
 *
 * conteudos/
 *   geometria_plana/
 *     titulo: "Geometria Plana"
 *     resumo: "Texto do resumo..."
 *     videoId: "dQw4w9WgXcQ"
 *     imagens: [
 *       { imageUrl: "https://...", legenda: "Figura 1 - Triângulo retângulo" },
 *       { imageUrl: "https://...", legenda: "Figura 2 - Quadrado e diagonal" }
 *     ]
 */
public class Imagem implements Serializable {

    private String imageUrl;
    private String legenda;

    // Construtor vazio necessário para o Firebase desserializar automaticamente
    public Imagem() {}

    public Imagem(String imageUrl, String legenda) {
        this.imageUrl = imageUrl;
        this.legenda  = legenda;
    }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getLegenda() { return legenda; }
    public void setLegenda(String legenda) { this.legenda = legenda; }
}