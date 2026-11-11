package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import model.Cano;
import model.Forma;
import model.Nuvem;
import model.Passaro;

public class ScreenSaver extends JFrame {

    private static final int CANVAS_WIDTH = 800;
    private static final int CANVAS_HEIGHT = 600;
    private static final int CHAO_ALTURA = 40; 
    private static final int DELAY_MS = 16; 

    private ScreenSaverPanel canvas;

    public ScreenSaver() {
        super("Jogo - Flappy Bird (Base POO)");
        this.canvas = new ScreenSaverPanel();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setContentPane(canvas);
        this.pack();
        this.setLocationRelativeTo(null);
    }

    public void start() {
        this.setVisible(true);
        this.canvas.startAnimation();
        this.canvas.requestFocusInWindow();
    }

    private class ScreenSaverPanel extends JPanel implements ActionListener {

        private Timer timer;
        
        /*********************************
        ** ↓ Declare suas formas aqui ↓ **
        *********************************/
        private List<Forma> formas = new ArrayList<>();
        private List<Forma> nuvens = new ArrayList<>(); 
        
        private Passaro jogador;
        private Image logo;
        
        private boolean up = false;
        private boolean puloPronto = true;
        
        private int contadorFrames = 0; 
        private int pontuacao = 0;
        private int recorde = 0;
        private int meiosPontos = 0; 
        private int velocidadeGlobal = -4; 
        
        private boolean jogoIniciado = false; 
        private boolean gameOver = false;

