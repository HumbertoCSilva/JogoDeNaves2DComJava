package meujogo.Modelo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class Fase extends JPanel implements ActionListener {

    private Image backGround;
    private Player player;
    private Timer timer;
    private List<Enemy1> enemy1;
    private List<Stars> stars;

    // --- NOVO: Enum para os estados do jogo ---
    public enum GameState {
        PLAYING,    // Jogo em andamento
        GAME_OVER,  // Jogo perdido
        GAME_WON    // Jogo vencido
    }
    private GameState currentGameState; // Variável que guarda o estado atual do jogo
    // --- FIM NOVO ---

    private Image fimJogoImage; // Imagem de fundo para game over/win screen
    private String gameOverText = "Tente novamente";
    private String winText = "VOCÊ VENCEU! BEM-VINDO À TERRA."; // Frase para a vitória
    private JButton restartButton;

    // --- Variáveis de controle de velocidade da fase ---
    private float gameSpeedMultiplier = 1.0f;
    private final float TURBO_GAME_SPEED_FACTOR = 1.5f;
    private final int NORMAL_ENEMY_STAR_SHOT_SPEED = 2; // Velocidade base de inimigos/estrelas/tiros

    // --- Variáveis para o aumento progressivo de velocidade por inimigos ---
    private int enemiesDestroyedSinceLastSpeedIncrease;
    private float baseGameSpeedMultiplier;
    private final int ENEMIES_PER_SPEED_UP = 5;
    private final float SPEED_INCREASE_FACTOR = 2.0f;

    // --- NOVO: Variáveis para a condição de vitória e extensão da fase ---
    private final long PHASE_LENGTH_DISTANCE = 15000; // DISTÂNCIA TOTAL DA FASE (configurável)
    // Player vence quando essa distância é "percorrida".
    // Ajuste este valor para mudar a duração da fase.
    private long distanceTraveled; // Distância percorrida nesta fase
    // --- FIM NOVO ---


    public Fase () {
        setFocusable(true);
        setDoubleBuffered(true);
        setLayout(null);

        System.out.println("Fase: Construtor chamado. Configurando.");

        ImageIcon referencia = new ImageIcon("res\\space_background_blue_73340_1024x768.jpg");
        backGround = referencia.getImage();
        if (backGround == null) { System.err.println("Fase: ERRO ao carregar background. Verifique o caminho da imagem!"); }

        player = new Player();
        if (player.getImage() == null) { System.err.println("Fase: ERRO ao carregar imagem do player. Verifique o caminho da imagem!"); }

        addKeyListener(new TecladoAdapter());
        System.out.println("Fase: KeyListener adicionado.");

        InicializaInimigos();
        System.out.println("Fase: " + enemy1.size() + " inimigos inicializados.");
        InicializaStars();
        System.out.println("Fase: " + stars.size() + " estrelas inicializadas.");

        ImageIcon fimJogoRef = new ImageIcon("res\\GameOver.jpg");
        fimJogoImage = fimJogoRef.getImage();
        if (fimJogoImage == null) { System.err.println("Fase: ERRO ao carregar imagem de Fim de Jogo. Verifique o caminho 'res\\GameOver.jpg'!"); }

        restartButton = new JButton("Reiniciar");
        restartButton.setBounds(0, 0, 100, 30);
        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reiniciarJogo();
            }
        });
        restartButton.setVisible(false);
        add(restartButton);

        enemiesDestroyedSinceLastSpeedIncrease = 0;
        baseGameSpeedMultiplier = 1.0f;

        // --- NOVO: Inicializa o estado do jogo e a distância percorrida ---
        currentGameState = GameState.PLAYING; // Começa no estado de jogo
        distanceTraveled = 0; // Distância inicial
        // --- FIM NOVO ---

        System.out.println("Fase: Estado inicial: " + currentGameState + ", Distância: " + distanceTraveled);

        timer = new Timer(5, this);
        timer.start();
        System.out.println("Fase: Timer iniciado com delay de 5ms.");
    }

    public void setGameOverText(String text) {
        this.gameOverText = text;
    }

    public void setWinText(String text) { // NOVO: Setter para a frase de vitória
        this.winText = text;
    }

    public void InicializaInimigos () {
        int quantidadeInimigos = 40;
        enemy1 = new ArrayList<>();

        for (int i = 0; i < quantidadeInimigos; i++) {
            int x = (int) (Math.random() * 8000 + 1024);
            int y = (int) (Math.random() * 650 + 30);
            Enemy1 newEnemy = new Enemy1(x, y);
            enemy1.add(newEnemy);
            if (newEnemy.getImagem() == null) { System.err.println("Fase: ERRO ao carregar imagem do inimigo. Verifique o caminho 'res\\enemy.png'!"); }
        }
    }

    public void InicializaStars () {
        int numStars = 100;
        stars = new ArrayList<>();

        for (int i = 0; i < numStars; i++) {
            int x = (int) (Math.random() * 1024);
            int y = (int) (Math.random() * 768);
            Stars newStar = new Stars(x, y);
            stars.add(newStar);
            if (newStar.getImagem() == null) { System.err.println("Fase: ERRO ao carregar imagem da estrela. Verifique o caminho 'res\\stars.png'!"); }
        }
    }

    public void reiniciarJogo() {
        System.out.println("Fase: Reiniciando o jogo...");
        currentGameState = GameState.PLAYING; // Volta para o estado de jogo
        player = new Player();
        InicializaInimigos();
        InicializaStars();
        restartButton.setVisible(false);
        player.getTiros().clear();

        enemiesDestroyedSinceLastSpeedIncrease = 0;
        baseGameSpeedMultiplier = 1.0f;
        distanceTraveled = 0; // NOVO: Reseta a distância ao reiniciar

        requestFocusInWindow();
        timer.start();
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;

        // Desenha os elementos do jogo apenas se estiver no estado PLAYING
        if (currentGameState == GameState.PLAYING) {
            g2d.drawImage(backGround, 0, 0, null);

            if (player.isVisivel()) {
                g2d.drawImage(player.getImage(), player.getX(), player.getY(), this);
                g2d.setColor(Color.GREEN);
                g2d.drawRect(player.getX(), player.getY(), player.getBounds().width, player.getBounds().height);
            }

            for(Stars s : stars) {
                if (s.isVisivel()) { g2d.drawImage(s.getImagem(), s.getX(), s.getY(), this); }
            }

            List<Tiro> tiros = player.getTiros();
            for(Tiro t : tiros) {
                if (t.isVisivel()) {
                    g2d.drawImage(t.getImagem(), t.getX(), t.getY(), this);
                    g2d.setColor(Color.CYAN);
                    g2d.drawRect(t.getX(), t.getY(), t.getBounds().width, t.getBounds().height);
                }
            }

            for(Enemy1 e : enemy1) {
                if (e.isVisivel()) {
                    g2d.drawImage(e.getImagem(), e.getX(), e.getY(), this);
                    g2d.setColor(Color.RED);
                    g2d.drawRect(e.getX(), e.getY(), e.getBounds().width, e.getBounds().height);
                }
            }

            // Desenha a UI de vidas e barra de vida
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 16));
            g2d.drawString("Vidas: " + player.getLives(), 10, 20);
            g2d.drawString("Distância: " + distanceTraveled, getWidth() - 150, 20); // NOVO: Mostra a distância

            int barWidth = player.getMaxHealth() * 30;
            int barHeight = 15;
            int barX = 10;
            int barY = 30;

            g2d.setColor(Color.GRAY);
            g2d.fillRect(barX, barY, barWidth, barHeight);

            g2d.setColor(Color.GREEN);
            int currentHealthBarWidth = player.getHealth() * (barWidth / player.getMaxHealth());
            g2d.fillRect(barX, barY, currentHealthBarWidth, barHeight);

            g2d.setColor(Color.WHITE);
            g2d.drawRect(barX, barY, barWidth, barHeight);

        } else { // Se o jogo não está PLAYING (Game Over ou Game Won)
            g2d.drawImage(fimJogoImage, 0, 0, getWidth(), getHeight(), null);

            String displayMessage;
            if (currentGameState == GameState.GAME_WON) {
                displayMessage = winText;
                System.out.println("Fase: Exibindo tela de VITÓRIA.");
            } else { // GameState.GAME_OVER
                displayMessage = gameOverText;
                System.out.println("Fase: Exibindo tela de GAME OVER.");
            }

            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 24));
            FontMetrics fm = g2d.getFontMetrics();
            int textWidth = fm.stringWidth(displayMessage); // Usa a mensagem a ser exibida
            int textHeight = fm.getHeight();

            int buttonWidth = restartButton.getWidth();
            int buttonHeight = restartButton.getHeight();
            int buttonMarginBottom = 70;
            int buttonX = (getWidth() - buttonWidth) / 2;
            int buttonY = getHeight() - buttonMarginBottom - buttonHeight;

            int textMarginAboveButton = 20;
            int textX = (getWidth() - textWidth) / 2;
            int textY = buttonY - textMarginAboveButton;
            textY = textY + fm.getDescent();

            g2d.drawString(displayMessage, textX, textY); // Desenha a mensagem correta

            restartButton.setBounds(buttonX, buttonY, buttonWidth, buttonHeight);
            restartButton.setVisible(true);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("Fase: actionPerformed() chamado. Estado: " + currentGameState + ". Foco: " + isFocusOwner());

        // Atualiza apenas se estiver no estado PLAYING
        if (currentGameState != GameState.PLAYING) {
            timer.stop();
            System.out.println("Fase: Timer parado. Estado final: " + currentGameState);
            return;
        }

        // --- NOVO: Atualiza a distância percorrida ---
        // Aumenta a distância baseada na velocidade dos inimigos (que é o que simula o "movimento do mundo")
        distanceTraveled += (int)(NORMAL_ENEMY_STAR_SHOT_SPEED * gameSpeedMultiplier);
        // --- FIM NOVO ---

        // --- NOVO: Verifica condição de vitória ---
        if (distanceTraveled >= PHASE_LENGTH_DISTANCE) {
            currentGameState = GameState.GAME_WON; // Define o estado de vitória
            System.out.println("Fase: PLAYER VENCEU! Distância percorrida: " + distanceTraveled);
            // O timer será parado na próxima chamada a actionPerformed
            repaint(); // Força a repintura da tela de vitória
            return; // Sai do update para não processar mais nada
        }
        // --- FIM NOVO ---

        // Lógica de velocidade do jogo
        if (player.isTurbo()) { gameSpeedMultiplier = baseGameSpeedMultiplier * TURBO_GAME_SPEED_FACTOR; }
        else { gameSpeedMultiplier = baseGameSpeedMultiplier; }

        Stars.setVELOCIDADE((int)(NORMAL_ENEMY_STAR_SHOT_SPEED * gameSpeedMultiplier));
        Enemy1.setVELOCIDADE((int)(NORMAL_ENEMY_STAR_SHOT_SPEED * gameSpeedMultiplier));
        Tiro.setVELOCIDADE((int)(NORMAL_ENEMY_STAR_SHOT_SPEED * gameSpeedMultiplier));

        player.Update();

        for (int i = 0; i < stars.size(); i++) {
            Stars s = stars.get(i);
            if (s.isVisivel()) { s.Update(); }
            else { stars.remove(i); i--; }
        }

        List<Tiro> tiros = player.getTiros();
        for (int i = 0; i < tiros.size(); i++) {
            Tiro t = tiros.get(i);
            if (t.isVisivel()) { t.Update(); }
            else { tiros.remove(i); i--; }
        }

        for (int i = 0; i < enemy1.size(); i++) {
            Enemy1 e_obj = enemy1.get(i);
            if (e_obj.isVisivel()) { e_obj.Update(); }
            else {
                enemy1.remove(i);
                i--;
                int x = (int) (Math.random() * 8000 + 1024);
                int y = (int) (Math.random() * 650 + 30);
                enemy1.add(new Enemy1(x, y));
                System.out.println("Fase: Inimigo removido e novo inimigo re-spawned. Total de inimigos: " + enemy1.size());
            }
        }
        ChecarColisoes();

        repaint();
    }

    public void ChecarColisoes () {
        Rectangle formaNave = player.getBounds();
        Rectangle formaEnemy1;
        Rectangle formaTiro;

        // Colisão Player com Inimigo
        for(int i = 0; i < enemy1.size(); i++) {
            Enemy1 tempEnemy1 = enemy1.get(i);
            if (tempEnemy1.isVisivel()) {
                formaEnemy1 = tempEnemy1.getBounds();
                System.out.println("DEBUG P-E: Player(" + formaNave.x + "," + formaNave.y + "," + formaNave.width + "," + formaNave.height + ") vs Enemy(" + formaEnemy1.x + "," + formaEnemy1.y + "," + formaEnemy1.width + "," + formaEnemy1.height + ")");
                if(formaNave.intersects(formaEnemy1)){
                    System.out.println("Fase: COLISÃO DETECTADA: Player e Inimigo! Player Turbo: " + player.isTurbo());
                    if (player.isTurbo()) {
                        tempEnemy1.setVisivel(false);
                        System.out.println("Fase: Inimigo destruído pelo Player em modo Turbo!");
                        enemiesDestroyedSinceLastSpeedIncrease++;
                        System.out.println("Fase: Contador de inimigos: " + enemiesDestroyedSinceLastSpeedIncrease);
                        if (enemiesDestroyedSinceLastSpeedIncrease >= ENEMIES_PER_SPEED_UP) {
                            baseGameSpeedMultiplier *= SPEED_INCREASE_FACTOR;
                            enemiesDestroyedSinceLastSpeedIncrease = 0;
                            System.out.println("Fase: Velocidade do jogo DOBRADA! Nova base: " + baseGameSpeedMultiplier);
                        }
                    } else {
                        player.takeDamage();
                        tempEnemy1.setVisivel(false);
                        System.out.println("Fase: Player sofreu dano!");
                        enemiesDestroyedSinceLastSpeedIncrease++;
                        System.out.println("Fase: Contador de inimigos: " + enemiesDestroyedSinceLastSpeedIncrease);
                        if (enemiesDestroyedSinceLastSpeedIncrease >= ENEMIES_PER_SPEED_UP) {
                            baseGameSpeedMultiplier *= SPEED_INCREASE_FACTOR;
                            enemiesDestroyedSinceLastSpeedIncrease = 0;
                            System.out.println("Fase: Velocidade do jogo DOBRADA! Nova base: " + baseGameSpeedMultiplier);
                        }

                        if (!player.isVisivel()) { // Se o player ficou invisível (sem vidas)
                            currentGameState = GameState.GAME_OVER; // Define o estado de Game Over
                            System.out.println("Fase: GAME OVER! Player sem vidas.");
                            // O timer será parado na próxima chamada a actionPerformed
                        }
                    }
                }
            }
        }

        // Colisão Tiro com Inimigo
        List<Tiro> tiros = player.getTiros();
        for(int j = 0; j < tiros.size(); j++) {
            Tiro tempoTiro = tiros.get(j);
            if (tempoTiro.isVisivel()) {
                formaTiro = tempoTiro.getBounds();
                for (int o = 0; o < enemy1.size();o++) {
                    Enemy1 tempEnemy1 = enemy1.get(o);
                    if (tempEnemy1.isVisivel()) {
                        formaEnemy1 = tempEnemy1.getBounds();
                        System.out.println("DEBUG T-E: Tiro(" + formaTiro.x + "," + formaTiro.y + "," + formaTiro.width + "," + formaTiro.height + ") vs Enemy(" + formaEnemy1.x + "," + formaEnemy1.y + "," + formaEnemy1.width + "," + formaEnemy1.height + ")");
                        if(formaTiro.intersects(formaEnemy1)) {
                            System.out.println("Fase: COLISÃO DETECTADA: Tiro e Inimigo!");
                            tempEnemy1.setVisivel(false);
                            tempoTiro.setVisivel(false);
                            enemiesDestroyedSinceLastSpeedIncrease++;
                            System.out.println("Fase: Contador de inimigos: " + enemiesDestroyedSinceLastSpeedIncrease);
                            if (enemiesDestroyedSinceLastSpeedIncrease >= ENEMIES_PER_SPEED_UP) {
                                baseGameSpeedMultiplier *= SPEED_INCREASE_FACTOR;
                                enemiesDestroyedSinceLastSpeedIncrease = 0;
                                System.out.println("Fase: Velocidade do jogo DOBRADA! Nova base: " + baseGameSpeedMultiplier);
                            }
                            break;
                        }
                    }
                }
            }
        }
    }

    private class TecladoAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            System.out.println("Fase: KeyPressed() chamado. Codigo: " + e.getKeyCode() + ". Foco: " + isFocusOwner());
            // Processa input APENAS se o jogo está no estado PLAYING
            if (currentGameState == GameState.PLAYING) { player.KeyPressed(e); }
        }
        @Override
        public void keyReleased(KeyEvent e) {
            System.out.println("Fase: KeyReleased() chamado. Codigo: " + e.getKeyCode() + ". Foco: " + isFocusOwner());
            // Processa input APENAS se o jogo está no estado PLAYING
            if (currentGameState == GameState.PLAYING) { player.KeyRelease(e); }
        }
    }
}