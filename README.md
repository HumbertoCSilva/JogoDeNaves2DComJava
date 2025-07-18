Meu Jogo: Um Simples Jogo de Tiro Espacial 2D
"Meu Jogo" é um clássico jogo de tiro espacial 2D desenvolvido em Java Swing. Pilote sua nave, destrua inimigos que se aproximam e sobreviva por uma distância definida para alcançar a vitória! Fique atento às colisões e gerencie a vida e as energias da sua nave.

Sumário
Funcionalidades

Como Executar

Pré-requisitos

Estrutura do Projeto

Executando a partir do Código-Fonte

Compilando e Executando um JAR (Opcional)

Instruções de Jogo

Processo de Desenvolvimento e Funcionalidades

Loop Principal do Jogo (Fase.java)

Nave do Jogador (Player.java)

Inimigos (Enemy1.java)

Projéteis (Tiro.java)

Elementos de Fundo (Stars.java)

Detecção de Colisões

Estados do Jogo (PLAYING, GAME_OVER, GAME_WON)

Dificuldade Dinâmica (Aumento de Velocidade)

Saúde e Vidas do Jogador

Modo Turbo

Condição de Vitória (Distância da Fase)

Interface do Usuário (UI)

Manutenção e Melhorias Futuras

Adicionando Novos Assets

Balanceando a Jogabilidade

Correção de Bugs

Otimização de Desempenho

Novas Funcionalidades a Considerar



Funcionalidades
Controle do Jogador: Navegue sua nave espacial com as setas direcionais.

Tiro: Atire nos inimigos pressionando a tecla 'A'.

Modo Turbo: Ative um breve modo turbo para acelerar e ficar invencível por um tempo, usando a tecla 'ESPAÇO'.

Inimigos: Inimigos gerados aleatoriamente se movem em sua direção.

Sistema de Vidas e Saúde: O jogador possui múltiplas vidas, e cada vida tem uma barra de saúde que se esgota com colisões.

Dificuldade Progressiva: O jogo fica gradualmente mais rápido à medida que você destrói inimigos.

Condição de Vitória: Avance uma distância predefinida para vencer o jogo e ser "bem-vindo à Terra".

Tela de Game Over/Vitória: Exibe uma tela clara quando o jogo termina, com a opção de reiniciar.

Elementos de Fundo: Estrelas que se movem criam a sensação de movimento no espaço.

Como Executar
Pré-requisitos
Java Development Kit (JDK) 8 ou superior: Tenha certeza de que o JDK está instalado e configurado em sua máquina. Você pode baixá-lo no site oficial da Oracle ou usar um gerenciador de pacotes (ex: OpenJDK).

Estrutura do Projeto
O projeto segue uma estrutura simples. Verifique se seus arquivos estão organizados da seguinte forma:

MeuJogo/
├── src/
│   ├── meujogo/
│   │   └── Container.java
│   └── meujogo/Modelo/
│       ├── Enemy1.java
│       ├── Fase.java
│       ├── Player.java
│       ├── Stars.java
│       └── Tiro.java
└── res/
    ├── enemy.png
    ├── GameOver.jpg
    ├── naveTurbo.png
    ├── rocket.png
    ├── space_background_blue_73340_1024x768.jpg
    ├── stars.png
    └── Tiros.png
Importante: As imagens na pasta res/ são essenciais para o funcionamento do jogo. Sem elas, você verá erros de carregamento e elementos visuais ausentes.

Executando a partir do Código-Fonte
Navegue até o diretório raiz do projeto: Abra seu terminal ou prompt de comando e vá para a pasta MeuJogo (onde src e res estão localizados).

Bash

cd /caminho/para/MeuJogo
Compile os arquivos Java:

Bash

