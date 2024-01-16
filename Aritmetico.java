package CompiladorChelero;

public class Aritmetico {
    private final Nodo nodo;
    private final TablaSimbolos tablaSimbolos;

    public Aritmetico(Nodo nodo, TablaSimbolos tablaSimbolos) {
        this.nodo = nodo;
        this.tablaSimbolos = tablaSimbolos;
    }

    public Object resolver() {
        return resolver(nodo);
    }

    public Object resolver(Nodo n) {
        if (n.getHijos() == null) {
            if (n.getValue().tipo == TipoToken.NUMBER) {
                return n.getValue().literal;
            } else if (n.getValue().tipo == TipoToken.STRING){
                return n.getValue().lexema;
            }else if (n.getValue().tipo == TipoToken.IDENTIFIER) {
                return tablaSimbolos.ObtenerValor(n.getValue().lexema);
            }
        }

        //Por simplicidad se asume que la lista de hijos del nodo tiene dos elementos
        Nodo izquierda = n.getHijos().get(0);
        Nodo derecha = n.getHijos().get(1);

        Object ResultadoIzquierda = resolver(izquierda);
        Object ResultadoDerecha = resolver(derecha);

        if (ResultadoIzquierda instanceof Double && ResultadoDerecha instanceof Double) {
            double valorIzquierda = (Double) ResultadoIzquierda;
            double valorDerecha = (Double) ResultadoDerecha;

            switch (n.getValue().tipo) {
                case PLUS:
                    return valorIzquierda + valorDerecha;
                case MINUS:
                    return valorIzquierda - valorDerecha;
                case STAR:
                    return valorIzquierda * valorDerecha;
                case SLASH:
                    if (valorDerecha != 0) {
                        return valorIzquierda / valorDerecha;
                    } else {
                        System.out.println("Error: División entre cero.");
                        return null;
                    }
                case LESS:
                    return (Boolean) (valorIzquierda < valorDerecha);
                case GREATER:
                    return (Boolean) (valorIzquierda > valorDerecha);
                case LESS_EQUAL:
                    return (Boolean) (valorIzquierda <= valorDerecha);
                case GREATER_EQUAL:
                    return (Boolean) (valorIzquierda >= valorDerecha);
                case EQUAL_EQUAL:
                    return (Boolean) (valorIzquierda == valorDerecha);
                case BANG_EQUAL:
                    return (Boolean) (valorIzquierda != valorDerecha);
                case EQUAL:
                    return (Double) (valorDerecha);
                
            }
        } else if (ResultadoIzquierda instanceof String && ResultadoDerecha instanceof String) {
            String valorIzquierda = (String) ResultadoIzquierda;
            String valorDerecha = (String) ResultadoDerecha;

            if (n.getValue().tipo == TipoToken.PLUS) {
                return valorIzquierda + valorDerecha;
            }
        } else if (ResultadoIzquierda instanceof Boolean && ResultadoDerecha instanceof Boolean){
            Boolean valorIzquierda = (Boolean) ResultadoIzquierda;
            Boolean valorDerecha = (Boolean) ResultadoDerecha;

            if (n.getValue().tipo == TipoToken.OR)
                return valorIzquierda || valorDerecha;
            if (n.getValue().tipo == TipoToken.AND)
                return valorIzquierda && valorDerecha;
        } else {
            System.out.println("Error semántico: Los operandos " + ResultadoIzquierda + " y " + ResultadoDerecha + " no son del mismo tipo.");
            return null;
        }
        return null;
    }
}
