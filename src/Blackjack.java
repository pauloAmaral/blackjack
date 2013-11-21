import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.font.*;
import java.text.*;

@SuppressWarnings("serial")
public class Blackjack extends JApplet implements Runnable, MouseListener, MouseMotionListener{


	Thread motor;
	Image offScreenImage;		// vai criar uma imagem que preenchemos primeiro e so depois colocamos no ecra
	Graphics offScr;		// Graphics associado a imagem que pintamos fora do ecra
	int comprimento, altura, pontosDealer, pontosJogador, ratoX, ratoY, estadoJogo, numCartasJ;
	boolean corre;
	CartasJogo cartasDealer;
	CartasJogo cartasJogador;
	Baralho baralho;
	Color corFundo,corBotao,corOver, corClicado, corDesactivo;
	Botao botaoDeal, botaoHit, botaoStand;
	String estado;	

	public void init(){
		motor = new Thread(this);
		comprimento = getWidth();
		altura = getHeight();

		addMouseListener(this);
		addMouseMotionListener(this);

		baralho = new Baralho();
		corFundo = new Color(0,172,110);

		cartasDealer = new CartasJogo();
		cartasJogador = new CartasJogo();
	
		corBotao = new Color(218,165,32);
		corOver = new Color(140,165,32);
		corClicado = new Color(140,165,204);
		corDesactivo = Color.lightGray;

		botaoDeal = new Botao(255,430,100,40,corBotao,true);
		botaoHit = new Botao(375,430,100,40,corBotao,false);
		botaoStand = new Botao(495,430,100,40,corBotao,false);
		pontosJogador = pontosDealer = 0;

		estado = "Not Playing";
		
		numCartasJ = 0;
		estadoJogo = 0;
	}

	public void start(){
		corre = true;
		motor.start();
	}

	public void run(){
		while(corre){
			repaint();
			
			if( estadoJogo == 1 ){
				// Inicio, dealer tira uma carta
				cartasJogador.reinicia();
				cartasDealer.reinicia();
				pontosJogador = 0;
				pontosDealer = 0;
				Carta c = baralho.tiraCarta();
				cartasDealer.adicionaCarta(c);
				pontosDealer += daPontos(c,pontosDealer);
				estadoJogo = 2;
				botaoDeal.setActivo(false);
				botaoHit.setActivo(true);
				botaoStand.setActivo(true);
			}else if( estadoJogo == 3 ){
				// Quando o jogador carrega em deal
				if( pontosDealer != 21 && pontosDealer<=pontosJogador ){
					Carta c = baralho.tiraCarta();
					cartasDealer.adicionaCarta(c);
					pontosDealer += daPontos(c,pontosDealer);
					if( pontosDealer>21 )
						gameover("Player Wins!");
					else if( pontosDealer>pontosJogador && pontosDealer<=21 )
						gameover("Dealer Wins!");
					else if( pontosDealer == 21 && pontosJogador == 21 )
						gameover("It's a Draw!");
				}else if( pontosDealer>pontosJogador ){
					gameover("Dealer Wins!");
				}else if( pontosDealer == 21 && pontosJogador == 21 ){
						gameover("It's a Draw!");
				}
			}

		try{
			Thread.sleep(1000/20);
			} catch(InterruptedException ie){
				System.err.println(ie.getMessage());
			}	
		}

	}

	public void update(Graphics g){
		paint(g);
	}

	public void paint(Graphics g){
		if( offScr == null){
			offScreenImage = createImage(comprimento, altura);
			offScr = offScreenImage.getGraphics();
		}

		// desenhar sempre para offScr

		// desenhar fundo
		offScr.setColor(corFundo);
		offScr.fillRect(0,0,comprimento,altura);

		offScr.setColor(Color.black);
		
		// Texto Dealer

		String texto = "Dealer:";
		Font font1 = new Font("Purisa", Font.BOLD, 20);
		AttributedString attributedString = new AttributedString(texto);
		attributedString.addAttribute(TextAttribute.FONT, font1);
		offScr.drawString(attributedString.getIterator(),35,30);
			
		// desenhar cartas do dealer

		desenhaCartas(cartasDealer,offScr,40,50,80,10);
		
		// Pontos Dealer
		offScr.setColor(Color.black);
		texto = "Dealer's Points: " + pontosDealer;
		attributedString = new AttributedString(texto);
		attributedString.addAttribute(TextAttribute.FONT, font1);
		offScr.drawString(attributedString.getIterator(),35,185);

		Font font2 = new Font("Purisa", Font.BOLD, 30);

		offScr.setColor(Color.white);
		attributedString = new AttributedString(estado);
		attributedString.addAttribute(TextAttribute.FONT, font2);
		offScr.drawString(attributedString.getIterator(),560,210);
	
		// Texto Dealer

		offScr.setColor(Color.black);
		texto = "Player:";
		attributedString = new AttributedString(texto);
		attributedString.addAttribute(TextAttribute.FONT, font1);
		offScr.drawString(attributedString.getIterator(),35,245);

		// desenhar cartas do jogador
		
		desenhaCartas(cartasJogador,offScr,40,265,80,10);

		// Pontos Jogador
		offScr.setColor(Color.black);
		texto = "Player's Points: " + pontosJogador;
		attributedString = new AttributedString(texto);
		attributedString.addAttribute(TextAttribute.FONT, font1);
		offScr.drawString(attributedString.getIterator(),35,400);

		// desenhar botoes
		desenhaBotao(botaoDeal,botaoDeal.getCor(),offScr);
		desenhaBotao(botaoHit,botaoHit.getCor(),offScr);
		desenhaBotao(botaoStand,botaoStand.getCor(),offScr);

		
		offScr.setColor(Color.darkGray);
		
		texto = "Deal";
		attributedString = new AttributedString(texto);
		attributedString.addAttribute(TextAttribute.FONT, font1);
		offScr.drawString(attributedString.getIterator(),280,457);

	
		texto = "Hit";
		attributedString = new AttributedString(texto);
		attributedString.addAttribute(TextAttribute.FONT, font1);
		offScr.drawString(attributedString.getIterator(),410,457);

		
		texto = "Stand";
		attributedString = new AttributedString(texto);
		attributedString.addAttribute(TextAttribute.FONT, font1);
		offScr.drawString(attributedString.getIterator(),510,457);

		

		g.drawImage(offScreenImage,0,0,null);

	}

