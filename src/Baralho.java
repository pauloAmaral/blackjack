import java.util.Random;

public class Baralho{
	
	private Carta[] cartas;
	private int numCartas = 52;

	public Baralho(){
		
		cartas = new Carta[numCartas];
		inicializaBaralho();
	}

	public void inicializaBaralho(){
		String []naipe = {"ouros", "espadas", "paus", "copas"};
		int j = 2;
		int posNaipe = 0;
		
		for(int i = 0; i<numCartas; i++){
										
			cartas[i] = new Carta(j,naipe[posNaipe]);
			j++;			
			if( j == 15 ){
				j = 2;
				posNaipe++;
			}
		}
	}

	public Carta tiraCarta(){
			// tirar uma carta aleatoriamente, o que a retira do baralho
		Random randomGenerator = new Random();
		int carta = -1;

		do{
			carta = randomGenerator.nextInt(52);	// gera aleatoriamente entre 0 e 51
		}while(cartas[carta]== null);

		Carta temp = cartas[carta];
		
		cartas[carta] = null;
		return temp;
	}
		
		

	public String toString(){
		String output = "";
		for(int i = 0; i<numCartas; i++){
			if( cartas[i]!= null )
				output+= cartas[i]+"\n";
		}
		return output;
	}
}
					
