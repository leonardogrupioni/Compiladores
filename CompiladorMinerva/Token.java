public class Token
{
    private nome tk;  
    private int a;
    private int b;
    private int index;
    private int line;
    private String lexema;
    
    public Token(int index, int a, int b, String lexema, nome tk, int line){
        setIndex(index);
        setA(a);
        setB(b);
        setLexema(lexema);
        setNome(tk);
        setLine(line);
    }

    // Getter and Setter for tk
    public nome getNome() {
        return tk;
    }

    private void setNome(nome tk) {
        this.tk = tk;
    }

    // Getter and Setter for a
    public int getA() {
        return a;
    }

    private void setA(int a) {
        this.a = a;
    }

    // Getter and Setter for b
    public int getB() {
        return b;
    }

    private void setB(int b) {
        this.b = b;
    }

    // Getter and Setter for index
    public int getIndex() {
        return index;
    }

    private void setIndex(int index) {
        this.index = index;
    }

    // Getter and Setter for line
    public int getLine() {
        return line;
    }

    private void setLine(int line) {
        this.line = line;
    }

    // Getter and Setter for lexema
    public String getLexema() {
        return lexema;
    }

    private void setLexema(String lexema) {
        this.lexema = lexema;
    }
    
    public String toString(){
        return ("[@" + this.index + ", " +  this.a + "-" + this.b + "," + "\"" + this.lexema + "\"" + ", <" + this.tk + ">," + this.line +"]");
    }
}