javac src/meujogo/Container.java src/meujogo/Modelo/*.java
Se você estiver usando uma IDE (como IntelliJ IDEA, Eclipse, NetBeans), ela geralmente cuida da compilação automaticamente.

Execute o jogo:

Bash

java -cp src meujogo.Container
A opção -cp src é crucial para que o Java encontre suas classes no diretório src.

Uma janela do jogo deve aparecer.

Compilando e Executando um JAR (Opcional)
Para criar um arquivo JAR executável, o que facilita a distribuição do jogo:

Navegue até o diretório raiz do projeto:

Bash

cd /caminho/para/MeuJogo
Compile os arquivos Java (se ainda não o fez):

Bash

javac src/meujogo/Container.java src/meujogo/Modelo/*.java
Crie o arquivo JAR:
Você precisa incluir as classes compiladas e os recursos (pasta res) no JAR. Isso geralmente é feito criando um arquivo manifest.mf e usando o comando jar.

Crie um arquivo chamado manifest.mf dentro do diretório MeuJogo com o seguinte conteúdo:

Main-Class: meujogo.Container
Class-Path: .
(Certifique-se de que há uma linha vazia no final do arquivo manifest.)

Agora, crie o JAR:

Bash

jar -cvfm MeuJogo.jar manifest.mf -C src . -C res .
MeuJogo.jar: Nome do arquivo JAR de saída.

manifest.jar: O arquivo manifest que acabamos de criar.

-C src .: Inclui todo o conteúdo do diretório src (suas classes compiladas).

-C res .: Inclui todo o conteúdo do diretório res (suas imagens).

Execute o JAR:

Bash

java -jar MeuJogo.jar
Instruções de Jogo
Movimento: Use as setas direcionais (cima, baixo, esquerda, direita) para mover sua nave espacial.

Tiro: Pressione a tecla 'A' para disparar projéteis contra os inimigos.

Turbo: Pressione a tecla 'ESPAÇO' para ativar o modo turbo. Neste modo, sua nave se move mais rápido e fica temporariamente invencível a colisões com inimigos.

Saúde: Fique de olho na barra de saúde na parte inferior esquerda. Cada colisão com um inimigo (fora do modo turbo) reduz sua saúde.

Vidas: Se sua saúde chegar a zero, você perde uma vida e sua nave é reiniciada. Se todas as vidas acabarem, é GAME OVER.

Objetivo: Sobreviva, destrua inimigos e viaje pela distância total da fase para alcançar a vitória! A distância percorrida é mostrada no canto superior direito.

Reiniciar: Após um Game Over ou uma vitória, um botão "Reiniciar" aparecerá na tela. Clique nele para começar um novo jogo.

Processo de Desenvolvimento e Funcionalidades
O jogo é construído usando componentes básicos do Java Swing e o padrão de design Model-View-Controller (MVC) de forma implícita, com as classes Modelo representando os elementos do jogo e Fase atuando como o controlador e a visualização principal.

Loop Principal do Jogo (Fase.java)
A classe Fase é o coração do jogo. Ela estende JPanel e implementa ActionListener para o Timer.

O Timer (com delay de 5ms) aciona o método actionPerformed, que é o game loop.

Dentro do actionPerformed, todos os elementos do jogo (Player, Enemy1, Stars, Tiro) têm seus métodos Update() chamados para atualizar suas posições e estados.

A cada tick do timer, o método repaint() é chamado para redesenhar todos os elementos na tela.

A distância percorrida (distanceTraveled) é atualizada aqui, simulando o progresso através da fase.

Nave do Jogador (Player.java)
Representa a nave controlada pelo jogador.

Gerencia a posição (x, y) e a velocidade (dx, dy).

Contém uma lista de objetos Tiro para os projéteis disparados.

Implementa o sistema de vidas (lives) e saúde (health) com um limite máximo de saúde por vida.

Possui um método takeDamage() que reduz a saúde e, se necessário, uma vida, reiniciando a posição do jogador.

Os métodos KeyPressed e KeyRelease respondem às entradas do teclado para movimento, tiro e ativação do turbo.

Carrega duas imagens: uma para o estado normal (rocket.png) e outra para o modo turbo (naveTurbo.png).

Inimigos (Enemy1.java)
Representa os inimigos que o jogador deve destruir.

Move-se horizontalmente em direção ao jogador.

É reinicializado com uma nova posição fora da tela quando se torna invisível (destruído ou fora da tela).

A imagem é escalada para um tamanho padrão (ENEMY_WIDTH, ENEMY_HEIGHT).

A velocidade dos inimigos é controlada globalmente através de um atributo estático VELOCIDADE, que é ajustado pela Fase.

Projéteis (Tiro.java)
Representa os tiros disparados pela nave do jogador.

Move-se horizontalmente para a direita.

Torna-se invisível quando sai da tela ou colide com um inimigo.

A imagem é escalada dinamicamente com base nos parâmetros passados (larguraDesejada, alturaDesejada).

A velocidade dos tiros também é controlada globalmente pela Fase.

Elementos de Fundo (Stars.java)
Representa as estrelas no fundo que se movem para a esquerda, dando a sensação de movimento.

Quando uma estrela sai da tela, ela é reposicionada aleatoriamente na borda direita para criar um efeito contínuo de rolagem.

A imagem da estrela também é escalada para um tamanho pequeno.

Sua velocidade é controlada globalmente pela Fase.

Detecção de Colisões
O método ChecarColisoes() na classe Fase é responsável por verificar interações entre diferentes objetos do jogo.

Utiliza a classe java.awt.Rectangle e o método intersects() para detectar colisões entre os retângulos de delimitação dos objetos.

Colisão Jogador-Inimigo: Se o jogador colidir com um inimigo:

Se o jogador estiver no modo turbo, o inimigo é destruído e contribui para o aumento de velocidade.

Caso contrário, o jogador sofre dano (player.takeDamage()) e o inimigo é destruído.

Colisão Tiro-Inimigo: Se um tiro colidir com um inimigo, ambos são destruídos e o contador para o aumento de velocidade é incrementado.

Mensagens de System.out.println são usadas para depuração de colisões.

Estados do Jogo (PLAYING, GAME_OVER, GAME_WON)
A classe Fase introduz um enum GameState para gerenciar o fluxo do jogo:

PLAYING: O jogo está em andamento.

GAME_OVER: O jogador perdeu.

GAME_WON: O jogador venceu.

O game loop (actionPerformed) e o método de desenho (paint) respondem ao currentGameState, exibindo a tela de jogo ou a tela de fim/vitória conforme apropriado.

Quando o jogo não está PLAYING, o timer é parado e o botão de reiniciar aparece.

Dificuldade Dinâmica (Aumento de Velocidade)
Variáveis como enemiesDestroyedSinceLastSpeedIncrease, ENEMIES_PER_SPEED_UP e SPEED_INCREASE_FACTOR na Fase controlam um sistema de dificuldade progressiva.

A cada ENEMIES_PER_SPEED_UP inimigos destruídos, a baseGameSpeedMultiplier é dobrada, aumentando a velocidade de todos os inimigos, estrelas e tiros. Isso torna o jogo mais desafiador à medida que o jogador progride.

Saúde e Vidas do Jogador
A classe Player possui atributos lives (número de vidas restantes) e health (saúde atual dentro de uma vida).

MAX_HEALTH define quantos "pontos de dano" uma vida pode suportar.

Quando a saúde chega a zero, uma vida é perdida, a saúde é redefinida e o jogador é reposicionado.

Se as vidas chegam a zero, a variável isVisivel do player se torna false, e o estado do jogo muda para GAME_OVER.

Modo Turbo
Ativado pela tecla ESPAÇO.

Muda a imagem da nave para naveTurbo.png.

Aumenta a velocidade do player (TURBO_SPEED_MULTIPLIER) e a velocidade geral do jogo (TURBO_GAME_SPEED_FACTOR).

Torna o player invencível a colisões com inimigos enquanto ativo.

Desativa automaticamente após TURBO_DURATION (5 segundos) através de um Timer interno ao Player.

Condição de Vitória (Distância da Fase)
A constante PHASE_LENGTH_DISTANCE define o quão "longe" o jogador precisa ir para vencer.

distanceTraveled é incrementado a cada frame do jogo, simulando o avanço.

Quando distanceTraveled atinge PHASE_LENGTH_DISTANCE, o currentGameState muda para GAME_WON, exibindo a mensagem de vitória.

Interface do Usuário (UI)
A tela de jogo (paint na Fase) desenha o background, player, inimigos, tiros e estrelas.

Exibe um placar simples com Vidas e Distância Percorrida.

Desenha uma barra de saúde visual para o jogador.

As telas de Game Over e Vitória exibem uma imagem de fundo genérica (GameOver.jpg) e uma mensagem de texto (customizável via setGameOverText e setWinText).

Um JButton "Reiniciar" é exibido nas telas de fim de jogo para permitir que o jogador recomece.

Manutenção e Melhorias Futuras
Adicionando Novos Assets
Imagens: Coloque novos arquivos de imagem (PNG, JPG) na pasta res/.

Carregamento: No código, use new ImageIcon("res\\seu_arquivo.png").getImage() para carregar a imagem. Lembre-se de verificar se a imagem carregou com sucesso (faça um null check).

Escala: Use bufferedImage.getScaledInstance(WIDTH, HEIGHT, Image.SCALE_SMOOTH) para redimensionar suas imagens para o tamanho desejado, mantendo a proporção ou definindo um novo tamanho fixo.

Balanceando a Jogabilidade
Velocidade Inicial: Ajuste NORMAL_ENEMY_STAR_SHOT_SPEED na Fase para alterar a velocidade inicial de inimigos, estrelas e tiros.

Velocidade do Jogador: Altere NORMAL_SPEED e TURBO_SPEED_MULTIPLIER na Player para ajustar a agilidade da nave.

Frequência de Inimigos: Modifique a quantidadeInimigos no método InicializaInimigos() da Fase para controlar quantos inimigos são gerados.

Aumento de Dificuldade: Ajuste ENEMIES_PER_SPEED_UP para definir quantos inimigos precisam ser destruídos antes que a velocidade do jogo aumente, e SPEED_INCREASE_FACTOR para a magnitude desse aumento.

Vida do Jogador: Altere INITIAL_LIVES e MAX_HEALTH na classe Player para modificar a durabilidade do jogador.

Duração do Turbo: Mude TURBO_DURATION na classe Player para ajustar por quanto tempo o modo turbo fica ativo.

Comprimento da Fase: Ajuste PHASE_LENGTH_DISTANCE na Fase para tornar o jogo mais curto ou mais longo.

Correção de Bugs
Mensagens de Log: Utilize as mensagens System.out.println que você já adicionou (especialmente as que começam com "Fase: ERRO") para identificar problemas de carregamento de assets ou fluxo do jogo.

Depuração Visual: As caixas de colisão (g2d.drawRect) são ótimas para depurar problemas de colisão. Mantenha-as visíveis durante o desenvolvimento para verificar se os Rectangles estão onde você espera.

Concorrência: Como o Swing é single-threaded, certifique-se de que todas as operações de UI e manipulação de estado do jogo aconteçam na Event Dispatch Thread (EDT). SwingUtilities.invokeLater() é útil para isso, como já utilizado na Container.

Otimização de Desempenho
Reuso de Objetos: Em vez de criar novos objetos Enemy1 e Stars constantemente, você poderia implementar um pool de objetos para reciclá-los. Isso reduz a carga do Garbage Collector.

Carregamento de Imagens: Garanta que as imagens são carregadas apenas uma vez. Seu código já faz isso no método Load(), que é bom.

Desenho: Minimizar as chamadas a repaint() e otimizar o método paintComponent pode ajudar, especialmente se o jogo se tornar mais complexo.

Novas Funcionalidades a Considerar
Tipos de Inimigos: Adicione diferentes tipos de inimigos com padrões de movimento e comportamentos de ataque distintos.

Power-ups: Implemente power-ups que o jogador possa coletar (ex: aumento de tiro, escudo temporário, cura).

Efeitos Sonoros e Música: Adicione áudio para tiros, explosões, música de fundo, etc.

Pontuação: Crie um sistema de pontuação para o jogador e talvez um placar de líderes.

Múltiplas Fases: Em vez de uma única distância para a vitória, crie fases distintas com objetivos e inimigos diferentes.

Animações: Use spritesheets para animações mais fluidas para o player, inimigos e explosões.

Menu Principal: Adicione uma tela de menu inicial com opções como "Iniciar Jogo", "Configurações", "Sair".
