import java.util.HashMap;
/**
 * Escreva uma descrição da classe nome aqui.
 * 
 * @author (seu nome) 
 * @version (um número da versão ou uma data)
 */   
public class PalavrasReservadas
{   
    HashMap<String, nome> palavrasReservadas = new HashMap<String, nome>();
    
    public void inicializarPalavrasReservadas(){
        palavrasReservadas.put("int", nome.INT);
        palavrasReservadas.put("bool", nome.BOOL);
        palavrasReservadas.put("imprima", nome.IMPRIMA);
        palavrasReservadas.put("de", nome.DE);
        palavrasReservadas.put("verdadeiro", nome.VERDADEIRO);
        palavrasReservadas.put("leia", nome.LEIA);
        palavrasReservadas.put("enquanto", nome.ENQUANTO);
        palavrasReservadas.put("falso", nome.FALSO);
        palavrasReservadas.put("dec", nome.DEC);
        palavrasReservadas.put("ate", nome.ATE);
        palavrasReservadas.put("faca", nome.FACA);
        palavrasReservadas.put("funcao", nome.FUNCAO);
        palavrasReservadas.put("carac", nome.CARAC);
        palavrasReservadas.put("entao", nome.ENTAO);
        palavrasReservadas.put("para", nome.PARA);
        palavrasReservadas.put("procedimento", nome.PROCEDIMENTO);
        palavrasReservadas.put("passo", nome.PASSO);
        palavrasReservadas.put("principal", nome.PRINCIPAL);
        palavrasReservadas.put("que", nome.QUE);
        palavrasReservadas.put("repita", nome.REPITA);
        palavrasReservadas.put("retorna", nome.RETORNA);
        palavrasReservadas.put("se", nome.SE);
        palavrasReservadas.put("senao", nome.SENAO);   
    }
    
    public nome verificarPalavrasReservadas(String lexema){
        nome t;
        t = palavrasReservadas.get(lexema);
        if(t == null) t = nome.ID;
        return t;
    }    
}
