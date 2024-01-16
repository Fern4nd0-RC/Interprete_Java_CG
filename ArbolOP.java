package CompiladorChelero;

import static CompiladorChelero.TipoToken.SLASH;
import static CompiladorChelero.TipoToken.STAR;
import static CompiladorChelero.TipoToken.OR;
import static CompiladorChelero.TipoToken.LESS;
import static CompiladorChelero.TipoToken.GREATER;
import static CompiladorChelero.TipoToken.LESS_EQUAL;
import static CompiladorChelero.TipoToken.GREATER_EQUAL;
import static CompiladorChelero.TipoToken.EQUAL_EQUAL;
import static CompiladorChelero.TipoToken.BANG_EQUAL;
import static CompiladorChelero.TipoToken.EQUAL;
import static CompiladorChelero.TipoToken.MINUS;
import static CompiladorChelero.TipoToken.PLUS;
import static CompiladorChelero.TipoToken.AND;


public class ArbolOP {
    private final Nodo raiz;
    private final TablaSimbolos tablaSimbolos;
    private Boolean Condicion;

    public ArbolOP(Nodo raiz){
        this.raiz = raiz;
        this.tablaSimbolos = new TablaSimbolos();
    }
    
    public void recorrer(){
        for(Nodo n : raiz.getHijos()){
            Token t = n.getValue();
            switch (t.tipo){
                
                case PLUS:
                case MINUS:
                case LESS:
                case EQUAL:
                case OR:
                    Aritmetico solver = new Aritmetico(n,this.tablaSimbolos);
                    Object res = solver.resolver();
                    System.out.println("Resultado de la operacion: "+ res);
                break;
                case VARIABLE:
                    
                    Nodo identificador = n.getHijos().get(0);

                    if (tablaSimbolos.ExisteIdentificador(identificador.getValue().lexema)){
                        System.out.println("Error: Variable Duplicada.");
                        throw new RuntimeException("Variable Ya Definida: " + identificador.getValue().lexema);
                    }

                    if(n.getHijos().size() == 1){
                        tablaSimbolos.Asignar(identificador.getValue().lexema, 0);
                    }else{
                        for (int i = 1; i < n.getHijos().size(); i++){
                            Nodo valor = n.getHijos().get(i);
                            if (valor.getValue().tipo == TipoToken.NUMBER){
                                tablaSimbolos.Asignar(identificador.getValue().lexema, valor.getValue().literal);
                            } else if (valor.getValue().tipo == TipoToken.STRING){
                                tablaSimbolos.Asignar(identificador.getValue().lexema, valor.getValue().lexema);
                            } else if (valor.getValue().tipo == TipoToken.IDENTIFIER){
                                tablaSimbolos.Asignar(identificador.getValue().lexema, tablaSimbolos.ObtenerValor(valor.getValue().lexema));
                            
                            } else if (valor.getValue().tipo == TipoToken.PLUS || valor.getValue().tipo == TipoToken.MINUS  || 
                                    valor.getValue().tipo == TipoToken.LESS ||
                                    valor.getValue().tipo == TipoToken.GREATER){
                                Aritmetico solverVariable = new Aritmetico(valor, this.tablaSimbolos);
                                Object resultadoVariable = solverVariable.resolver();
                                tablaSimbolos.Asignar(identificador.getValue().lexema, resultadoVariable);
                            } else {
                                throw new RuntimeException("Tipo de dato no válido: " + valor.getValue().lexema);
                            }
                        }
                    }
                    break;
                case IF:
                    Nodo condicion = n.getHijos().get(0);
                    Aritmetico solverSi = new Aritmetico(condicion, this.tablaSimbolos);
                    Object resultadoSi = solverSi.resolver();

                    if(!(resultadoSi instanceof Boolean)){
                        throw new RuntimeException("La condicion no es booleana: " + resultadoSi);
                    }

                    Condicion = (Boolean) resultadoSi;
                    if (Condicion){
                        Nodo bloque = n.getHijos().get(1);
                            switch (bloque.getValue().tipo){
                                case PRINT:
                                    for (Nodo hijo : bloque.getHijos()){
                                        Aritmetico solverImprimir = new Aritmetico(hijo, this.tablaSimbolos);
                                        Object resultado = solverImprimir.resolver();
                                        System.out.println("Resultado del imprimir: " + resultado);
                                    }
                                    break;
                            }
                    } else {
                        if (n.getHijos().size() == 3){
                            Nodo bloque = n.getHijos().get(2);
                            for (Nodo hijo : bloque.getHijos()){
                                switch (hijo.getValue().tipo){
                                    case PRINT:
                                        for (Nodo bijo : bloque.getHijos()){
                                            Nodo aux = bijo.getHijos().get(0);
                                            Aritmetico solverImprimir = new Aritmetico(aux, this.tablaSimbolos);
                                            Object resultado = solverImprimir.resolver();
                                            System.out.println("Resultado del imprimir: " + resultado);
                                        }
                                        break;
                                }
                                break;
                            }
                        }
                    }
                    break;

                case PRINT:
                    for (Nodo bijo : n.getHijos()){
                        Aritmetico solverImprimir = new Aritmetico(bijo, this.tablaSimbolos);
                        Object resultado = solverImprimir.resolver();
                        System.out.println("Resultado del imprimir: " + resultado);
                    }
                    break;

                }
            }
        }
    
    
    
    
    }
    
    



