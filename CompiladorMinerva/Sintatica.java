import java.util.*;
import java.lang.Math;
/**
 * Escreva uma descrição da classe TreeTest aqui.
 * 
 * @author (seu nome) 
 * @version (um número da versão ou uma data)
 */
public class Sintatica
{ 
    ArrayList<Object> codigo = new ArrayList<Object>(); //array codigo fonte
    private boolean tipo, nomeMetodo, variavel;
    private int i, limite, countPrinc;
    private Token token;
    private No no;
    ArrayList<Token> Tokens;    
    nome nome;

    public void programa(){
        Entrada entrada = new Entrada();
        String teste = entrada.lerArq();
        char[] n = teste.toCharArray();

        Lexica lex = new Lexica();
        Tokens = lex.AnLex(n);

        Arvore arvSintatica = AnSint(Tokens);
    }

    private Arvore AnSint(ArrayList<Token> tokens){
        no = new No("Inicio");
        Arvore arv = new Arvore(no);
        limite = Tokens.size();

        nome nome;

        i = 0;
        countPrinc = 0;

        token = tokens.get(i);

        procedimento();

        return arv;
    }

    private void funcao(){
        if(token.getNome() == nome.FUNCAO){
            no.acrescentarFilho(token.getNome() + ": " + token.getLexema());

            proxToken();

            if(tipo()){
                tipo = false;

                no.acrescentarFilho(token.getNome() + ": " + token.getLexema());

                proxToken();

                nomeMetodo(no);

            }else erro(token);

            Apar(no); 

            if(tipo()){
                No param = no.acrescentarFilho("parametro");
                tipo = false;
                param.acrescentarFilho(token.getNome() + ": " + token.getLexema());
                proxToken();

                if(variavel()){
                    variavel = false;

                    param.acrescentarFilho(token.getNome() + ": " + token.getLexema());

                    proxToken();
                }else erro(token);
            }

            Fpar(no);

            if(token.getNome() == nome.PTV){
                no.acrescentarFilho(token.getNome() + ": " + token.getLexema());
                proxToken();

                procedimento();
            }else if(token.getNome() == nome.ACHAV){
                no.acrescentarFilho(token.getNome() + ": " + token.getLexema());
                proxToken();

                No bloco = no.acrescentarFilho("bloco");
                bloco(bloco);

                if(token.getNome() == nome.RETORNA){
                    bloco.acrescentarFilho(token.getNome() + ": " + token.getLexema());
                    proxToken();

                    if(token.getNome() == nome.NUM || token.getNome() == nome.ID){
                        bloco.acrescentarFilho(token.getNome() + ": " + token.getLexema());
                        proxToken();

                        if(token.getNome() == nome.PTV){
                            bloco.acrescentarFilho(token.getNome() + ": " + token.getLexema());
                            proxToken();

                            if(token.getNome() == nome.FCHAV){
                                no.acrescentarFilho(token.getNome() + ": " + token.getLexema());
                                proxToken();

                                Eof();
                            }else erro(token);
                        }else erro(token);
                    }else erro(token);
                }else erro(token);
            }else erro(token);
        }else erro(token);
    }

