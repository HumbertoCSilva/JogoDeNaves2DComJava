package meujogo.Modelo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.awt.image.BufferedImage;

public class Player implements ActionListener {

    private int x, y;
    private int dx, dy;
    private Image imagem;
    private int altura, largura;
    private List<Tiro> tiros;
    private boolean isVisivel;
    private boolean isTurbo;
    private Timer turboTimer;

    private final int PLAYER_WIDTH = 90;
    private final int PLAYER_HEIGHT = 90;

    private final int SHOT_WIDTH = 30;
    private final int SHOT_HEIGHT = 10;

    private final int TURBO_DURATION = 5000;

    private final int NORMAL_SPEED = 3;
    private final int TURBO_SPEED_MULTIPLIER = 6;
    private int currentSpeedX;
    private int currentSpeedY;

    // --- NOVO: Variáveis para o sistema de vidas ---
    private int lives; // Número de vidas restantes
    private int health; // Barra de vida atual (quantos danos falta para perder uma vida)
    private final int MAX_HEALTH = 3; // Dano máximo que uma vida suporta
    private final int INITIAL_LIVES = 5; // Vidas iniciais do player
    private final int INITIAL_X = 100; // Posição X inicial do player
    private final int INITIAL_Y = 100; // Posição Y inicial do player
    // --- FIM NOVO ---


    public Player() {
        this.x = INITIAL_X; // Usa a constante
        this.y = INITIAL_Y; // Usa a constante
        isVisivel = true;
        isTurbo = false;
        lives = INITIAL_LIVES; // Inicializa as vidas
        health = MAX_HEALTH; // Inicializa a vida atual (cheia)

        tiros = new ArrayList<>();

        turboTimer = new Timer(TURBO_DURATION, this);
        turboTimer.setRepeats(false);

        Load();
        currentSpeedX = NORMAL_SPEED;
        currentSpeedY = NORMAL_SPEED;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (isTurbo) {
            isTurbo = false;
            Load(); // Retorna à imagem normal
            currentSpeedX = NORMAL_SPEED; // Retorna à velocidade normal
            currentSpeedY = NORMAL_SPEED; // Retorna à velocidade normal
        }
    }

    public void Load() {
        ImageIcon referencia = new ImageIcon("res\\rocket.png");
        Image tempImage = referencia.getImage();

        BufferedImage bufferedImage = new BufferedImage(
                tempImage.getWidth(null) > 0 ? tempImage.getWidth(null) : 1,
                tempImage.getHeight(null) > 0 ? tempImage.getHeight(null) : 1,
                BufferedImage.TYPE_INT_ARGB
        );
        Graphics g = bufferedImage.createGraphics();
        g.drawImage(tempImage, 0, 0, null);
        g.dispose();

        imagem = bufferedImage.getScaledInstance(PLAYER_WIDTH, PLAYER_HEIGHT, Image.SCALE_SMOOTH);
        altura = imagem.getHeight(null);
        largura = imagem.getWidth(null);
        System.out.println("Player: Imagem carregada. Largura=" + largura + ", Altura=" + altura);
    }

    public void Update() {
        x += dx * currentSpeedX;
        y += dy * currentSpeedY;

        if (x < 0) x = 0;
        if (x > 1024 - largura) x = 1024 - largura;
        if (y < 0) y = 0;
        if (y > 768 - altura) y = 768 - altura;
    }

    public void TiroSimples() {
        this.tiros.add(new Tiro(x + largura, y + (altura / 2), SHOT_WIDTH, SHOT_HEIGHT));
    }

    public void Turbo() {
        if (!isTurbo) {
            isTurbo = true;
            ImageIcon referencia = new ImageIcon("res\\naveTurbo.png");
            Image tempImage = referencia.getImage();

            BufferedImage bufferedImage = new BufferedImage(
                    tempImage.getWidth(null) > 0 ? tempImage.getWidth(null) : 1,
                    tempImage.getHeight(null) > 0 ? tempImage.getHeight(null) : 1,
                    BufferedImage.TYPE_INT_ARGB
            );
            Graphics g = bufferedImage.createGraphics();
            g.drawImage(tempImage, 0, 0, null);
            g.dispose();

            imagem = bufferedImage.getScaledInstance(PLAYER_WIDTH, PLAYER_HEIGHT, Image.SCALE_SMOOTH);
            altura = imagem.getHeight(null);
            largura = imagem.getWidth(null);
            System.out.println("Player Turbo: Imagem carregada. Largura=" + largura + ", Altura=" + altura);
            turboTimer.restart();
        }
    }

    // --- NOVO: Método para o player receber dano ---
    public void takeDamage() {
        if (!isTurbo) { // Player só toma dano se NÃO estiver no modo turbo
            health--;
            System.out.println("Player: Dano recebido! Vida atual: " + health + "/" + MAX_HEALTH + ", Vidas restantes: " + lives);

            if (health <= 0) { // Se a vida atual de uma "barra" acabou
                lives--; // Perde uma vida
                System.out.println("Player: Perdeu uma vida! Vidas restantes: " + lives);

                if (lives <= 0) { // Se não há mais vidas
                    isVisivel = false; // Player se torna invisível (fim de jogo)
                    System.out.println("Player: GAME OVER! Nenhuma vida restante.");
                } else {
                    health = MAX_HEALTH; // Reseta a vida para a próxima barra
                    resetPosition(); // Move o player de volta para a posição inicial
                }
            }
        }
    }

    // --- NOVO: Método para resetar a posição do player ---
    public void resetPosition() {
        this.x = INITIAL_X;
        this.y = INITIAL_Y;
        // Opcional: Adicionar um curto período de invencibilidade após o respawn aqui.
        // Por exemplo, ligar um timer curto que desativa o turbo e o deixa invencível por 1-2 segundos.
    }
    // --- FIM NOVO ---


    public Rectangle getBounds() {
        return new Rectangle(x, y, largura, altura);
    }

    public void KeyPressed(KeyEvent tecla) {
        int codigo = tecla.getKeyCode();

        if (codigo == KeyEvent.VK_SPACE) {
            Turbo();
        }

        if (codigo == KeyEvent.VK_A) {
            if (!isTurbo) { // Permite tiro apenas se NÃO estiver em modo turbo
                TiroSimples();
            }
        }

        if (codigo == KeyEvent.VK_UP) { dy = -1; }
        if (codigo == KeyEvent.VK_DOWN) { dy = 1; }
        if (codigo == KeyEvent.VK_LEFT) { dx = -1; }
        if (codigo == KeyEvent.VK_RIGHT) { dx = 1; }
    }

    public void KeyRelease(KeyEvent tecla) {
        int codigo = tecla.getKeyCode();

        if (codigo == KeyEvent.VK_UP) { dy = 0; }
        if (codigo == KeyEvent.VK_DOWN) { dy = 0; }
        if (codigo == KeyEvent.VK_LEFT) { dx = 0; }
        if (codigo == KeyEvent.VK_RIGHT) { dx = 0; }
    }

    public boolean isTurbo() { return isTurbo; }
    public void setVisivel(boolean visivel) { isVisivel = visivel; }
    public boolean isVisivel() { return isVisivel; }
    public int getX() { return x; }
    public int getY() { return y; }
    public Image getImage() { return imagem; }
    public List<Tiro> getTiros() { return tiros; }

    // --- NOVO: Getters para as vidas e a saúde ---
    public int getLives() { return lives; }
    public int getHealth() { return health; }
    public int getMaxHealth() { return MAX_HEALTH; } // Retorna a vida máxima por barra
    // --- FIM NOVO ---
}