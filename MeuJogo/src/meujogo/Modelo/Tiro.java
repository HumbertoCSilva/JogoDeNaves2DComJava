package meujogo.Modelo;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage; // NOVO: Importação para BufferedImage

public class Tiro {

    private Image imagem;
    private int x,y;
    private int largura, altura;
    private boolean isVisivel;

    private static final int LARGURA_TELA = 938;
    private static int VELOCIDADE = 2;

    public Tiro (int x, int y, int larguraDesejada, int alturaDesejada) {
        this.x = x;
        this.y = y;
        isVisivel = true;
        Load(larguraDesejada, alturaDesejada);
    }

    public void Load (int larguraDesejada, int alturaDesejada) {
        ImageIcon referencia = new ImageIcon("res\\Tiros.png");
        Image tempImage = referencia.getImage(); // Imagem original

        // --- CORREÇÃO AQUI: Garante que a imagem está carregada antes de escalar e obter dimensões ---
        BufferedImage bufferedImage = new BufferedImage(
                tempImage.getWidth(null) > 0 ? tempImage.getWidth(null) : 1,
                tempImage.getHeight(null) > 0 ? tempImage.getHeight(null) : 1,
                BufferedImage.TYPE_INT_ARGB
        );
        Graphics g = bufferedImage.createGraphics();
        g.drawImage(tempImage, 0, 0, null);
        g.dispose();
        // FIM CORREÇÃO

        imagem = bufferedImage.getScaledInstance(larguraDesejada, alturaDesejada, Image.SCALE_SMOOTH);
        this.largura = imagem.getWidth(null);
        this.altura = imagem.getHeight(null);
        System.out.println("Tiro: Imagem carregada. Largura=" + largura + ", Altura=" + altura);
    }

    public void Update () {
        this.x += VELOCIDADE;
        if(this.x > LARGURA_TELA){
            isVisivel = false;
        }
    }

    public Rectangle getBounds(){
        return new Rectangle(x,y,largura,altura);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Image getImagem() {
        return imagem;
    }

    public boolean isVisivel() {
        return isVisivel;
    }

    public void setVisivel(boolean visivel) {
        isVisivel = visivel;
    }

    public static int getVELOCIDADE() {
        return VELOCIDADE;
    }

    public static void setVELOCIDADE(int VELOCIDADE) {
        Tiro.VELOCIDADE = VELOCIDADE;
    }
}