    private void procedimento(){
        if(token.getNome() == nome.PROCEDIMENTO){
            no.acrescentarFilho(token.getNome() + ": " + token.getLexema());
            proxToken();

            if(token.getNome() == nome.PRINCIPAL){
                no.acrescentarFilho(token.getNome() + ": " + token.getLexema());
                proxToken();
                countPrinc++;

                Apar(no);
                Fpar(no);

                if(token.getNome() == nome.ACHAV){
                    no.acrescentarFilho(token.getNome() + ": " + token.getLexema());
                    proxToken();

                    No bloco = no.acrescentarFilho("bloco");
                    bloco(bloco);

                    if(token.getNome() == nome.FCHAV){
                        no.acrescentarFilho(token.getNome() + ": " + token.getLexema());
                        proxToken();

                        Eof();
                    }else erro(token);
                }else erro(token);
            }else {

                nomeMetodo(no);
                Apar(no);   

                if(tipo()){
                    No param = no.acrescentarFilho("parametro");
                    tipo = false;
                    param.acrescentarFilho(token.getNome() + ": " + token.getLexema());
                    proxToken();

                    if(variavel()){
                        variavel = false;

                        param.acrescentarFilho(token.getNome() + ": " + token.getLexema());

                        proxToken();
                    }else erro(token);
                }

                Fpar(no);

                if(token.getNome() == nome.PTV){
                    no.acrescentarFilho(token.getNome() + ": " + token.getLexema());
                    proxToken();
                    
                    procedimento();
                }else if(token.getNome() == nome.ACHAV){
                    no.acrescentarFilho(token.getNome() + ": " + token.getLexema());
                    proxToken();

                    No bloco = no.acrescentarFilho("bloco");
                    bloco(bloco);

                    if(token.getNome() == nome.FCHAV){
                        no.acrescentarFilho(token.getNome() + ": " + token.getLexema());

                        proxToken();

                        Eof();
                    }else erro(token);
                }else erro(token);
            }
        }else funcao();
    }

    private void Eof(){
        if(token.getNome() == nome.EOF){
            fimSintatica();
        }else procedimento();
    }

    private void bloco(No bloco){
        while(token.getNome() == nome.SE || token.getNome() == nome.ENQUANTO || token.getNome() == nome.REPITA || token.getNome() == nome.PARA || token.getNome() == nome.IMPRIMA || token.getNome() == nome.LEIA || token.getNome() == nome.ID || tipo()){
            if(token.getNome() == nome.SE){
                instrucao(bloco);
            }else if(token.getNome() == nome.ENQUANTO){
                repeticaoEnquanto(bloco);
            }else if(token.getNome() == nome.REPITA){
                repeticaoRepitaAte(bloco);
            }else if(token.getNome() == nome.PARA){
                repeticaoRepitaPara(bloco);
            }else if(token.getNome() == nome.IMPRIMA || token.getNome() == nome.LEIA){
                No comando = bloco.acrescentarFilho("comando");

                imprimaLeia(comando);
            }else if(token.getNome() == nome.ID){
                Token tokenAnt = token;

                proxToken();

                if(token.getNome() == nome.APAR){
                    No chamada = bloco.acrescentarFilho("chamadaMetodo");
                    No nomeMetodo = chamada.acrescentarFilho("nomeMetodo");
                    nomeMetodo.acrescentarFilho(tokenAnt.getNome() + ": " + tokenAnt.getLexema());

                    chamada.acrescentarFilho(token.getNome() + ": " + token.getLexema());
                    proxToken();

                    chamadaMetodo(chamada);
                }else{
                    No atribuicao = bloco.acrescentarFilho("atribuicaoVariavel");
                    No variavel = atribuicao.acrescentarFilho("variavel");
                    variavel.acrescentarFilho(tokenAnt.getNome() + ": " + tokenAnt.getLexema());

                    atribuicaoVariavel(atribuicao);
                }
            }else declaracao(bloco);
        }
    }

    private void chamadaMetodo(No chamada){
        if(variavel()){
            variavel = false;

            No variavel = chamada.acrescentarFilho("variavel");
            variavel.acrescentarFilho(token.getNome() + ": " + token.getLexema());

            proxToken();
        }

        Fpar(chamada);
        Ptv(chamada);
    }

