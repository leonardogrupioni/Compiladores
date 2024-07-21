
/**
 * Escreva uma descrição da classe Arvore aqui.
 * 
 * @author (seu nome) 
 * @version (um número da versão ou uma data)
 */
public class Arvore
{
    private No raiz;
    
    public Arvore(No no){
        setRaiz(no);
    }
    
    private void setRaiz(No raiz){
        this.raiz = raiz;
    }
    
    public No getRaiz(){
        return this.raiz;
    }
}
