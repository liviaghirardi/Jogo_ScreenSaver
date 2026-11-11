package model;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

public class Passaro extends Personagem {
    private int gravidade = 1;
    private int forcaPulo = -10;
    private boolean vivo = true; 
    
    public Passaro(int x, int y, int largura, int altura, String caminhoImagem) {
        super(x, y, largura, altura, caminhoImagem);
        setVelocidadeX(0); 
        setVelocidadeY(0);
    }

    public void pular() {
        if (vivo) setVelocidadeY(forcaPulo);
    }

    @Override
    public void Mover(int larguraTela, int alturaTela) {
        if (!vivo) return;

        setVelocidadeY(getVelocidadeY() + gravidade);
        setY(getY() + getVelocidadeY());

        if (getY() < 0) {
            setY(0);
            setVelocidadeY(0);
        }
        
        if (getY() + getAltura() >= alturaTela) {
            setY(alturaTela - getAltura());
            setVelocidadeY(0);
            morrer();
        }
    }

    @Override
    public void desenhar(Graphics2D g) {
        // Salva a configuração atual da tela
        AffineTransform configuracaoAntiga = g.getTransform();
        
        // Calcula a inclinação (limita entre virado para cima e bico para baixo)
        double angulo = Math.toRadians(Math.max(-45, Math.min(getVelocidadeY() * 4, 90)));
        
        // Rotação centralizada no meio do pássaro
        g.rotate(angulo, getX() + (getLargura() / 2.0), getY() + (getAltura() / 2.0));
        
        // Pinta a imagem rotacionada
        super.desenhar(g);
        
        // Restaura a tela para não girar os outros elementos (como os canos)
        g.setTransform(configuracaoAntiga);
    }

    public boolean isVivo() { return vivo; }
    public void morrer() { vivo = false; }
}