    private void atribuicaoVariavel(No atribuicao){
        if(token.getNome() == nome.ATRIB){
            atribuicao.acrescentarFilho(token.getNome() + ": " + token.getLexema());
            proxToken();

            if(token.getNome() == nome.VERDADEIRO || token.getNome() == nome.FALSO){
                atribuicao.acrescentarFilho(token.getNome() + ": " + token.getLexema());
                proxToken();

                if(token.getNome() == nome.PTV){
                    atribuicao.acrescentarFilho(token.getNome() + ": " + token.getLexema());
                    proxToken();
                }else erro(token);
            }else if(token.getNome() == nome.ARRAYC){
                atribuicao.acrescentarFilho(token.getNome() + ": " + token.getLexema());
                proxToken();

                if(token.getNome() == nome.PTV){
                    atribuicao.acrescentarFilho(token.getNome() + ": " + token.getLexema());
                    proxToken();
                }else erro(token);

            }else if(token.getNome() == nome.ELCARAC){
                atribuicao.acrescentarFilho(token.getNome() + ": " + token.getLexema());
                proxToken();

                if(token.getNome() == nome.PTV){
                    atribuicao.acrescentarFilho(token.getNome() + ": " + token.getLexema());
                    proxToken();
                }else erro(token);
            }else if(token.getNome() == nome.ID){
                Token tokenAnt = token;

                proxToken();

                if(token.getNome() == nome.APAR){
                    No nomeMetodo = atribuicao.acrescentarFilho("nomeMetodo");
                    nomeMetodo.acrescentarFilho(tokenAnt.getNome() + ": " + tokenAnt.getLexema());

                    atribuicao.acrescentarFilho(token.getNome() + ": " + token.getLexema());
                    proxToken();

                    chamadaMetodo(atribuicao);
                }else{
                    No variavel = atribuicao.acrescentarFilho("termo");
                    variavel.acrescentarFilho(tokenAnt.getNome() + ": " + tokenAnt.getLexema());

                    expressao(atribuicao);

                    if(token.getNome() == nome.PTV){
                        atribuicao.acrescentarFilho(token.getNome() + ": " + token.getLexema());
                        proxToken();
                    }else erro(token);
                }
            }else{
                expressao(atribuicao);

                if(token.getNome() == nome.PTV){
                    atribuicao.acrescentarFilho(token.getNome() + ": " + token.getLexema());
                    proxToken();
                }else erro(token);
            }
        }else erro(token);
    }

    private void imprimaLeia(No comando){
        if(token.getNome() == nome.IMPRIMA){
            comando.acrescentarFilho(token.getNome() + ": " + token.getLexema());
            proxToken();

            Apar(comando);
            if(token.getNome() == nome.ID){
                No variavel = comando.acrescentarFilho("variavel");
                variavel.acrescentarFilho(token.getNome() + ": " + token.getLexema());
                proxToken();

                Fpar(comando);
                Ptv(comando);
            }else if(token.getNome() == nome.ARRAYC){
                comando.acrescentarFilho(token.getNome() + ": " + token.getLexema());
                proxToken();

                Fpar(comando);
                Ptv(comando);
            }else erro(token);
        }else if(token.getNome() == nome.LEIA){
            comando.acrescentarFilho(token.getNome() + ": " + token.getLexema());
            proxToken();

            Apar(comando);

            if(token.getNome() == nome.ID){
                No variavel = comando.acrescentarFilho("variavel");
                variavel.acrescentarFilho(token.getNome() + ": " + token.getLexema());
                proxToken();

                Fpar(comando);
                Ptv(comando);
            }
        }
    }

    private void Apar(No bloco){
        if(token.getNome() == nome.APAR){
            bloco.acrescentarFilho(token.getNome() + ": " + token.getLexema());
            proxToken();
        }else erro(token);
    }

    private void Fpar(No bloco){
        if(token.getNome() == nome.FPAR){
            bloco.acrescentarFilho(token.getNome() + ": " + token.getLexema());
            proxToken();
        }else erro(token);
    }

    private void Ptv(No bloco){
        if(token.getNome() == nome.PTV){
            bloco.acrescentarFilho(token.getNome() + ": " + token.getLexema());
            proxToken();
        }else erro(token);
    }