        public ScreenSaverPanel() {
            this.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
            this.setFocusable(true);

            try {
                URL urlLogo = ScreenSaver.class.getResource("/image/logo.png");
                logo = (urlLogo != null) ? ImageIO.read(urlLogo) : ImageIO.read(new File("src/image/logo.png"));
            } catch (Exception e) {
                logo = null;
            }

            /***********************************
            ** ↓ Instancie suas formas aqui ↓ **
            ***********************************/
            nuvens.add(new Nuvem(100, 80, 120, 60, -1));
            nuvens.add(new Nuvem(450, 120, 150, 75, -2));
            nuvens.add(new Nuvem(700, 50, 100, 50, -1));

            this.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
                        acaoPular();
                    }
                }
                @Override
                public void keyReleased(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) up = false;
                }
            });

            this.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) { acaoPular(); }
                @Override
                public void mouseReleased(MouseEvent e) { up = false; }
            });

            reiniciarJogo(); 
            this.timer = new Timer(DELAY_MS, this);
        }

        private void acaoPular() {
            if (!jogoIniciado) {
                jogoIniciado = true;
                up = true;
            } else if (gameOver && puloPronto) {
                reiniciarJogo();
            } else {
                up = true;
            }
        }

        private void reiniciarJogo() {
            formas.clear();
            jogador = new Passaro(150, 250, 52, 41, "image/flipbird.png");
            formas.add(jogador);
            
            pontuacao = 0;
            meiosPontos = 0;
            velocidadeGlobal = -4;
            contadorFrames = 0;
            gameOver = false;
            jogoIniciado = false;
            puloPronto = false; 
            up = false;
        }

        private void gerarParDeCanos() {
            int espacoAbertura = 160; 
            int larguraCano = 70;
            int alturaCanoCima = 50 + (int)(Math.random() * (CANVAS_HEIGHT - CHAO_ALTURA - espacoAbertura - 100));
            
            Cano canoCima = new Cano(CANVAS_WIDTH, 0, larguraCano, alturaCanoCima, velocidadeGlobal);
            Cano canoBaixo = new Cano(CANVAS_WIDTH, alturaCanoCima + espacoAbertura, larguraCano, CANVAS_HEIGHT - CHAO_ALTURA - alturaCanoCima - espacoAbertura, velocidadeGlobal);
            
            formas.add(canoCima);
            formas.add(canoBaixo);
        }

        @Override
        public Dimension getPreferredSize() { return new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT); }
        @Override
        public Dimension getMinimumSize() { return new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT); }
        public void startAnimation() { this.timer.start(); }

        @Override
        public void actionPerformed(ActionEvent e) {
            
            if (!up) puloPronto = true; 

            /******************************
            ** ↓ Mova suas formas aqui ↓ **
            ******************************/
            for (Forma n : nuvens) {
                n.Mover(CANVAS_WIDTH, CANVAS_HEIGHT);
            }

            if (!jogoIniciado || gameOver) {
                this.repaint();
                return;
            }

            if (up && puloPronto) {
                jogador.pular();
                puloPronto = false;
            }

            contadorFrames++;
            int limiteFrames = 100 + (velocidadeGlobal * 3); 
            if (contadorFrames >= limiteFrames) {
                gerarParDeCanos();
                contadorFrames = 0;
            }

            for (int i = formas.size() - 1; i >= 0; i--) {
                Forma f = formas.get(i);
                
                f.Mover(CANVAS_WIDTH, CANVAS_HEIGHT - CHAO_ALTURA);
                
                if (f != jogador) {
                    if (jogador.intersects(f)) f.aoColidir(jogador);
                    
                    if (!f.isUltrapassada() && f.getX() + f.getLargura() < jogador.getX()) {
                        f.setUltrapassada(true);
                        meiosPontos++;
                        if (meiosPontos % 2 == 0) { 
                            pontuacao++;
                            if (pontuacao % 5 == 0 && velocidadeGlobal > -10) velocidadeGlobal--; 
                        }
                    }

                    if (f.getX() + f.getLargura() < 0) formas.remove(i);
                }
            }

            if (!jogador.isVivo()) {
                gameOver = true;
                if (pontuacao > recorde) recorde = pontuacao;
            }

            this.repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            GradientPaint ceu = new GradientPaint(0, 0, new Color(135, 206, 235), 0, CANVAS_HEIGHT, new Color(255, 255, 255));
            g2d.setPaint(ceu);
            g2d.fillRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);

            /*********************************
            ** ↓ Desenhe suas formas aqui ↓ **
            *********************************/
            for (Forma n : nuvens) { n.desenhar(g2d); }

            for (Forma f : formas) { f.desenhar(g2d); }

            g2d.setColor(new Color(222, 184, 135)); 
            g2d.fillRect(0, CANVAS_HEIGHT - CHAO_ALTURA, CANVAS_WIDTH, CHAO_ALTURA);
            g2d.setColor(new Color(34, 139, 34)); 
            g2d.fillRect(0, CANVAS_HEIGHT - CHAO_ALTURA, CANVAS_WIDTH, 8);

            if (!jogoIniciado) {
                if (logo != null) {
                    g2d.drawImage(logo, (CANVAS_WIDTH - 250) / 2, 120, 250, 39, null);
                }
                g2d.setColor(Color.DARK_GRAY);
                g2d.setFont(new Font("Arial", Font.BOLD, 40));
                g2d.drawString("FLAPPY BIRD", 270, 280);
                g2d.setFont(new Font("Arial", Font.BOLD, 20));
                g2d.drawString("Clique ou pressione ESPAÇO para Começar", 195, 340);
                
            } else {
                g2d.setColor(Color.BLACK);
                g2d.setFont(new Font("Arial", Font.BOLD, 26));
                g2d.drawString("Pontos: " + pontuacao, 20, 40);
                g2d.drawString("Recorde: " + recorde, 20, 70);
                
                g2d.setColor(Color.WHITE);
                g2d.drawString("Pontos: " + pontuacao, 18, 38);
                g2d.drawString("Recorde: " + recorde, 18, 68);

                if (gameOver) {
                    g2d.setColor(new Color(0, 0, 0, 150));
                    g2d.fillRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT); 
                    
                    g2d.setColor(Color.RED);
                    g2d.setFont(new Font("Arial", Font.BOLD, 60));
                    g2d.drawString("GAME OVER", 200, 250);
                    
                    g2d.setColor(Color.WHITE);
                    g2d.setFont(new Font("Arial", Font.BOLD, 20));
                    g2d.drawString("Pressione ESPAÇO para reiniciar", 230, 320);
                }
            }
        }
    }
}