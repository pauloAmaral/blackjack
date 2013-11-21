import java.awt.Color;

public class Botao{
	
	private int x,y,tamX,tamY;
	private Color cor, corOver, corClicado;
	private boolean activo;


	public Botao(int x, int y, int tamX, int tamY, Color cor, boolean activo){	
		this.x = x;
		this.y = y;
		this.tamX = tamX;
		this.tamY = tamY;
		this.cor = cor;
		this.activo = activo;
	}

	public boolean getActivo(){
		return activo;
	}

	public void setActivo(boolean activo){
		this.activo = activo;
	}

	public void setCor(Color cor){
		this.cor = cor;
	}

	public int getX(){
		return x;
	}

	public int getY(){
		return y;
	}

	public int getTamX(){
		return tamX;
	}

	public int getTamY(){
		return tamY;
	}

	public Color getCor(){
		return cor;
	}

	public boolean estaDentro(int mx, int my){
		boolean dentro = false;

		if( (mx>=x && mx<x+tamX) && (my>=y && my<y+tamY) )
			dentro=true;
		
		return dentro;
	}
}
