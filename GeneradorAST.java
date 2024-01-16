package CompiladorChelero;

import java.util.List;
import java.util.Stack;

public class GeneradorAST {

    private final List<Token> postfija;
    private final Stack<Nodo> pila;

    public GeneradorAST(List<Token> postfija){
        this.postfija = postfija;
        this.pila = new Stack<>();
    }

    public ArbolOP generarAST() {
        Stack<Nodo> pilaPadres = new Stack<>();
        Nodo raiz = new Nodo(null);
        pilaPadres.push(raiz);

        Nodo padre = raiz;

        for(Token t : postfija){
            if(t.tipo == TipoToken.EOF){
                break;
            }

            if(t.esPalabraReservada()){
                Nodo n = new Nodo(t);

                padre = pilaPadres.peek();
                padre.insertarSiguienteHijo(n);

                pilaPadres.push(n);
                padre = n;

            }
            else if(t.esOperando()){
                Nodo n = new Nodo(t);
                pila.push(n);
            }
            else if(t.esOperador()){
                int aridad = t.Aridad();
                Nodo n = new Nodo(t);
                for(int i=1; i<=aridad; i++){
                    Nodo nodoAux = pila.pop();
                    n.insertarHijo(nodoAux);
                }
                pila.push(n);
            }
            else if(t.tipo == TipoToken.SEMICOLON){

                if (pila.isEmpty()){
                    
                    pilaPadres.pop();
                    padre = pilaPadres.peek();
                }
                else{
                    Nodo n = pila.pop();

                    if(padre.getValue() != null && padre.getValue().tipo == TipoToken.VARIABLE){
                        
                        if(n.getValue().lexema == "="){
                            padre.insertarHijos(n.getHijos());
                        }
                        else{
                            padre.insertarSiguienteHijo(n);
                        }
                        pilaPadres.pop();
                        padre = pilaPadres.peek();
                    }
                    else if(padre.getValue() != null && padre.getValue().tipo == TipoToken.PRINT){
                        padre.insertarSiguienteHijo(n);
                        pilaPadres.pop();
                        padre = pilaPadres.peek();
                    }
                    else {
                        padre.insertarSiguienteHijo(n);
                    }
                }
            }
        }

        
        ArbolOP programa = new ArbolOP(raiz);

        return programa;
    }
}
