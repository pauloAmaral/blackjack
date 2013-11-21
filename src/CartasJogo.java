public class CartasJogo{
	
	Carta[] cartas;
	int posUltima;
	
	public CartasJogo(){
		posUltima = 0;
		cartas = new Carta[9];
		for(int i = 0; i<9; i++){	
			cartas[i] = null;
		}
	}

	public void reinicia(){
		for(int i = 0; i<9; i++){	
			cartas[i] = null;
		}
		posUltima = 0;
	}

	public void adicionaCarta(Carta carta){
		if( posUltima<9 ){
			cartas[posUltima] = carta;
			posUltima++;
		}
	}

	public boolean estaVazia(){
		boolean vazia = false;
		if( posUltima == 0 )
			vazia = true;
	
		return vazia;
	}

	public Carta daCarta(int pos){
		return cartas[pos];
	}
}
