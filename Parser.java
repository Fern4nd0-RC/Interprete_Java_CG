package CompiladorChelero;

import java.util.List;


public class Parser {
    private final List <Token> tokens;


    //Revisar y/o corregir
    private int i = 0;
    private boolean error = false;
    private Token preanalisis;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }
   public void parse() {
        i = 0;
        preanalisis = tokens.get(i);
        //Funcion a la primera cadena de produccion
        Declaration();
        if (error == true && !(preanalisis.getTipo()==TipoToken.EOF)) {
            System.out.println("Error: en la posicion " + preanalisis.linea + ". No se esperaba el token " + preanalisis.tipo);
        } else if (error == false  && (preanalisis.getTipo()==TipoToken.EOF)) {
            System.out.println("Analisis sintactico exitoso");
            System.out.println(" ");
        }
    }
   
 void Declaration(){
       if (error) return;
        switch(preanalisis.getTipo()){
            case CLASS:
                Class_dec();
                Declaration();
            case FUN:
                Func_dec();
                Declaration();
            case VAR:
                Var_dec();
                Declaration();
            
            case FOR, IF, PRINT, RETURN, WHILE,LEFT_BRACE,TRUE,FALSE,NULL,NUMBER,STRING,IDENTIFIER:
                Statement();
                Declaration();
            

        } 
       

    }
    void Class_dec(){
        switch(preanalisis.getTipo()){
            case CLASS: 
                match(TipoToken.CLASS);
                match(TipoToken.IDENTIFIER);
                Class_inher();
                match(TipoToken.LEFT_BRACE);
                Funtions();
                match(TipoToken.RIGHT_BRACE);
            
            default:
                error = true;
                System.out.println("Error: en la Posicion " + preanalisis.linea + ". Se Esperaba un CLASE.");
                
        }
        
    }

    void Class_inher(){
        if(error) return;
        if (preanalisis.getTipo() == TipoToken.LESS){
            match(TipoToken.LESS);
            match(TipoToken.IDENTIFIER);
        }
        
    }
    
    void Func_dec(){
        switch(preanalisis.getTipo()){
            case FUN:
                match(TipoToken.FUN);
                Function();
                
            default:
                error = true;
                System.out.println("Error: en la Posicion " + preanalisis.linea + ". Se Esperaba un FUN.");
        }
        
    }
    
    void Var_dec(){
        
        switch(preanalisis.getTipo()){
            case VAR:
                match(TipoToken.VAR);
                match(TipoToken.IDENTIFIER);
                Var_init();
                match(TipoToken.SEMICOLON);
            default:
                error = true;
                System.out.println("Error: en la Posicion " + preanalisis.linea + ". Se Esperaba un VAR.");
        }
         
        
    }
    void Var_init(){
        if(error) return;

        if (preanalisis.getTipo() == TipoToken.EQUAL){
            match(TipoToken.EQUAL);
            Expr();
        }
    }
    
    void Statement(){
        switch (preanalisis.getTipo()) {
        case FOR :
            For_state();
        case IF :
            If_state();
        case PRINT : 
            Print_state();
        case RETURN : 
            Return_state();
        case WHILE : 
            While_state();
        case LEFT_BRACE : 
            Block();
        case TRUE, FALSE, NULL, NUMBER, STRING, IDENTIFIER : 
            Expr_State();
        default : 
            error = true;
            System.out.println("Error: en la Posicion " + preanalisis.linea + ". Se Esperaba un .");
       
        
    }
    }
    void Expr_State(){

        if (error) return;

        Expr();
        match(TipoToken.SEMICOLON);


    }
    
    void For_state(){
        switch(preanalisis.getTipo()){
            case FOR:
                match(TipoToken.FOR);
                match(TipoToken.LEFT_PAREN);
                For_state1();
                For_state2();
                For_state3();
                match(TipoToken.RIGHT_PAREN);
                Statement();
            default:
                error = true;
                System.out.println("Error: en la Posicion " + preanalisis.linea + ". Se Esperaba un FOR.");
        
        } 

        
    }
    
    void For_state1(){
        switch(preanalisis.getTipo()){
            case VAR:
                Var_dec();
            case TRUE,FALSE,NULL,NUMBER,STRING,IDENTIFIER:
                Expr_State();
            case SEMICOLON:
                match(TipoToken.SEMICOLON);
            default:
                error = true;
                System.out.println("Error: en la Posicion " + preanalisis.linea + ". Se Esperaba una Variable o una Expresion o ' ; '.");   
        } 

    }
    
    
    void For_state2(){
        switch(preanalisis.getTipo()){           
            case TRUE,FALSE,NULL,NUMBER,STRING,IDENTIFIER:
                Expr();
                match(TipoToken.SEMICOLON);
            
            case SEMICOLON:
                match(TipoToken.SEMICOLON);
            default:
                error = true;
                System.out.println("Error: en la Posicion " + preanalisis.linea + ". Se Esperaba un expresion o un ' ; '.");
        
        } 


    }
    
    
    void For_state3(){
        if (error) return;

       if(preanalisis.getTipo() == TipoToken.TRUE || preanalisis.getTipo() == TipoToken.FALSE || preanalisis.getTipo() == TipoToken.NULL || preanalisis.getTipo() == TipoToken.NUMBER ||
                    preanalisis.getTipo() == TipoToken.STRING || preanalisis.getTipo() == TipoToken.IDENTIFIER 
                ){
           Expr();

        }
    }
    void If_state(){
        
        switch(preanalisis.getTipo()){
            case IF:
                match(TipoToken.IF);
                match(TipoToken.LEFT_PAREN);
                Expr();
                match(TipoToken.RIGHT_PAREN);
                Statement();
                Else_state();
                
            default:
                error = true;
                System.out.println("Error: en la Posicion " + preanalisis.linea + ". Se Esperaba un SI.");
        }

    }
    void Else_state(){
        if(error) return;
        
        if (preanalisis.getTipo() == TipoToken.ELSE){
            match(TipoToken.ELSE);
            Statement();
        }
        
    }
    
    void Print_state(){
        switch(preanalisis.getTipo()){
            case PRINT:
                match(TipoToken.PRINT);
                Expr();
                match(TipoToken.SEMICOLON);
            default:
                error = true;
                System.out.println("Error: en la Posicion " + preanalisis.linea + ". Se Esperaba un IMPRIMIR.");
        
        } 
   
    }
    
    void Return_state(){
        
        switch(preanalisis.getTipo()){
            case RETURN:
                match(TipoToken.RETURN);
                Return_exp_opc();
                match(TipoToken.SEMICOLON);
            default:
                error = true;
            System.out.println("Error: en la Posicion " + preanalisis.linea + ". Se Esperaba un RETORNAR.");
        } 
        
    }
    
    void Return_exp_opc(){
        if(error) return;
        
        if(preanalisis.getTipo() == TipoToken.TRUE || preanalisis.getTipo() == TipoToken.FALSE || preanalisis.getTipo() == TipoToken.NULL || preanalisis.getTipo() == TipoToken.NUMBER ||
                    preanalisis.getTipo() == TipoToken.STRING || preanalisis.getTipo() == TipoToken.IDENTIFIER 
                ){
            
            Expr();
            
        }
    }
    void While_state(){
        
         switch(preanalisis.getTipo()){
             case WHILE:
                 match(TipoToken.WHILE);
                 match(TipoToken.LEFT_PAREN);
                 Expr();
                 match(TipoToken.RIGHT_PAREN);
                 Statement();
            default:
                error = true;
                System.out.println("Error: en la Posicion " + preanalisis.linea + ". Se Esperaba un MIENTRAS.");

        
    }
    }
    void Block(){
        
        switch (preanalisis.getTipo()) {
        
            case LEFT_BRACE:
                match(TipoToken.LEFT_BRACE);
                Block_dec();
                match(TipoToken.RIGHT_BRACE);
            
            default:
                error = true;
                System.out.println("Error: en la Posicion " + preanalisis.linea + ". Se Esperaba un '{'.");
        }
        
      
    }
    
