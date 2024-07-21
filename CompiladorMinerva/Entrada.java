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
public class Entrada
{
    public String lerArq(){
        String codigo = "";
        String nome = coletarString("nome do arquivo") + ".txt";
        try{
            FileReader arq = new FileReader(nome);
            BufferedReader lerArq = new BufferedReader(arq);
            
            String line = null;
            line = lerArq.readLine();
            
            codigo = codigo + line;
            
            while (line != null) {
                line = lerArq.readLine();
                codigo = codigo + line;
            }
            
            arq.close();    
        }catch (IOException e) {
            System.err.printf("Erro na abertura do arquivo: %s.\n",
                e.getMessage());
        }
        
        return codigo;
    }
    
    public int coletarInt(String txt){
        return Integer.parseInt(JOptionPane.showInputDialog(txt+""));
    }    

    public String coletarString(String txt){
        return JOptionPane.showInputDialog(txt+""); 
    }

    public void mostrar(String txt){
        JOptionPane.showMessageDialog(null, txt+"");
    }
}
