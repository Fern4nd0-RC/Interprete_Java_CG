package CompiladorChelero;

public class Token {

    final TipoToken tipo;
    final String lexema;
    final Object literal;
    final int linea;

    public Token(TipoToken tipo, String lexema, Object literal, int linea) {
        this.tipo = tipo;
        this.lexema = lexema;
        this.linea = linea;
        if (tipo == TipoToken.TRUE){
            this.literal = true;
        } else if (tipo == TipoToken.FALSE){
            this.literal = false;
        } else {
            this.literal = literal;
        }
    }
    
    public Token (TipoToken tipo, String lexema){
        this.tipo = tipo;
        this.lexema = lexema;
        this.literal = null;
        this.linea = 0;
        
    }
    
    public TipoToken getTipo() {
        return tipo;
    }
   
    //Metodos Auxiliares
    public boolean esOperando(){
        switch (this.tipo){
            case IDENTIFIER:
            case NUMBER:
            case STRING:
            case TRUE:
            case FALSE:
                return true;
            default:
                return false;
        }
    }

    public boolean esOperador(){
        switch (this.tipo){
            case PLUS:
            case MINUS:
            case STAR:
            case SLASH:
            case BANG, BANG_EQUAL,
                EQUAL, EQUAL_EQUAL,
                GREATER, GREATER_EQUAL,
                LESS, LESS_EQUAL:
            case OR:
            case AND:
                return true;
            default:
                return false;
        }
    }

    public boolean esPalabraReservada(){
        switch (this.tipo){
            case PRINT:
            case RETURN:
            case VARIABLE:
            case NULL:
            case FUN:
            case CLASS:
            case IF:
            case ELSE:
            case WHILE:
            case FOR:
                return true;
            default:
                return false;
        }
    }

    public boolean esEstructuradeControl(){
        switch (this.tipo){
            case IF:
            case ELSE:
            case WHILE:
            case FOR:
                return true;
            default:
                return false;
        }
    }

    public boolean PrecedenciaMayorIgual(Token token){
        return this.ObtenerPrecedencia() >= token.ObtenerPrecedencia();
    }

    private int ObtenerPrecedencia(){
        switch (this.tipo){
            case PLUS:
            case MINUS:
                return 6;
            case STAR:
            case SLASH:
                return 7;
            case LESS:
            case GREATER:
            case LESS_EQUAL:
            case GREATER_EQUAL:
                return 5;
            case EQUAL_EQUAL:
            case BANG_EQUAL:
                return 4;
            case EQUAL:
                return 1;

            case OR:
                return 2;
            case AND:
                return 3;
        }
        return 0;
    }

    public int Aridad(){
        switch (this.tipo){
            case PLUS:
            case MINUS:
            case STAR:
            case SLASH:
            case OR:
            case AND:
            case BANG, BANG_EQUAL,
                EQUAL, EQUAL_EQUAL,
                GREATER, GREATER_EQUAL,
                LESS, LESS_EQUAL:
                return 2;
        }
        return 0;
    }

    @Override
    public boolean equals(Object o){
        if(!(o instanceof Token)) return false;
        if(this.tipo == ((Token)o).tipo) return true;
        return false;
    }

    public String toString(){
        return tipo + " " + lexema + " " + literal;
    }
}
