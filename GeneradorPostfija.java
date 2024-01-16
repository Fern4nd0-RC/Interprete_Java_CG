package CompiladorChelero;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class GeneradorPostfija {

    private final List<Token> infija;
    private final Stack<Token> pila;
    private final List<Token> postfija;

    public GeneradorPostfija(List<Token> infija) {
        this.infija = infija;
        this.pila = new Stack<>();
        this.postfija = new ArrayList<>();
    }

    public List<Token> convertir(){
        boolean estructuraDeControl = false;
        Stack<Token> pilaEstructurasDeControl = new Stack<>();

        for(int i=0; i<infija.size(); i++){
            Token t = infija.get(i);

            if(t.tipo == TipoToken.EOF){
                break;
            }

            if(t.esPalabraReservada()){
               
                postfija.add(t);
                if (t.esEstructuradeControl()){
                    estructuraDeControl = true;
                    pilaEstructurasDeControl.push(t);
                }
            }
            else if(t.esOperando()){
                postfija.add(t);
            }
            else if(t.tipo == TipoToken.LEFT_PAREN){
                pila.push(t);
            }
            else if(t.tipo == TipoToken.RIGHT_PAREN){
                while(!pila.isEmpty() && pila.peek().tipo != TipoToken.LEFT_PAREN){
                    Token temp = pila.pop();
                    postfija.add(temp);
                }
                if(estructuraDeControl){
                    postfija.add(new Token(TipoToken.SEMICOLON, ";"));
                }
                if(!pila.isEmpty() && pila.peek().tipo == TipoToken.LEFT_PAREN){

                    pila.pop();
                }

            }
            else if(t.esOperador()){
                while(!pila.isEmpty() && pila.peek().PrecedenciaMayorIgual(t)){
                    Token temp = pila.pop();
                    postfija.add(temp);
                }
                pila.push(t);
            }
            else if(t.tipo == TipoToken.SEMICOLON){
                while(!pila.isEmpty() && pila.peek().tipo != TipoToken.LEFT_BRACE){
                    Token temp = pila.pop();
                    postfija.add(temp);
                }
                postfija.add(t);
            }
            else if(t.tipo == TipoToken.LEFT_BRACE){
             
                pila.push(t);
            }
            else if(t.tipo == TipoToken.RIGHT_BRACE && estructuraDeControl){


                if(infija.get(i + 1).tipo == TipoToken.ELSE){

                    pila.pop();
                }
                else{
                   
                    pila.pop();
                    postfija.add(new Token(TipoToken.SEMICOLON, ";"));

                    
                    pilaEstructurasDeControl.pop();
                    if(pilaEstructurasDeControl.isEmpty()){
                        estructuraDeControl = false;
                    }
                }


            }
        }
        while(!pila.isEmpty()){
            Token temp = pila.pop();
            postfija.add(temp);
        }

        while(!pilaEstructurasDeControl.isEmpty()){
            pilaEstructurasDeControl.pop();
            postfija.add(new Token(TipoToken.SEMICOLON, ";"));
        }

        return postfija;
    }

}