import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JOptionPane;

/**
 * Escreva uma descrição da classe Lexica aqui.
 * 
 * @author (seu nome) 
 * @version (um número da versão ou uma data)
 */
public class Lexica
{
    int indice = 1;
    
    private ArrayList<String> listtokens = new ArrayList<String>();
    
    PalavrasReservadas palavrasReservadas = new PalavrasReservadas();
    
    public ArrayList<Token> AnLex(char fonte[]){
        palavrasReservadas.inicializarPalavrasReservadas();
        
        int a = 0, b = -1, line = 1;
        ArrayList<Token> tokens = new ArrayList<Token>();
        Token token;
        String cadeia = "", tk = "erro";
        boolean adiciona = false, sentinela = false, aspa = false, aspaD = false, coment = false, menor = false, maior = false, entrou = false;
        nome tipo = nome.ID;
        
        for(int i = 0; i < fonte.length; i++){
            var c = fonte[i];
            
            if(Character.toString(c).matches("#") || coment == true){
                
                cadeia += c;
                coment = true; 
                if(cadeia.length() > 1 && c == '#'){
                    cadeia = "";
                    coment = false;
                } else if (i == fonte.length-1){
                    erro();
                }
            } else if(Character.toString(c).matches("\"") || aspaD == true){
                if(!Character.toString(c).matches("\"")) cadeia += c;
                b++;
                aspaD = true; 
                if(entrou == false) entrou = true;
                else if(Character.toString(c).matches("\"")){
                    listtokens.add("[@" + indice + ", " +  a + "-" + b + "," + "\"" + cadeia + "\"" + ", <" + nome.ARRAYC + ">," + line +"]");
                    token = new Token(indice,a,b,cadeia,nome.ARRAYC,line);
                    tokens.add(token);
                    a = b + 1; indice++; cadeia = "";
                    aspaD = false; entrou = false;
                } else if (i == fonte.length-1){
                    erro();
                }
            } else if(Character.toString(c).matches("'") || aspa == true){
                if(!Character.toString(c).matches("'")) cadeia += c; 
                b++;
                //'A'
                //234
                //5-2 = 3
                aspa = true;
                if(entrou == false) entrou = true;
                else if(Character.toString(c).matches("'")){
                    listtokens.add("[@" + indice + ", " +  a + "-" + b + "," + "\"" + cadeia + "\"" + ", <" + nome.ELCARAC + ">," + line +"]");
                    token = new Token(indice,a,b,cadeia,nome.ELCARAC,line);
                    tokens.add(token);
                    a = b + 1; indice++; cadeia = "";
                    aspa = false; entrou = false;
                } else if(b-a > 2) erro();
                
            } else if(Character.toString(c).matches("[a-zA-Z]") || sentinela == true){
                cadeia += c;
                b++;
                
                if(i < (fonte.length - 1) && !Character.toString(fonte[i+1]).matches("[a-zA-Z\\d]")){
                    tipo = palavrasReservadas.verificarPalavrasReservadas(cadeia);
                    listtokens.add("[@" + indice + ", " +  a + "-" + b + "," + "\"" + cadeia + "\"" + ", <" + tipo + ">," + line +"]");
                    token = new Token(indice,a,b,cadeia,tipo,line);
                    tokens.add(token);
                    a = b + 1; indice++; cadeia = "";
                    sentinela = false;
                }else if(i < (fonte.length - 1) && Character.toString(fonte[i+1]).matches("\\d")){
                    sentinela = true;
                }
            }
            else if(Character.toString(c).matches("\\d")){
                cadeia += c;
                b++;
                
                if(i < (fonte.length - 1) && !Character.toString(fonte[i+1]).matches("\\d")){
                    listtokens.add("[@" + indice + ", " +  a + "-" + b + "," + "\"" + cadeia + "\"" + ", <" + nome.NUM + ">," + line +"]");
                    token = new Token(indice,a,b,cadeia,nome.NUM,line);
                    tokens.add(token);
                    a = b + 1; indice++; cadeia = "";
                }
            } else if(Character.toString(c).matches("\\s")){
                //ignorando 
            } else if(Character.toString(c).matches("[+*;\\.(){}\\[\\]]")){ 
                cadeia += c; 
                b = a;
            
                String teste = Character.toString(c);
                tipo = nome.MAIS;
                switch(teste){
                    case "*":
                        tipo = nome.MULT;
                        break;
                    case ";":
                        tipo = nome.PTV;
                        break;
                    case ".":
                        tipo = nome.PTO;
                        break;
                    case "(":
                        tipo = nome.APAR;
                        break;
                    case ")":
                        tipo = nome.FPAR;
                        break;
                    case "{":
                        tipo = nome.ACHAV;
                        break;
                    case "}":
                        tipo = nome.FCHAV;
                        break;
                    case "[":
                        tipo = nome.ACOL;
                        break;
                    case "]":
                        tipo = nome.FCOL;
                        break;
                }
                
                listtokens.add("[@" + indice + ", " +  a + "-" + b + "," + "\"" + cadeia + "\"" + ", <" + tipo + ">," + line +"]");
                token = new Token(indice,a,b,cadeia,tipo,line);
                tokens.add(token);
                a = b + 1; indice++; cadeia = "";
            } else if(Character.toString(c).matches("[<>=\\-]")){
                cadeia += c; 
                
                String teste = Character.toString(c);
                
                switch(teste){
                    case ">":
                        if(!Character.toString(fonte[i+1]).matches("\\=")){
                            tipo = nome.MAIOR;
                            b = a;
                            adiciona = true;
                        } else maior = true;
                        break;
                    case "<":
                        if(!Character.toString(fonte[i+1]).matches("[=\\-]")){
                            tipo = nome.MENOR;
                            b = a;
                            adiciona = true;
                        } else menor = true;
                        break;
                    case "=":
                        if(menor){
                            tipo = nome.MENORIGUAL;
                            b = a + 1;
                        }
                        else if (maior){
                            tipo = nome.MAIORIGUAL;
                            b = a + 1;
                        }
                        else {
                            tipo = nome.IGUAL;
                            b = a;
                        }
                        adiciona = true;
                        break;
                    case "-":
                        if(menor){
                            tipo = nome.ATRIB;
                            b = a + 1;
                        }
                        else {
                            tipo = nome.MENOS;
                            b = a;
                        }
                        adiciona = true;
                        break;
                }
                
                if(adiciona){
                    listtokens.add("[@" + indice + ", " +  a + "-" + b + "," + "\"" + cadeia + "\"" + ", <" + tipo + ">," + line +"]");
                    token = new Token(indice,a,b,cadeia,tipo,line);
                    tokens.add(token);
                    a = b + 1; indice++; cadeia = "";
                    adiciona = false;
                    menor = false; maior = false; 
                }
                
            }else{
                erro();
            }
        }
        listtokens.add("[@" + indice + ", " +  a + "-" + a + "," + "\"EOF\"" + ", <" + nome.EOF + ">," + line +"]");
        token = new Token(indice,a,b,cadeia,nome.EOF,line);
        tokens.add(token);
        System.out.println(listtokens);
        return tokens; 
    }
    
    private void erro(){
        System.out.println("Erro!!");
        System.exit(0);
    }
}