    private void declaracao(No decl){
        No declaracao = decl.acrescentarFilho("declaracao");

        if(tipo()){
            tipo = false;
            No tipo = declaracao.acrescentarFilho("tipo");
            tipo.acrescentarFilho(token.getNome() + ": " + token.getLexema());
            proxToken();
        }else erro(token);

        if(variavel()){
            variavel = false;
            No variavel = declaracao.acrescentarFilho("variavel");
            variavel.acrescentarFilho(token.getNome() + ": " + token.getLexema());
            proxToken();
        }else erro(token);

        if(token.getNome() == nome.ACOL){
            declaracao.acrescentarFilho(token.getNome() + ": " + token.getLexema());
            proxToken();

            if(token.getNome() == nome.NUM){
                declaracao.acrescentarFilho(token.getNome() + ": " + token.getLexema());
                proxToken();

                if(token.getNome() == nome.FCOL){
                    declaracao.acrescentarFilho(token.getNome() + ": " + token.getLexema());
                    proxToken();
                }else erro(token);
            }else erro(token);
        }

        if(token.getNome() == nome.PTV){
            declaracao.acrescentarFilho(token.getNome() + ": " + token.getLexema());
            proxToken();

        }else if(token.getNome() == nome.ATRIB){
            declaracao.acrescentarFilho(token.getNome() + ": " + token.getLexema());
            proxToken();

            if(token.getNome() == nome.VERDADEIRO || token.getNome() == nome.FALSO){
                declaracao.acrescentarFilho(token.getNome() + ": " + token.getLexema());
                proxToken();

                if(token.getNome() == nome.PTV){
                    declaracao.acrescentarFilho(token.getNome() + ": " + token.getLexema());
                    proxToken();

                }else erro(token);
            }else if(token.getNome() == nome.ARRAYC){
                declaracao.acrescentarFilho(token.getNome() + ": " + token.getLexema());
                proxToken();

                if(token.getNome() == nome.PTV){
                    declaracao.acrescentarFilho(token.getNome() + ": " + token.getLexema());
                    proxToken();

                }else erro(token);
            }else if(token.getNome() == nome.ELCARAC){
                declaracao.acrescentarFilho(token.getNome() + ": " + token.getLexema());
                proxToken();

                if(token.getNome() == nome.PTV){
                    declaracao.acrescentarFilho(token.getNome() + ": " + token.getLexema());
                    proxToken();

                }else erro(token);
            }else if(token.getNome() == nome.ID){
                Token tokenAnt = token;

                proxToken();

                if(token.getNome() == nome.APAR){
                    No nomeMetodo = declaracao.acrescentarFilho("nomeMetodo");
                    nomeMetodo.acrescentarFilho(tokenAnt.getNome() + ": " + tokenAnt.getLexema());

                    declaracao.acrescentarFilho(token.getNome() + ": " + token.getLexema());
                    proxToken();

                    chamadaMetodo(declaracao);
                }else{
                    No variavel = declaracao.acrescentarFilho("termo");
                    variavel.acrescentarFilho(tokenAnt.getNome() + ": " + tokenAnt.getLexema());

                    expressao(declaracao);

                    if(token.getNome() == nome.PTV){
                        declaracao.acrescentarFilho(token.getNome() + ": " + token.getLexema());
                        proxToken();
                    }else erro(token);
                }
            }else{
                expressao(declaracao);

                if(token.getNome() == nome.PTV){
                    declaracao.acrescentarFilho(token.getNome() + ": " + token.getLexema());
                    proxToken();
                }else erro(token);
            }
        }else erro(token);
    }

