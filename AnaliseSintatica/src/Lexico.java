import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 *
 * @author tarci
 */
public class Lexico {
    //public static String linha;
    private char[] conteudo;
    private int estado;

    public Lexico(String caminhoCodigoFonte) {
        try {
            String conteudoStr;
            conteudoStr = new String(Files.readAllBytes(Paths.get(caminhoCodigoFonte)));
            System.out.println(conteudoStr);
            this.conteudo = conteudoStr.toCharArray();
            this.estado = 0;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    //Retorna próximo char
    private char nextChar() {
        return this.conteudo[this.estado++];
    }

    //Verifica existe próximo char ou chegou ao final do código fonte
    private boolean hasNextChar() {
        return estado < this.conteudo.length;
    }

    //Retrocede o índice que aponta para o "char da vez" em uma unidade
    private void back() {
        this.estado--;
    }

    //Identificar se char é letra minúscula
    private boolean isLetra(char c) {
        return (c >= 'a') && (c <= 'z');
    }

    //Identificar se char é dígito
    private boolean isDigito(char c) {
        return (c >= '0') && (c <= '9');
    }

    private boolean isOperadorAritmetico(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '%';
    }

    private boolean isCaracterEspecial(char c) {
        return c == ')' || c == '(' || c == '}' || c == '{' || c == ';' || c == ',';
    }

    private boolean isOperadorRelacional(char c){
        return c == '>' || c == '<';
    }

    private boolean isOperadorAtribuicao(char c) {
        return (c == '=');
    }

    private boolean isSpace(char c) {
        return c == ' ' || c == '\t' || c == '\n' || c == '\r';
    }
    //Método retorna próximo token válido ou retorna mensagem de erro.
    public Token nextToken() {
        Token token = null;
        char c;
        int estado = 0;
        StringBuffer lexema = new StringBuffer();

        while (hasNextChar()) {
            c = this.nextChar();
            switch (estado) {
                case 0:
                    if (isSpace(c)) {
                        estado = 0;
                    } else if (isLetra(c)) {
                        lexema.append(c);
                        estado = 1;
                    } else if (isDigito(c)) {
                        lexema.append(c);
                        estado = 2;
                    } else if (isCaracterEspecial(c)) {
                        lexema.append(c);
                        estado = 5;
                    } else if (isOperadorAritmetico(c)) {
                        lexema.append(c);
                        return new Token(lexema.toString(), Token.TIPO_OPERADOR_ARITMETICO);
                    } else if (c == 39){
                        lexema.append(c);
                        estado = 7;
                    } else if (c == '<') {
                        lexema.append(c);
                        estado = 10;
                    } else if (c == '=') {
                        lexema.append(c);
                        estado = 13;
                    } else if (c == '>') {
                        lexema.append(c);
                        estado = 15;
                    } else if (c == '$') {
                        lexema.append(c);
                        estado = 99;
                        back();
                    } else {
                        throw new RuntimeException("Símbolo não reconhecido \"" + lexema.toString() + "\"");
                    }
                    break;
                case 1:
                    if (isDigito(c) || isLetra(c)) {
                        lexema.append(c);
                        estado = 16;
                    }
                    else {
                        back();
                        if ("if".contentEquals(lexema.toString()) || "main".contentEquals(lexema.toString()) || "else".contentEquals(lexema.toString()) || "while".contentEquals(lexema.toString()) || "do".contentEquals(lexema.toString()) || "for".contentEquals(lexema.toString()) || "int".contentEquals(lexema.toString()) || "float".contentEquals(lexema.toString()) || "char".contentEquals(lexema.toString())) {
                            back();
                            return new Token(lexema.toString(), Token.TIPO_PALAVRA_RESERVADA);
                        } else {
                            back();
                            return new Token(lexema.toString(), Token.TIPO_IDENTIFICADOR);
                        }
                    }
                    break;
                case 2:
                    if (isDigito(c) ) {
                        lexema.append(c);
                        estado = 2;
                    }
                    else if ( c == '.') {
                        lexema.append(c);
                        estado = 4;
                    }
                    else {
                        back();
                        return new Token(lexema.toString(), Token.TIPO_INTEIRO);
                    }
                    break;
                case 4:
                    if (isDigito(c)) {
                        lexema.append(c);
                        estado = 4;
                    }
                    else {
                        back();
                        return new Token(lexema.toString(), Token.TIPO_REAL);
                    }
                    break;
                case 5:
                   back();
                   return new Token(lexema.toString(), Token.TIPO_CARACTER_ESPECIAL);
                case 6:
                    back();
                    return new Token(lexema.toString(), Token.TIPO_OPERADOR_ARITMETICO);
                case 7:
                    if (isDigito(c) || isLetra(c)){
                        lexema.append(c);
                        estado = 8;
                    }
                    else{
                        throw new RuntimeException("Símbolo não reconhecido\"" + lexema.toString() + "\"");
                    }
                    break;
                case 8:
                    if (c == 39){
                        lexema.append(c);
                        estado = 9;
                    }
                    else{
                        throw new RuntimeException("Char não reconhecido\"" + lexema.toString() + "\"");
                    }
                    break;
                case 9:
                    back();
                    return new Token(lexema.toString(), Token.TIPO_CHAR);
                case 10:
                    if (c == '=' || c == '>'){
                        lexema.append(c);
                        estado = 11;
                    }
                    else {
                        back();
                        return new Token(lexema.toString(), Token.TIPO_OPERADOR_RELACIONAL);
                    }
                    break;
                case 11:
                    back();
                    return new Token(lexema.toString(), Token.TIPO_OPERADOR_RELACIONAL);
                case 12:
                    back();
                    return new Token(lexema.toString(), Token.TIPO_OPERADOR_RELACIONAL);
                case 13:
                    if (c == '='){
                        lexema.append(c);
                        estado = 14;
                    }
                    else {
                        back();
                        return new Token(lexema.toString(), Token.TIPO_OPERADOR_RELACIONAL);
                    }
                    break;
                case 14:
                    back();
                    return new Token(lexema.toString(), Token.TIPO_OPERADOR_RELACIONAL);
                case 15:
                    if (c == '=') {
                        lexema.append(c);
                        estado = 12;
                    }
                    else {
                        back();
                        return new Token(lexema.toString(), Token.TIPO_OPERADOR_RELACIONAL);
                    }
                    break;
                case 16:
                    if (this.isLetra(c) || this.isDigito(c)) {
                        lexema.append(c);
                        estado = 16;
                    } else {
                        if ("if".contentEquals(lexema.toString()) || "main".contentEquals(lexema.toString()) || "else".contentEquals(lexema.toString()) || "while".contentEquals(lexema.toString()) || "do".contentEquals(lexema.toString()) || "for".contentEquals(lexema.toString()) || "int".contentEquals(lexema.toString()) || "float".contentEquals(lexema.toString()) || "char".contentEquals(lexema.toString())) {
                            this.back();
                            return new Token(lexema.toString(), Token.TIPO_PALAVRA_RESERVADA);
                        }

                        this.back();
                        return new Token(lexema.toString(), Token.TIPO_IDENTIFICADOR);
                    }
                    break;
                case 99:
                    return new Token(lexema.toString(), Token.TIPO_FIM_CODIGO);
            }
        }
        return token;
    }
}