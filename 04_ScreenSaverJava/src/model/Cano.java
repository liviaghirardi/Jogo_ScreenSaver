package model;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

public class Cano extends Retangulo {

    public Cano(int x, int y, int largura, int altura, int velX) {
        super(x, y, largura, altura, new Color(34, 139, 34)); 
        setVelocidadeX(velX);
        setVelocidadeY(0);
    }

    @Override
    public void Mover(int larguraTela, int alturaTela) {
        setX(getX() + getVelocidadeX());
    }

    @Override
    public void desenhar(Graphics2D g) {
        // 1. Desenha o preenchimento verde (herdado do Retangulo)
        super.desenhar(g); 
        
        // 2. Desenha uma borda escura por cima para dar estilo de jogo
        g.setColor(new Color(0, 100, 0));
        g.setStroke(new BasicStroke(3)); // Espessura da linha
        g.drawRect(getX(), getY(), getLargura(), getAltura());
    }

    @Override
    public void aoColidir(Passaro passaro) {
        passaro.morrer();
    }
}