void Block_dec(){
        if(error) return;
        if(preanalisis.getTipo() == TipoToken.CLASS || preanalisis.getTipo() == TipoToken.FUN || preanalisis.getTipo() == TipoToken.TRUE ||
                preanalisis.getTipo() == TipoToken.FALSE || preanalisis.getTipo() == TipoToken.NULL || preanalisis.getTipo() == TipoToken.NUMBER ||
                    preanalisis.getTipo() == TipoToken.STRING || preanalisis.getTipo() == TipoToken.IDENTIFIER || preanalisis.getTipo() == TipoToken.VAR ||
                        preanalisis.getTipo() == TipoToken.PRINT || preanalisis.getTipo() == TipoToken.IF || preanalisis.getTipo() == TipoToken.WHILE || 
                            preanalisis.getTipo() == TipoToken.RETURN || preanalisis.getTipo() == TipoToken.LEFT_BRACE
                ){
            Declaration();
            Block_dec();
        }
    }


    void Function()
    {
         switch(preanalisis.getTipo()){
             case IDENTIFIER:
                 match(TipoToken.IDENTIFIER);
                 match(TipoToken.LEFT_PAREN);
                 Param_opc();
                 match(TipoToken.RIGHT_PAREN);
                 Block();
             default:
                error = true;
                System.out.println("Error: en la Posicion " + preanalisis.linea + ". Se Esperaba un IDENTIFICADOR.");
        
        } 
       

    }


    void Funtions()
    {
        if(error) return;
        if (preanalisis.getTipo() == TipoToken.IDENTIFIER){
            Function();
            Funtions();
        }
    }

    void Param_opc()
    {
        if(error) return;
        if (preanalisis.getTipo() == TipoToken.IDENTIFIER){
            Params_1();
        }

    }

    void Params_1()
    {
        
        switch(preanalisis.getTipo()){
            case IDENTIFIER:
                match(TipoToken.IDENTIFIER);
                Params_2();
            default:
                error = true;
                System.out.println("Error: en la Posicion " + preanalisis.linea + ". Se Esperaba un IDENTIFICADOR.");

        } 

    }

    void Params_2()
    {
        if(error) return;
        if (preanalisis.getTipo() == TipoToken.COMMA){

            match(TipoToken.COMMA);
            match(TipoToken.IDENTIFIER);
            Params_2();
        }

    }


    void Arguments_OPC()
    {
        if(error) return;
        if (preanalisis.getTipo() == TipoToken.LEFT_PAREN){
            Arguments_1();
        }

    }

    void Arguments_1()
    {

        if (error) return;
        Expr();
        Arguments_2();

    }

    void Arguments_2()
    {
        if(error) return;
         switch(preanalisis.getTipo()){
             case COMMA:
                 match(TipoToken.COMMA);
                 Expr();
                Arguments_2();
             default:
        } 

    }

    