	public int daPontos(Carta c, int pontosJogador){
		int pontos = c.getCarta();

		if( c.getCarta()>10 && c.getCarta() != 14 ){
			pontos = 10;
		}else if( c.getCarta()>10 && c.getCarta() == 14 ){
			if( pontosJogador+11 >21 )
				pontos=1;
			else
				pontos=11;
		}

		return pontos;
	}

	public void gameover(String estado){
		this.estado = estado;
		botaoDeal.setActivo(true);
		botaoHit.setActivo(false);
		botaoStand.setActivo(false);
		estadoJogo=0;
		baralho = new Baralho();
		numCartasJ = 0;
	}

	public void desenhaCartas(CartasJogo cg, Graphics gr, int startX, int startY, int tamX, int distX){
		// cg -> cartas de jogo;
		// gr -> objecto graphics para onde se deseja desenhar;
		// startx e starty -> primeira posicao para onde se deseja desenhar;
		// tamX -> comprimento das cartas;
		// distX -> distancia entre as cartas

		int posx = startX;
	
		for(int i = 0; i<9; i++){
			Carta cart = cg.daCarta(i);
			if( cart == null ){
				break;
			}
			cart.getImagem().paintIcon(null,gr,posx,startY);
			posx+=tamX+distX;		
		}
	}

	public boolean eBlackJack(Carta c1, Carta c2){
		boolean ebl = false;
		int v1 = c1.getCarta();
		int v2 = c2.getCarta();
		
		if( ( (v1==10||v1==11||v1==12||v1==13) && v2==14) || (v1==14 && (v2==10||v2==11||v2==12||v2==13) ) )
			ebl=true;

		return ebl;
	}

	public void desenhaBotao(Botao b,Color cor,Graphics g){
		Color c = cor;

		if( !b.getActivo() )
			c = corDesactivo;
		g.setColor(c); 
		g.fillRect(b.getX(),b.getY(),b.getTamX(),b.getTamY());
		g.setColor(Color.black);
		g.drawRect(b.getX(),b.getY(),b.getTamX(),b.getTamY());
	}

	public void mouseClicked(MouseEvent e){}
		

   	public void mousePressed(MouseEvent e){
		ratoX = e.getX();
		ratoY = e.getY();
			
		if( botaoDeal.estaDentro(ratoX,ratoY) && botaoDeal.getActivo() ){
			botaoDeal.setCor(corClicado);
			estado = "Playing";
			estadoJogo = 1;
		}else if( botaoHit.estaDentro(ratoX,ratoY) && botaoHit.getActivo() ){
			numCartasJ++;
			botaoHit.setCor(corClicado);
			Carta c = baralho.tiraCarta();
			cartasJogador.adicionaCarta(c);
			pontosJogador+=daPontos(c,pontosJogador);
			if( numCartasJ == 2 && eBlackJack(cartasJogador.daCarta(0),cartasJogador.daCarta(1)) ){
				gameover("Player Wins!");
			}else if( pontosJogador>21 ){
				//GAME OVER
				gameover("Dealer Wins!");
			}
		}else if( botaoStand.estaDentro(ratoX,ratoY) && botaoStand.getActivo() ){
			botaoStand.setCor(corClicado);	
			botaoHit.setActivo(false);
			estadoJogo = 3;
		}else{
			botaoDeal.setCor(corBotao);
			botaoHit.setCor(corBotao);
			botaoStand.setCor(corBotao);
		}
		
	}

   	public void mouseReleased(MouseEvent e){
		ratoX = e.getX();
		ratoY = e.getY();

		if( botaoDeal.estaDentro(ratoX,ratoY) ){
			botaoDeal.setCor(corOver);
		}else if( botaoHit.estaDentro(ratoX,ratoY) ){
			botaoHit.setCor(corOver);
		}else if( botaoStand.estaDentro(ratoX,ratoY) ){
			botaoStand.setCor(corOver);
		}else{
			botaoDeal.setCor(corBotao);
			botaoHit.setCor(corBotao);
			botaoStand.setCor(corBotao);
		}

	}

	public void mouseEntered(MouseEvent e){}

	public void mouseExited(MouseEvent e){}
	
	public void mouseDragged(MouseEvent e){}

	public void mouseMoved(MouseEvent e){
		ratoX = e.getX();
		ratoY = e.getY();

		if( botaoDeal.estaDentro(ratoX,ratoY) ){
			botaoDeal.setCor(corOver);
		}else if( botaoHit.estaDentro(ratoX,ratoY) ){
			botaoHit.setCor(corOver);
		}else if( botaoStand.estaDentro(ratoX,ratoY) ){
			botaoStand.setCor(corOver);
		}else{
			botaoDeal.setCor(corBotao);
			botaoHit.setCor(corBotao);
			botaoStand.setCor(corBotao);
		}
	}







}
