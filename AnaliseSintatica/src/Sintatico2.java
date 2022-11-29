import java.util.ArrayList;

public class Sintatico2{
    private Lexico lexico;
    private Token token;

    public Sintatico2(Lexico lexico){
        this.lexico= lexico;
    }

    public void S(){
        this.token= this.lexico.nextToken();
        if(!token.getLexema().equals("int")){
            throw new RuntimeException("Faltou implementar o int");
        }
        this.token=this.lexico.nextToken();
        if(!token.getLexema().equals("main")){
            throw new RuntimeException("Faltou implementar o main");
        }
        this.token=this.lexico.nextToken();
        if(!token.getLexema().equals("(")){
            throw new RuntimeException("Você esqueceu de abrir ( ");
        }
        this.token= this.lexico.nextToken();
        if(!token.getLexema().equals(")")){
            throw new RuntimeException("Você esqueceu de fechar ) ");
        }
        this.token= this.lexico.nextToken();

        this.B();

        if(this.token != null && this.token.getTipo() == Token.TIPO_FIM_CODIGO){
            System.out.println("Compilação efetuada! @Equipe Dev - Valmir Júnior / Guilherme Belian");
        }else{
            throw new RuntimeException("Erro! próximo do fim do programa.");
        }
    }

    private void B(){
        if(!this.token.getLexema().equals("{")){
            throw new RuntimeException("Erro, esperava "+"{"+" / próximo de  "+this.token.getLexema());
        }
        this.token= this.lexico.nextToken();
        this.CS();
        if(!this.token.getLexema().equals("}")){
            throw new RuntimeException("Você esqueceu de fechar "+"}"+ " / próximo de "+this.token.getLexema());
        }
        this.token= this.lexico.nextToken();
    }

    private void CS(){
        if(
                this.token.getLexema().equals("int") ||
                        this.token.getLexema().equals("float") ||
                        this.token.getLexema().equals("char")){
            this.C();
            this.CS();
        }else if(this.token.getTipo() == Token.TIPO_OPERADOR_ARITMETICO){
            this.E();
            this.CS();
        }else if(this.token.getTipo() == Token.TIPO_PALAVRA_RESERVADA){
            this.PL();
            this.CS();
        }else if(this.token.getTipo() == Token.TIPO_INTEIRO){
            this.T();
            this.CS();
        }else if(this.token.getLexema().equals(";")){
            this.SC();
            this.CS();
        }else if(this.token.getTipo() == Token.TIPO_IDENTIFICADOR){
            this.E();
            this.CS();
        }else if(this.token.getTipo() == Token.TIPO_CHAR){
            this.T();
            this.CS();
        }else if(this.token.getTipo() == Token.TIPO_REAL){
            this.T();
            this.CS();
        }
    }

    private void C(){
        if(this.token.getTipo() == Token.TIPO_IDENTIFICADOR){
            this.ATRIBUICAO();
        }else if(this.token.getLexema().equals("int") ||
                this.token.getLexema().equals("float") ||
                this.token.getLexema().equals("char")){

            this.DECLARACAO();
        }else{
            throw new RuntimeException("Erro, esperava-se que você declarasse um comando próximo de: "+this.token.getLexema());
        }
    }

    private void DECLARACAO(){
        if(!(this.token.getLexema().equals("int") ||
                this.token.getLexema().equals("float") ||
                this.token.getLexema().equals("char"))){
            throw new RuntimeException("Erro! na declaração de variavel. "+" Você errou próximo de: "+this.token.getLexema());
        }
        this.token= this.lexico.nextToken();
        if(this.token.getTipo() != Token.TIPO_IDENTIFICADOR){
            throw new RuntimeException("Erro! na declaração de variavel. "+" Você errou próximo de: "+this.token.getLexema());
        }
        this.token = this.lexico.nextToken();
        if(this.token.getTipo() != Token.TIPO_OPERADOR_ATRIBUICAO && !this.token.getLexema().equalsIgnoreCase(";")){
            throw new RuntimeException("Erro! na declaração de variavel. "+" Você errou próximo de: "+this.token.getLexema());
        }
        this.token = this.lexico.nextToken();
    }

