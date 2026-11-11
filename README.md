# Flappy Bird POO - Mini Projeto 04

Projeto desenvolvido para a disciplina de Programação Orientada a Objetos, utilizando Java e Swing, sob a orientação do Prof. Caina Antunes. Transformamos a base de um "ScreenSaver" em um jogo completo estilo Flappy Bird, aplicando rigorosamente os quatro pilares da POO.

**Desenvolvido por:** Lívia e Danilo

---

## 🏗️ Os 4 Pilares da POO no Projeto

| Pilar | Onde está no código? | Como foi aplicado? |
| :--- | :--- | :--- |
| **Abstração** | Classe `model.Forma` | Criamos uma classe abstrata genérica que define os atributos e métodos base (x, y, `Mover`, `desenhar`) para tudo o que aparece na tela, sem se preocupar com os detalhes de desenho de cada forma específica. |
| **Encapsulamento** | Todas as classes do `model` | Todos os atributos (`x`, `y`, `velocidade`, `vivo`) são `private`. O acesso é feito via Getters e Setters, permitindo validações (ex: impedir largura negativa) e protegendo a física do jogo de alterações indevidas. |
| **Herança** | `Passaro`, `Cano`, `Nuvem` | `Passaro` estende `Personagem`, herdando a capacidade de desenhar sprites. `Cano` estende `Retangulo`. Evitamos repetição de código reutilizando a estrutura visual já existente e adicionando apenas comportamentos novos (como a gravidade). |
| **Polimorfismo** | `ScreenSaver.java` e `aoColidir()` | No Game Loop, usamos uma única lista `List<Forma>` genérica. Chamamos `Mover()` e `desenhar()` sem precisar saber se é um pássaro, nuvem ou cano. O método `aoColidir()` é reescrito (`@Override`): canos dão Game Over, nuvens não fazem nada. |

---

## 📊 Diagrama de Classes (Mermaid)

```mermaid
classDiagram
    direction TB

    class Forma {
        <<abstract>>
        -int x
        -int y
        -int largura
        -int altura
        -boolean ultrapassada
        +Forma(...)
        +desenhar(Graphics2D g)* void
        +Mover(int larguraTela, int alturaTela) void
        +intersects(Forma outra) boolean
        +aoColidir(Passaro passaro) void
    }

    class Retangulo {
        +desenhar(Graphics2D g) void
    }
    
    class Cano {
        +Cano(...)
        +Mover(int larguraTela, int alturaTela) void
        +desenhar(Graphics2D g) void
        +aoColidir(Passaro passaro) void
    }

    class Elipse {
        +desenhar(Graphics2D g) void
    }
    
    class Nuvem {
        +Nuvem(...)
        +Mover(int larguraTela, int alturaTela) void
        +aoColidir(Passaro passaro) void
    }

    class Personagem {
        -Image sprite
        +Personagem(...)
        +desenhar(Graphics2D g) void
    }
    
    class Passaro {
        -int gravidade
        -boolean vivo
        +Passaro(...)
        +pular() void
        +Mover(int larguraTela, int alturaTela) void
        +desenhar(Graphics2D g) void
    }

    Forma <|-- Retangulo : Herança
    Retangulo <|-- Cano : Especialização
    Forma <|-- Elipse : Herança
    Elipse <|-- Nuvem : Especialização
    Forma <|-- Personagem : Herança
    Personagem <|-- Passaro : Especialização