    private void instrucao(No bloco){
        if(token.getNome() == nome.SE){
            No selecao = bloco.acrescentarFilho("selecao");
            selecao.acrescentarFilho(token.getNome() + ": " + token.getLexema());
            proxToken();

            Apar(selecao);
            condicao(selecao);

            Fpar(selecao);

            if(token.getNome() == nome.ENTAO){
                selecao.acrescentarFilho(token.getNome() + ": " + token.getLexema());
                proxToken();

                if(token.getNome() == nome.ACHAV){
                    selecao.acrescentarFilho(token.getNome() + ": " + token.getLexema());
                    proxToken();

                    No blocoSelecao = selecao.acrescentarFilho("bloco");
                    bloco(blocoSelecao);

                    if(token.getNome() == nome.FCHAV){
                        selecao.acrescentarFilho(token.getNome() + ": " + token.getLexema());
                        proxToken();

                        while(token.getNome() == nome.SENAO){
                            selecao.acrescentarFilho(token.getNome() + ": " + token.getLexema());
                            proxToken();

                            if(token.getNome() == nome.ACHAV){
                                selecao.acrescentarFilho(token.getNome() + ": " + token.getLexema());
                                proxToken();

                                blocoSelecao = selecao.acrescentarFilho("bloco");
                                bloco(blocoSelecao);

                                if(token.getNome() == nome.FCHAV){
                                    selecao.acrescentarFilho(token.getNome() + ": " + token.getLexema());
                                    proxToken();
                                }else erro(token);
                            }else if(token.getNome() == nome.SE){
                                selecao.acrescentarFilho(token.getNome() + ": " + token.getLexema());
                                proxToken();

                                Apar(selecao);
                                condicao(selecao);

                                Fpar(selecao);

                                if(token.getNome() == nome.ENTAO){
                                    selecao.acrescentarFilho(token.getNome() + ": " + token.getLexema());
                                    proxToken();

                                    if(token.getNome() == nome.ACHAV){
                                        selecao.acrescentarFilho(token.getNome() + ": " + token.getLexema());
                                        proxToken();

                                        blocoSelecao = selecao.acrescentarFilho("bloco");
                                        bloco(blocoSelecao);

                                        if(token.getNome() == nome.FCHAV){
                                            selecao.acrescentarFilho(token.getNome() + ": " + token.getLexema());
                                            proxToken();
                                        }else erro(token);
                                    }else erro(token);
                                }else erro(token);
                            }
                        }
                    }else erro(token);
                }else erro(token);
            }else erro(token);
        }
    }

    private void condicao(No cond){
        No condicao = cond.acrescentarFilho("condicao");
        expressao(condicao);

        if(token.getNome() == nome.IGUAL || token.getNome() == nome.MENOR || token.getNome() == nome.MAIOR || token.getNome() == nome.MAIORIGUAL || token.getNome() == nome.MENORIGUAL){
            condicao.acrescentarFilho(token.getNome() + ": " + token.getLexema());
            proxToken();

            expressao(condicao);
        }
    }

    private void expressao(No exp){
        No expressao = exp.acrescentarFilho("expressao");
        termo(expressao);

        while(token.getNome() == nome.MAIS || token.getNome() == nome.MENOS || token.getNome() == nome.MULT){
            expressao.acrescentarFilho(token.getNome() + ": " + token.getLexema());
            proxToken();

            termo(expressao);
        }
    }

    private void termo(No term){
        if(token.getNome() == nome.ID){
            No termo = term.acrescentarFilho("termo");
            termo.acrescentarFilho(token.getNome() + ": " + token.getLexema());
            proxToken();
        }else if(token.getNome() == nome.NUM){
            No termo = term.acrescentarFilho("termo");
            termo.acrescentarFilho(token.getNome() + ": " + token.getLexema());
            proxToken();

            if(token.getNome() == nome.PTO){
                termo.acrescentarFilho(token.getNome() + ": " + token.getLexema());
                proxToken();

                if(token.getNome() == nome.NUM){
                    termo.acrescentarFilho(token.getNome() + ": " + token.getLexema());
                    proxToken();
                }
            }
        }else if(token.getNome() == nome.APAR){
            term.acrescentarFilho(token.getNome() + ": " + token.getLexema());
            proxToken();

            expressao(term);

            if(token.getNome() == nome.FPAR){
                term.acrescentarFilho(token.getNome() + ": " + token.getLexema());
                proxToken();
            }else erro(token);
        }
    }

