package model;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public abstract class Forma {
    private int x, y, largura, altura, velocidadeX, velocidadeY;
    private Color cor;
    
    // NOVO: Controla se a forma já foi ultrapassada pelo jogador (para contar pontos)
    private boolean ultrapassada = false;

    public Forma(int x, int y, int largura, int altura, Color cor) {
        this.x = x; this.y = y;
        // Atribuição direta: chamar setters sobrescrevíveis no construtor é arriscado
        if (largura > 0) this.largura = largura;
        if (altura > 0) this.altura = altura;
        this.cor = cor;
        this.velocidadeX = 5; this.velocidadeY = 5;
    }

    public abstract void desenhar(Graphics2D g);

    public void Mover(int larguraTela, int alturaTela) {
        setX(getX() + getVelocidadeX());
        setY(getY() + getVelocidadeY());

        if (getX() < 0 || getX() + getLargura() > larguraTela) setVelocidadeX(getVelocidadeX() * -1);
        if (getY() < 0 || getY() + getAltura() > alturaTela) setVelocidadeY(getVelocidadeY() * -1);
    }

    public boolean intersects(Forma outra) {
        Rectangle minhaCaixa = new Rectangle(this.getX(), this.getY(), this.getLargura(), this.getAltura());
        Rectangle outraCaixa = new Rectangle(outra.getX(), outra.getY(), outra.getLargura(), outra.getAltura());
        return minhaCaixa.intersects(outraCaixa);
    }

    public void aoColidir(Passaro passaro) {}

    // Getters e Setters
    public int getX() { return x; }
    public void setX(int x) { this.x = x; }
    public int getY() { return y; }
    public void setY(int y) { this.y = y; }
    public int getLargura() { return largura; }
    public void setLargura(int largura) { if (largura > 0) this.largura = largura; }
    public int getAltura() { return altura; }
    public void setAltura(int altura) { if (altura > 0) this.altura = altura; }
    public int getVelocidadeX() { return velocidadeX; }
    public void setVelocidadeX(int velocidadeX) { this.velocidadeX = velocidadeX; }
    public int getVelocidadeY() { return velocidadeY; }
    public void setVelocidadeY(int velocidadeY) { this.velocidadeY = velocidadeY; }
    public Color getCor() { return cor; }
    public void setCor(Color cor) { this.cor = cor; }
    public boolean isUltrapassada() { return ultrapassada; }
    public void setUltrapassada(boolean ultrapassada) { this.ultrapassada = ultrapassada; }
}