import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class Retangulo {

    List<Set<String>> palavrasPorTamanho;
    List<Trie> trie;
    int maxTamanho;

    public Retangulo(List<String> palavras) {
        preProcessarPalavras(palavras);

    }

    public Retangulo(List<String> palavras, int dummy)
    {

        preProcessarPalavras(palavras);
        preProcessarPalavrasTrie(palavras);
    }

    private void preProcessarPalavras(List<String> palavras) {
        palavrasPorTamanho = new ArrayList<>();
        maxTamanho = 0;
        for (String palavra : palavras) {
            int tamanho = palavra.length();
            this.maxTamanho = Math.max(tamanho, maxTamanho);
            while (palavrasPorTamanho.size() <= tamanho) {
                palavrasPorTamanho.add(null);
            }
            Set<String> conjuntoPalavras = palavrasPorTamanho.get(tamanho);
            if (conjuntoPalavras == null) {
                conjuntoPalavras = new HashSet<>();
                palavrasPorTamanho.set(tamanho, conjuntoPalavras);
            }
            conjuntoPalavras.add(palavra);
        }
    }

    private void preProcessarPalavrasTrie(List<String> palavras)
    {
        //sempre tem de ser executado depois do outro agora
        trie = new ArrayList<>();
        for(int i = 0; i <= maxTamanho; i++ ){
            Trie novaTrie = new Trie();
            trie.add(novaTrie);
        }

        for(String palavra : palavras)
        {
            int tamanho = palavra.length();
            trie.get(tamanho).adicionaPalavra(palavra);// preciso inicializar a lista. Como?
        }
    }

    public List<String> encontrarMaiorMatriz() {
        int area = maxTamanho * maxTamanho;
        while (area > 0) {
            for (int nLinhas = maxTamanho; nLinhas > 0; nLinhas--) {
                int nColunas = area / nLinhas;
                if (nColunas > nLinhas) {
                    break;  // queremos linhas >= colunas
                }
                if (nLinhas * nColunas != area) {
                    continue;
                }
                List<String> resultado = encontrarMatriz(
                        nLinhas, nColunas);
                if (resultado != null) {
                    return resultado;
                }
            }
            area--;
        }
        return null;
    }

    public List<String> encontrarMaiorMatrizTrie() {
        int area = maxTamanho * maxTamanho;
        while (area > 0) {
            for (int nLinhas = maxTamanho; nLinhas > 0; nLinhas--) {
                int nColunas = area / nLinhas;
                if (nColunas > nLinhas) {
                    break;  // queremos linhas >= colunas
                }
                if (nLinhas * nColunas != area) {
                    continue;
                }
                List<String> resultado = encontrarMatrizTrie(
                        nLinhas, nColunas);
                if (resultado != null) {
                    return resultado;
                }
            }
            area--;
        }
        return null;
    }

    private List<String> encontrarMatriz(int p, int q) {
        // preciso de p palavras de tamanho q
        List<String> retangulo = new ArrayList<>(p);
//        return expandirRetangulo(retangulo, p, q);
        return expandirRetanguloComPoda(retangulo, p, q);
    }

    private List<String> encontrarMatrizTrie(int p, int q) {
        // preciso de p palavras de tamanho q
        List<String> retangulo = new ArrayList<>(p);
//        return expandirRetangulo(retangulo, p, q);
        return expandirRetanguloComPodaTrie(retangulo, p, q);
    }

    private List<String> expandirRetangulo(List<String> retangulo,
                                           int p, int q) {
        // verifica se eh um retangulo completo (estado final)
        if (retangulo.size() == p) {
            if (verificarRetangulo(retangulo, p)) {
                return retangulo;
            } else {
                return null;
            }
        }

        // nao eh retangulo completo ainda, vou expandir
        Set<String> palavrasQ = palavrasPorTamanho.get(q);
        if (palavrasQ == null) {
            return null;
        }
        for (String palavraQ : palavrasQ) {
            retangulo.add(palavraQ);  // expande
            // chama recursivamente
            List<String> resultado = expandirRetangulo(retangulo, p, q);
            if (resultado != null) {
                return resultado;
            }
            retangulo.remove(retangulo.size() - 1);  // limpa
        }
        return null;
    }

    private List<String> expandirRetanguloComPoda
            (List<String> retangulo, int p, int q) {
        // verifica se eh um estado valido! (fail early)
        if (!verificarRetangulo(retangulo, p)) {
            return null;
        }

        // verifica se eh um retangulo completo (estado final)
        if (retangulo.size() == p) {
            return retangulo;
        }

        // nao eh um retangulo completo ainda; vou expandir!
        Set<String> palavrasQ = palavrasPorTamanho.get(q);
        if (palavrasQ == null) {
            return null;
        }
        for (String palavraQ : palavrasQ) {
            // expande
            retangulo.add(palavraQ);

            // chama recursivamente
            List<String> resultado = expandirRetanguloComPoda(retangulo, p, q);
            if (resultado != null) {
                return resultado;
            }

            // limpa
            retangulo.remove(retangulo.size() - 1);
        }
        return null;
    }

    private List<String> expandirRetanguloComPodaTrie (List<String> retangulo, int p, int q) {
        // verifica se eh um estado valido! (fail early)
        if (!verificarRetanguloTrie(retangulo, p)) {
            return null;
        }

        // verifica se eh um retangulo completo (estado final)
        if (retangulo.size() == p) {
            return retangulo;
        }

        // nao eh um retangulo completo ainda; vou expandir!
        Set<String> palavrasQ = palavrasPorTamanho.get(q);
        if (palavrasQ == null) {
            return null;
        }
        for (String palavraQ : palavrasQ) {
            // expande
            retangulo.add(palavraQ);

            // chama recursivamente
            List<String> resultado = expandirRetanguloComPodaTrie(retangulo, p, q);
            if (resultado != null) {
                return resultado;
            }

            // limpa
            retangulo.remove(retangulo.size() - 1);
        }
        return null;
    }

    /**
     * Valida o retangulo informado.
     *
     * @param retangulo Lista com p palavras de tamanho q
     * @param tamanhoAlvo o tamanho desejado
     * @return true, se o retangulo contem q palavras, cada qual
     *               sendo o p-prefixo de alguma palavra validao
     *               com tamanhoAlvo caracteres;
     *         false, caso contrario
     */
    private boolean verificarRetangulo(List<String> retangulo,
                                       int tamanhoAlvo) {
        if (retangulo.size() == 0) {
            return true;
        }
        Set<String> palavrasP = palavrasPorTamanho.get(tamanhoAlvo);
        if (palavrasP == null) {
            return false;
        }
        StringBuffer sb = new StringBuffer();
        for (int coluna = 0; coluna < retangulo.get(0).length(); coluna++) {
            sb.setLength(0);
            for (int linha = 0; linha < retangulo.size(); linha++) {
                sb.append(retangulo.get(linha).charAt(coluna));
            }
            String palavra = sb.toString();
            if (tamanhoAlvo == retangulo.size()) {
                // nao estamos interessados em prefixos,
                // mas na palavra inteira!!
                if (!palavrasP.contains(palavra)) {
                    return false;
                }
            } else {
                // queremos na verdade saber se a palavra
                // referente a essa coluna eh prefixo de alguma palavra valida
                if (!verificarPrefixo(palavra, tamanhoAlvo)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean verificarRetanguloTrie(List<String> retangulo, int tamanhoAlvo)
    {
        if (retangulo.size() == 0) {
            return true;
        }

        Set<String> palavrasP = palavrasPorTamanho.get(tamanhoAlvo);
        if (palavrasP == null) {
            return false;
        }
        StringBuffer sb = new StringBuffer();
        for (int coluna = 0; coluna < retangulo.get(0).length(); coluna++) {
            sb.setLength(0);
            for (int linha = 0; linha < retangulo.size(); linha++) {
                sb.append(retangulo.get(linha).charAt(coluna));
            }
            String palavra = sb.toString();
            if (tamanhoAlvo == retangulo.size()) {
                // nao estamos interessados em prefixos,
                // mas na palavra inteira!!
                if (!palavrasP.contains(palavra)) {
                    return false;
                }
            } else {
                // queremos na verdade saber se a palavra
                // referente a essa coluna eh prefixo de alguma palavra valida
                    if (trie.get(tamanhoAlvo).achaAlgum(palavra)) { // linha onde o codigo difere tb, usa trie pra procurar
                        return true;
                }
            }
        }
        return true;

    }



    private boolean verificarPrefixo(String prefixo, int tamanhoAlvo) {
        Set<String> palavrasComTamanhoAlvo = palavrasPorTamanho.get(
                tamanhoAlvo);
        if (palavrasComTamanhoAlvo == null) {
            return false;
        }
        for (String palavra : palavrasComTamanhoAlvo) {
            if (palavra.startsWith(prefixo)) {
                return true;
            }
        }
        return false;
    }


    public static void imprimirRetangulo(List<String> retangulo) {
        if (retangulo == null) {
            System.out.println("Nao encontrou nada!");
        } else {
            for (String palavra : retangulo) {
                System.out.println(palavra);
            }
        }
    }

    public static void main(String args[]) {
        List<String> palavras = new ArrayList<>();
        palavras.add("a");
        palavras.add("bb");
        palavras.add("asa");
        palavras.add("asas");
        palavras.add("sas");
        palavras.add("sal");
        palavras.add("ala");
        palavras.add("sala");
        palavras.add("apendicite");
        palavras.add("abacate");
        palavras.add("pequeno");
        palavras.add("aborigene");
//      palavras.add("inconstitucionalissimamente");

        long tempoDeInicio = System.currentTimeMillis();
        Retangulo instancia = new Retangulo(palavras);
        List<String> resultado = instancia.encontrarMaiorMatriz();
        long tempoDeFim = System.currentTimeMillis();
        imprimirRetangulo(resultado);

        long tempoDeInicioTrie = System.currentTimeMillis();
        Retangulo instanciaTrie = new Retangulo(palavras, 0);
        List<String> resultadoTrie = instanciaTrie.encontrarMaiorMatrizTrie();
        long tempoDeFimTrie = System.currentTimeMillis();
        imprimirRetangulo(resultadoTrie);


        System.out.print("Tempo usando Set:" + (tempoDeFim - tempoDeInicio) + "\n");
        System.out.print("Tempo usando Trie:" + (tempoDeFimTrie - tempoDeInicioTrie) + "\n");

    }

}