    private void repeticaoEnquanto(No repete){
        if(token.getNome() == nome.ENQUANTO){
            No repeticao = repete.acrescentarFilho("repeticao");
            repeticao.acrescentarFilho(token.getNome() + ": " + token.getLexema());
            proxToken();

            if(token.getNome() == nome.APAR){
                repeticao.acrescentarFilho(token.getNome() + ": " + token.getLexema());
                proxToken();

                condicao(repeticao);

                if(token.getNome() == nome.FPAR){
                    repeticao.acrescentarFilho(token.getNome() + ": " + token.getLexema());
                    proxToken();

                    if(token.getNome() == nome.ACHAV){
                        repeticao.acrescentarFilho(token.getNome() + ": " + token.getLexema());
                        proxToken();

                        No blocoRepeticao = repeticao.acrescentarFilho("bloco");
                        bloco(blocoRepeticao);

                        if(token.getNome() == nome.FCHAV){
                            repeticao.acrescentarFilho(token.getNome() + ": " + token.getLexema());
                            proxToken();
                        }else erro(token);
                    }else erro(token);
                }else erro(token);
            }else erro(token);
        }
    }

    private void repeticaoRepitaAte(No repete){
        if(token.getNome() == nome.REPITA){
            No repeticao = repete.acrescentarFilho("repeticao");
            repeticao.acrescentarFilho(token.getNome() + ": " + token.getLexema());
            proxToken();

            if(token.getNome() == nome.ACHAV){
                repeticao.acrescentarFilho(token.getNome() + ": " + token.getLexema());
                proxToken();

                No blocoRepeticao = repeticao.acrescentarFilho("bloco");
                bloco(blocoRepeticao);

                if(token.getNome() == nome.FCHAV){
                    repeticao.acrescentarFilho(token.getNome() + ": " + token.getLexema());
                    proxToken();

                    if(token.getNome() == nome.ATE){
                        repeticao.acrescentarFilho(token.getNome() + ": " + token.getLexema());
                        proxToken();

                        if(token.getNome() == nome.QUE){
                            repeticao.acrescentarFilho(token.getNome() + ": " + token.getLexema());
                            proxToken();

                            if(token.getNome() == nome.APAR){
                                repeticao.acrescentarFilho(token.getNome() + ": " + token.getLexema());
                                proxToken();

                                condicao(repeticao);

                                if(token.getNome() == nome.FPAR){
                                    repeticao.acrescentarFilho(token.getNome() + ": " + token.getLexema());
                                    proxToken();

                                    if(token.getNome() == nome.PTV){
                                        repeticao.acrescentarFilho(token.getNome() + ": " + token.getLexema());
                                        proxToken();

                                    }else erro(token);
                                }else erro(token);
                            }else erro(token);
                        }else erro(token);
                    }else erro(token);
                }else erro(token);
            }else erro(token);
        }
    }