//--------------
    
    void Expr()
    {

        if(error) return;

        Assignment();

    }

    void Assignment()
    {

        if(error) return;

        Logic_OR();

        Assignment_OPC();

    }

    void Logic_OR() {
        if(error) return;

        Logic_AND();
        Logic_OR_2();

    }

    void Logic_OR_2()
    {

        if(error) return;

        if (preanalisis.getTipo() == TipoToken.OR){
            match(TipoToken.OR);
            Logic_AND();
            Logic_OR_2();
        }

    }

    void Assignment_OPC()
    {
        if(error) return;

        if (preanalisis.getTipo() == TipoToken.EQUAL_EQUAL){
            match(TipoToken.EQUAL_EQUAL);
            Expr();
        }
    }

    void Logic_AND()
    {
        if(error) return;
        Equality();
        Logic_AND_2();
    }

    
    void Logic_AND_2()
    {

        if(error) return;

        if (preanalisis.getTipo() == TipoToken.AND){
            match(TipoToken.AND);

            Equality();

            Logic_AND_2();

        }

    }
    

    void Equality()
    {
        if(error) return;
        Compare();
        Equality_2();
    }


    
    void Equality_2(){

        if(error) return;
        
        switch(preanalisis.getTipo()){
            case BANG_EQUAL:
                match(TipoToken.BANG_EQUAL);
                Compare();
                Equality_2();
            
            case EQUAL_EQUAL:
                match(TipoToken.EQUAL_EQUAL);
                Compare();
                Equality_2();
        } 

    }


 
     void Compare()
    {
         switch(preanalisis.getTipo()){
            case TRUE,FALSE,NULL,NUMBER,STRING,IDENTIFIER:
                Term();
                Compare_2();
            default:
                error = true;
                System.out.println("Error: en la Posicion " + preanalisis.linea + ". Se Esperaba un \t'TRUE' , 'FALSE' , 'NULL' , algun 'NUMBER' , alguna 'STRING' o un 'IDENTIFICADOR' ");
                }   
         
    }


    
    
    
     void Compare_2()
    {
        if(error) return;
        
        switch(preanalisis.getTipo()){
            case GREATER:
                match(TipoToken.GREATER);
                Term();
                Compare_2();
                
            case GREATER_EQUAL:
                match(TipoToken.GREATER_EQUAL);
                Term();
                Compare_2();
                
            case LESS:
                match(TipoToken.LESS);
                Term();
                Compare_2();
            
            case LESS_EQUAL:    
                match(TipoToken.LESS_EQUAL);
                Term();
                Compare_2();

                
        } 
        

    }
    
    
    
    


   
    void Term()
    {
        switch(preanalisis.getTipo()){
            
            case TRUE,FALSE,NULL,NUMBER,STRING,IDENTIFIER:
                Factor();
                Term_2();
                
            default:
                error = true;
                System.out.println("Error: en la Posicion " + preanalisis.linea + ". Se Esperaba un \t'TRUE' , 'FALSE' , 'NULL' , algun 'NUMBER' , alguna 'STRING' o un 'IDENTIFICADOR' ");
        
        }
        
        

    }

  
    
    void Term_2()
    {
        
        if (error) return;
        switch(preanalisis.getTipo()){
            case MINUS: 
                match(TipoToken.MINUS);
                Factor();
                Term_2();
            case PLUS:
                match(TipoToken.PLUS);
                Factor();
                Term_2();
        }
        
    }


    
    void Factor()
    {
         switch(preanalisis.getTipo()){
            case TRUE,FALSE,NULL,NUMBER,STRING,IDENTIFIER:
                Unary();
                Factor_2();
                 
        }

    }


    void Factor_2()
    {
        if(error) return;
        switch(preanalisis.getTipo()){
            case SLASH:
                match(TipoToken.SLASH);
                Unary();
                Factor_2();
            case STAR:
                match(TipoToken.STAR);
                Unary();
                Factor_2();
                
        }

    }
       
    void Unary()
    {
        
        switch(preanalisis.getTipo()){
            case TRUE,FALSE,NULL,NUMBER,STRING,IDENTIFIER:
                Call();
            case BANG:
                match(TipoToken.BANG);
                Unary();
            case MINUS:
                 match(TipoToken.MINUS);
                 Unary();
            default: break;
                 
        }

    }

  
    void Call()
    {
//Espero más cosas y error
        
        switch(preanalisis.getTipo()){
            case TRUE,FALSE,NULL,NUMBER,STRING,IDENTIFIER:
                Primary();
                Call_2();
            default:
                error = true;
                System.out.println("Error: en la Posicion " + preanalisis.linea + ". Se Esperaba un \tTRUE || FALSE || NULL || NUMBER || STRING || IDENTIFIER .");
     
                
        }
        

    }

    void Call_2()
    {
        
        if(error) return;
        
        switch(preanalisis.getTipo()){
            case LEFT_BRACE:
                match(TipoToken.LEFT_PAREN);
                Arguments_OPC();
                match(TipoToken.RIGHT_PAREN);
                Call_2();
            
            case DOT:
                match(TipoToken.DOT);
                match(TipoToken.IDENTIFIER);
                Call_2();
                
                
        }
        
    }


 void Primary()
    {
    switch(preanalisis.getTipo()){
        case TRUE: match(TipoToken.TRUE);
        case FALSE: match(TipoToken.FALSE);
        case NULL:  match(TipoToken.NULL);
        case NUMBER:  match(TipoToken.NUMBER);
        case STRING:  match(TipoToken.STRING);
        case IDENTIFIER:  match(TipoToken.IDENTIFIER);
        case LEFT_PAREN:  
            match(TipoToken.LEFT_PAREN);
            Expr();
            match(TipoToken.RIGHT_PAREN);
            
        default: 
            error = true;
            System.out.println("Error: en la Posicion " + preanalisis.linea + ". Se Esperaba otra cosa");

    }
    }

       private void match(TipoToken tt) throws ParserException {
        if(preanalisis.getTipo() ==  tt){
            i++;
            preanalisis = tokens.get(i);
        }
        else{
            String message = "Error en la línea " +
                    preanalisis.linea +
                    ". Se esperaba " + preanalisis.getTipo() +
                    " pero se encontró " + tt;
            throw new ParserException(message);
        }
    }
}
