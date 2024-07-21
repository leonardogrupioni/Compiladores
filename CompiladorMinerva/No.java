import java.util.ArrayList;

/**
 * Escreva uma descrição da classe No aqui.
 * 
 * @author (seu nome) 
 * @version (um número da versão ou uma data)
 */
public class No
{
    private String chave;
    private ArrayList<No> filhos;
    
    public No(String chave){
        setChave(chave);
        filhos = new ArrayList<>();
    }
    
    private void setChave(String chave){
        this.chave = chave;
    }
    
    public String getChave(){
        return this.chave;
    }
    
    public No acrescentarFilho(String chave){
        No filho = new No(chave);
        filhos.add(filho);
        
        return filho;
    }
    
    public void imprimirArvore(){
        System.out.println("\nraiz: " + getChave());
        System.out.print("filhos: ");
        
        if(filhos.size() != 0){
            for(int i = 0; i < filhos.size(); i++){
                No f = filhos.get(i);
                System.out.print(f.getChave() + " ");
            }
        
            for(int i = 0; i < filhos.size(); i++){
                No f = filhos.get(i);
            
                if(f.filhos.size() != 0){
                    f.imprimirArvore();
                }
            }
        }
        
    }
}