    private void repeticaoRepitaPara(No repete){
        if(token.getNome() == nome.PARA){
            No repeticao = repete.acrescentarFilho("repeticao");
            repeticao.acrescentarFilho(token.getNome() + ": " + token.getLexema());
            proxToken();

            if(variavel()){
                variavel = false;

                No variavel = repeticao.acrescentarFilho("termo");
                variavel.acrescentarFilho(token.getNome() + ": " + token.getLexema());

                proxToken();
            }else erro(token);

            if(token.getNome() == nome.DE){
                repeticao.acrescentarFilho(token.getNome() + ": " + token.getLexema());
                proxToken();

                if(token.getNome() == nome.ID || token.getNome() == nome.NUM){
                    repeticao.acrescentarFilho(token.getNome() + ": " + token.getLexema());
                    proxToken();

                    if(token.getNome() == nome.ATE){
                        repeticao.acrescentarFilho(token.getNome() + ": " + token.getLexema());
                        proxToken();

                        if(token.getNome() == nome.ID || token.getNome() == nome.NUM){
                            No termo = repeticao.acrescentarFilho("termo");
                            termo.acrescentarFilho(token.getNome() + ": " + token.getLexema());
                            proxToken();

                            if(token.getNome() == nome.PASSO){
                                repeticao.acrescentarFilho(token.getNome() + ": " + token.getLexema());
                                proxToken();

                                if(token.getNome() == nome.NUM){
                                    termo = repeticao.acrescentarFilho("termo");
                                    termo.acrescentarFilho(token.getNome() + ": " + token.getLexema());
                                    proxToken();

                                    if(token.getNome() == nome.FACA){
                                        repeticao.acrescentarFilho(token.getNome() + ": " + token.getLexema());
                                        proxToken();

                                        if(token.getNome() == nome.ACHAV){
                                            repeticao.acrescentarFilho(token.getNome() + ": " + token.getLexema());
                                            proxToken();

                                            No blocoRepeticao = repeticao.acrescentarFilho("bloco");
                                            bloco(blocoRepeticao);

                                            if(token.getNome() == nome.FCHAV){
                                                repeticao.acrescentarFilho(token.getNome() + ": " + token.getLexema());
                                                proxToken();
                                            }else erro(token);
                                        }else erro(token);
                                    }else erro(token);
                                }else if(token.getNome() == nome.MENOS){
                                    No expressao = repeticao.acrescentarFilho("expressao");
                                    expressao.acrescentarFilho(token.getNome() + ": " + token.getLexema());
                                    proxToken();

                                    if(token.getNome() == nome.NUM){
                                        termo = expressao.acrescentarFilho("termo");
                                        termo.acrescentarFilho(token.getNome() + ": " + token.getLexema());
                                        proxToken();

                                        if(token.getNome() == nome.FACA){
                                            repeticao.acrescentarFilho(token.getNome() + ": " + token.getLexema());
                                            proxToken();

                                            if(token.getNome() == nome.ACHAV){
                                                repeticao.acrescentarFilho(token.getNome() + ": " + token.getLexema());
                                                proxToken();

                                                No blocoRepeticao = repeticao.acrescentarFilho("bloco");
                                                bloco(blocoRepeticao);

                                                if(token.getNome() == nome.FCHAV){
                                                    repeticao.acrescentarFilho(token.getNome() + ": " + token.getLexema());
                                                    proxToken();
                                                }else erro(token);
                                            }else erro(token);
                                        }else erro(token);
                                    }else erro(token);
                                }else erro(token);
                            }else erro(token);
                        }else erro(token);
                    }else erro(token);
                }else erro(token);
            }else erro(token);
        }
    }

    private boolean tipo(){
        tipo = false;

        if(token.getNome() == nome.INT || token.getNome() == nome.BOOL || token.getNome() == nome.DEC || token.getNome() == nome.CARAC){
            tipo = true;
        }

        return tipo;
    }

    private void nomeMetodo(No bloco){
        if(token.getNome() == nome.ID){
            bloco.acrescentarFilho(token.getNome() + ": " + token.getLexema());
            proxToken();
        }else erro(token);
    }

    private boolean variavel(){
        variavel = false;

        if(token.getNome() == nome.ID){
            variavel = true;
        }     

        return variavel;
    } 

    private void fimSintatica(){
        if(countPrinc == 1){
            System.out.println("Analise Sintatica Executada com Sucesso, sem detecção de erros!!!");
            no.imprimirArvore();
        }else if(countPrinc > 1){
            System.out.println("O programa possui mais de um procedimento principal!!!");
        }else if(countPrinc == 0){
            System.out.println("Nao ha um procedimento principal!!!");
        }
    }

    private void proxToken(){
        if(i < limite) i++;
        token = Tokens.get(i);
    }

    private void erro(Token token){
        System.out.println("Erro Sintatico no Token: " + Tokens.get(i));
        System.exit(0);
    }
}
