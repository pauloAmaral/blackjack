import javax.swing.ImageIcon;
import java.security.*;
import java.io.*;

public class Carta{

	private int carta;	// vai entre 2 e 13(AS)
	private String naipe;	
	private ImageIcon imagem;

	public Carta(int carta, String naipe){
		this.carta = carta;
		this.naipe = naipe;
		imagem = new ImageIcon("imagens/"+getCarta()+getNaipe()+".png");				
	}

	public int getCarta(){
		return carta;
	}

	public String getNaipe(){
		return naipe;
	}

	public String toString(){
		String output = "";
		if( getCarta()>=2 && getCarta()<=10 )
			output+=getCarta();
		else if( getCarta() == 11 )
			output+="Dama";
		else if( getCarta() == 12 )
			output+="Valete";
		else if( getCarta() == 13 )
			output+="Rei";
		else if( getCarta() == 14 )
			output+="As";

		output+=" de " + getNaipe();
		return output;
	}

	public ImageIcon getImagem(){
		return imagem;
	}

}
