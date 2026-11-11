package model;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.File;
import java.net.URL;
import javax.imageio.ImageIO;

public class Personagem extends Forma {
    private Image sprite;

    public Personagem(int x, int y, int largura, int altura, String caminhoImagem) {
        // Usa Color.WHITE como cor de "fallback" (segurança) caso a imagem falhe
        super(x, y, largura, altura, Color.WHITE);
        carregarImagem(caminhoImagem);
    }

    private void carregarImagem(String caminho) {
        try {
            URL url = Personagem.class.getResource("/" + caminho);
            sprite = (url != null) ? ImageIO.read(url) : ImageIO.read(new File("src/" + caminho));
        } catch (Exception e) {
            System.out.println("Não foi possível carregar a imagem: " + caminho);
            sprite = null;
        }
    }

    @Override
    public void desenhar(Graphics2D g) {
        if (sprite != null) {
            // Desenha a imagem esticando-a para a largura e altura da Bounding Box
            g.drawImage(sprite, getX(), getY(), getLargura(), getAltura(), null);
        } else {
            // Caso a imagem não carregue, desenha um retângulo para o jogo não quebrar
            g.setColor(getCor());
            g.fillRect(getX(), getY(), getLargura(), getAltura());
        }
    }
}