    private void ATRIBUICAO(){
        if(this.token.getTipo() != Token.TIPO_IDENTIFICADOR){
            throw new RuntimeException("Erro de atribuição! Próximo de: "+this.token.getLexema());
        }
        // this.token = this.lexico.nextToken();
        if(this.token.getTipo() == Token.TIPO_OPERADOR_RELACIONAL){
            this.token= this.lexico.nextToken();
            return;
        }
        if(this.token.getTipo() == Token.TIPO_CARACTER_ESPECIAL){
            this.token= this.lexico.nextToken();
            return;
        }
        if(this.token.getTipo() == Token.TIPO_OPERADOR_ARITMETICO){
            this.token= this.lexico.nextToken();
            return;
        }
        if(this.token.getTipo() == Token.TIPO_PALAVRA_RESERVADA){
            this.token = this.lexico.nextToken();
            return;
        }
        if(this.token.getTipo() != Token.TIPO_OPERADOR_ATRIBUICAO){
            throw new RuntimeException("Erro de atribuição! Próximo de: "+this.token.getLexema());
        }
        this.token = this.lexico.nextToken();
        this.E();
        this.token = this.lexico.nextToken();
    }

    private void E(){
        if(this.token.getTipo() == Token.TIPO_IDENTIFICADOR ||
                this.token.getLexema().equals("int") ||
                this.token.getLexema().equals("float")){

            this.T();

            if(this.token.getTipo() == Token.TIPO_OPERADOR_ARITMETICO){
                this.El();
            }
            if(this.token.getTipo() == Token.TIPO_OPERADOR_ATRIBUICAO){
                this.AV();
            }
        }
    }

    private void El(){
        if(this.token.getTipo() == Token.TIPO_OPERADOR_ARITMETICO){
            this.OP();
            this.T();
            this.El();
        }else{
        }
    }
    private void AV() {
        if (this.token.getTipo() == Token.TIPO_OPERADOR_ATRIBUICAO) {
            this.token = this.lexico.nextToken();
            if (this.token.getTipo() != Token.TIPO_IDENTIFICADOR
                    && this.token.getTipo() != Token.TIPO_INTEIRO
                    && this.token.getTipo() != Token.TIPO_REAL
                    && this.token.getTipo() != Token.TIPO_CHAR) {
                throw new RuntimeException("Erro na atribuição de variável. Era pra ter colocado" +
                        " um identificador ou número ou char perto de " + this.token.getLexema());
            }
            this.token = this.lexico.nextToken();
            if (this.token.getTipo() == Token.TIPO_OPERADOR_ARITMETICO
                    || this.token.getTipo() == Token.TIPO_IDENTIFICADOR
                    || this.token.getTipo() == Token.TIPO_INTEIRO
                    || this.token.getTipo() == Token.TIPO_REAL
                    || this.token.getTipo() == Token.TIPO_CHAR
                    || this.token.getLexema().equals(";")) {
                this.token = this.lexico.nextToken();
            } else {
                throw new RuntimeException("Erro na atribuição de variável. Era pra ter colocado" +
                        " um operador aritmetico (+,-,*,/) ou identificador ou número" +
                        " ou char ou finalizar a atribuição com ; perto de " + this.token.getLexema());
            }
        }
    }

    private void T(){
        if(this.token.getTipo() == Token.TIPO_IDENTIFICADOR ||
                this.token.getTipo() == Token.TIPO_INTEIRO ||
                this.token.getTipo() == Token.TIPO_REAL ||
                this.token.getTipo() == Token.TIPO_CHAR){

            this.token=this.lexico.nextToken();
        }else{
            throw new RuntimeException("Erro! Era para ser um identificador " + "ou numero proximo de "+ this.token.getLexema());
        }
    }

