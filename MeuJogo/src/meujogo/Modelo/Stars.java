package meujogo.Modelo;

import javax.swing.*;
import java.awt.*;
import java.util.Random;
import java.awt.image.BufferedImage; // NOVO: Importação para BufferedImage

public class Stars {

    private Image imagem;
    private int x,y;
    private int largura, altura;
    private boolean isVisivel;

    private final int STAR_WIDTH = 5;
    private final int STAR_HEIGHT = 5;

    private static int VELOCIDADE = 2;

    public Stars (int x, int y) {
        this.x = x;
        this.y = y;
        isVisivel = true;
        Load();
    }

    public void Load () {
        ImageIcon referencia = new ImageIcon("res\\stars.png");
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

        imagem = bufferedImage.getScaledInstance(STAR_WIDTH, STAR_HEIGHT, Image.SCALE_SMOOTH);
        this.largura = imagem.getWidth(null);
        this.altura = imagem.getHeight(null);
        System.out.println("Stars: Imagem carregada. Largura=" + largura + ", Altura=" + altura);
    }

    public void Update () {
        if (this.x < 0) {
            Random a = new Random();
            int m = a.nextInt(500);
            this.x = m + 1024;
            Random r = new Random();
            int n = r.nextInt(768);
            this.y = n;
        }else {
            this.x -= VELOCIDADE;
        }
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

    public static void setVELOCIDADE(int velocidade) {
        VELOCIDADE = velocidade;
    }
}