    private void OP(){
        if(this.token.getTipo() == Token.TIPO_OPERADOR_ARITMETICO){
            this.token=this.lexico.nextToken();
        }else{
            throw new RuntimeException("Erro! Era para ser um operador" + " aritmetico(+/-/*/%/) proximo de "+ this.token.getLexema());
        }
    }
    private void SC() {
        if (this.token.getLexema().equals(";")) {
            this.token = this.lexico.nextToken();
        } else {
            throw new RuntimeException("Tu esqueceu do ; perto de " + this.token.getLexema());
        }
    }
    private void PL() {
        if (this.token.getLexema().equals("if")
                || this.token.getLexema().equals("while")) {
            this.token = this.lexico.nextToken();
            this.OB(); // (
            this.FP(); // )
            this.AC(); // {
            this.FC(); // }
        }
        else if (this.token.getLexema().equals("}") && this.token.getLexema().equals("else")) {
        }
        else if (this.token.getLexema().equals("else")) {
            this.token = this.lexico.nextToken();
            if (this.token.getLexema().equals("if")) {
                this.lexico.nextToken();
                this.OB();
                this.FP();
                this.AC();
                this.FC();
            } else if (this.token.getLexema().equals("{")) {
                this.AC();
                this.FC();
            }
        } else if (this.token.getTipo() == Token.TIPO_PALAVRA_RESERVADA) {
            this.token = this.lexico.nextToken();
        } else {
            throw new RuntimeException("Era pra tu ter colocado uma palavra reservada "
                    + "(main | if | else | int | float | char | do | for | while) perto de " +
                    this.token.getLexema());
        }
    }

    private void OB() {
        if (this.token.getLexema().equals("(")) {
            this.token = this.lexico.nextToken();

            if (this.token.getLexema().equals("int")
                    || this.token.getLexema().equals("float")
                    || this.token.getLexema().equals("char")) {
                this.token = this.lexico.nextToken();
                if (this.token.getTipo() != Token.TIPO_IDENTIFICADOR) {
                    throw new RuntimeException("Errasse na declaração de variável" +
                            " dentro do escopo do if ou while. Era pra ter colocado" +
                            " um identificador perto de " + this.token.getLexema());
                } else {
                    this.token = this.lexico.nextToken();
                }
            }
            else if (this.token.getTipo() == Token.TIPO_INTEIRO
                    || this.token.getTipo() == Token.TIPO_REAL
                    || this.token.getTipo() == Token.TIPO_IDENTIFICADOR) {
                this.token = this.lexico.nextToken();
            }

            if (this.token.getTipo() == Token.TIPO_OPERADOR_ARITMETICO
                    || this.token.getTipo() == Token.TIPO_OPERADOR_ATRIBUICAO
                    || this.token.getTipo() == Token.TIPO_OPERADOR_RELACIONAL) {
                this.token = this.lexico.nextToken();
            } else {
                throw new RuntimeException("Erro! Na operação de variável" +
                        " dentro do escopo do if ou while.\nEra pra ter colocado um" +
                        " operador aritmético (+,-,*,/), operador de atribuição (=) ou" +
                        " operador relacional (<, <=, >, >=, !=, ==) perto de " + this.token.getLexema());
            }

            if (this.token.getTipo() == Token.TIPO_IDENTIFICADOR
                    || this.token.getTipo() == Token.TIPO_INTEIRO
                    || this.token.getTipo() == Token.TIPO_REAL
                    || this.token.getTipo() == Token.TIPO_CHAR) {
                this.token = this.lexico.nextToken();
            } else {
                throw new RuntimeException("Errasse na operação de variável" +
                        " dentro do escopo do if ou while.\nEra pra ter colocado um" +
                        " identificador ou número ou char perto de " + this.token.getLexema());
            }

        } else {
            throw new RuntimeException("Ei boy, tu não abriu o parêntese do teu if/while/else if" +
                    " perto de " + this.token.getLexema() + " visse.");
        }
    }
    private void FP() {
        if (this.token.getLexema().equals(")")) {
            this.token = this.lexico.nextToken();
        } else {
            throw new RuntimeException("Aguarde o processo, esqueceu de fechar o parêntese do " +
                    "if ou while antes de " + this.token.getLexema());
        }
    }

    private void AC() {
        if (this.token.getLexema().equals("{")) {
            this.token = this.lexico.nextToken();
            this.CS();
        } else {
            throw new RuntimeException("Esqueceu de abrir chaves perto de " + this.token.getLexema());
        }
    }

    private void FC() {
        if (this.token.getLexema().equals("}")) {
            this.token = this.lexico.nextToken();
        } else {
            throw new RuntimeException("Esqueceu de fechar chaves perto de " + this.token.getLexema());
        }
    }
}