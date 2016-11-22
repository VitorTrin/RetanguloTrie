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
                    if (!trie.get(tamanhoAlvo).achaAlgum(palavra)) { // linha onde o codigo difere tb, usa trie pra procurar
                        return false;
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
//        palavras.add("a");
//        palavras.add("bb");
//        palavras.add("asa");
//        palavras.add("asas");
//        palavras.add("sas");
//        palavras.add("sal");
//        palavras.add("ala");
//        palavras.add("sala");
//        palavras.add("apendicite");
//        palavras.add("abacate");
//        palavras.add("pequeno");
//        palavras.add("aborigene");
      palavras.add("inconstitucionalissimamente");

        //novas palavras: 54763

//     palavras.add("ten");
        palavras.add("tenable");
        palavras.add("tenacious");
        palavras.add("tenaciously");
        palavras.add("tenacity");
        palavras.add("tenant");
        palavras.add("tenantxs");
        palavras.add("tenants");
        palavras.add("tend");
        palavras.add("tended");
        palavras.add("tendencies");
        palavras.add("tendency");
        palavras.add("tender");
        palavras.add("tenderfoot");
        palavras.add("tenderloin");
        palavras.add("tenderly");
        palavras.add("tenderness");
        palavras.add("tenders");
        palavras.add("tending");
        palavras.add("tendon");
        palavras.add("tends");
        palavras.add("tenebrous");
        palavras.add("tenement");
        palavras.add("tenementxs");
        palavras.add("tenements");
        palavras.add("tenet");
        palavras.add("tenfold");
        palavras.add("tenneco");
        palavras.add("tennessee");
        palavras.add("tenney");
        palavras.add("tennis");
        palavras.add("tennyson");
        palavras.add("tenon");
        palavras.add("tenor");
        palavras.add("tenorxs");
        palavras.add("tenors");
        palavras.add("tens");
        palavras.add("tense");
        palavras.add("tensed");
        palavras.add("tensely");
        palavras.add("tenseness");
        palavras.add("tenser");
        palavras.add("tenses");
        palavras.add("tensest");
        palavras.add("tensile");
        palavras.add("tensing");
        palavras.add("tension");
        palavras.add("tensional");
        palavras.add("tensions");
        palavras.add("tensor");
        palavras.add("tenspot");
        palavras.add("tent");
        palavras.add("tentacle");
        palavras.add("tentacled");
        palavras.add("tentacles");
        palavras.add("tentative");
        palavras.add("tentatively");
        palavras.add("tented");
        palavras.add("tenterhooks");
        palavras.add("tenth");
        palavras.add("tenting");
        palavras.add("tents");
        palavras.add("tenuous");
        palavras.add("tenure");
        palavras.add("tepee");
        palavras.add("tepid");
        palavras.add("tequila");
        palavras.add("teratogenic");
        palavras.add("teratology");
        palavras.add("terbium");
        palavras.add("tercel");
        palavras.add("teresa");
        palavras.add("term");
        palavras.add("termed");
        palavras.add("terminable");
        palavras.add("terminal");
        palavras.add("terminalxs");
        palavras.add("terminally");
        palavras.add("terminals");
        palavras.add("terminat");
        palavras.add("terminate");
        palavras.add("terminated");
        palavras.add("terminates");
        palavras.add("terminating");
        palavras.add("termination");
        palavras.add("terminations");
        palavras.add("terminator");
        palavras.add("terminatorxs");
        palavras.add("terminators");
        palavras.add("terming");
        palavras.add("termini");
        palavras.add("terminologies");
        palavras.add("terminology");
        palavras.add("terminus");
        palavras.add("termite");
        palavras.add("terms");
        palavras.add("termwise");
        palavras.add("tern");
        palavras.add("ternary");
        palavras.add("terpsichore");
        palavras.add("terpsichorean");
        palavras.add("terra");
        palavras.add("terrace");
        palavras.add("terraced");
        palavras.add("terraces");
        palavras.add("terrain");
        palavras.add("terrainxs");
        palavras.add("terrains");
        palavras.add("terramycin");
        palavras.add("terrapin");
        palavras.add("terre");
        palavras.add("terrestrial");
        palavras.add("terrible");
        palavras.add("terribly");
        palavras.add("terrier");
        palavras.add("terrierxs");
        palavras.add("terriers");
        palavras.add("terrific");
        palavras.add("terrified");
        palavras.add("terrifies");
        palavras.add("terrify");
        palavras.add("terrifying");
        palavras.add("territorial");
        palavras.add("territories");
        palavras.add("territory");
        palavras.add("territoryxs");
        palavras.add("terror");
        palavras.add("terrorxs");
        palavras.add("terrorism");
        palavras.add("terrorist");
        palavras.add("terroristxs");
        palavras.add("terroristic");
        palavras.add("terrorists");
        palavras.add("terrorize");
        palavras.add("terrorized");
        palavras.add("terrorizes");
        palavras.add("terrorizing");
        palavras.add("terrors");
        palavras.add("terry");
        palavras.add("terse");
        palavras.add("tertiary");
        palavras.add("tess");
        palavras.add("tessellate");
        palavras.add("test");
        palavras.add("testability");
        palavras.add("testable");
        palavras.add("testament");
        palavras.add("testamentxs");
        palavras.add("testamentary");
        palavras.add("testaments");
        palavras.add("testate");
        palavras.add("testbed");
        palavras.add("tested");
        palavras.add("tester");
        palavras.add("testers");
        palavras.add("testes");
        palavras.add("testibil");
        palavras.add("testicle");
        palavras.add("testiclexs");
        palavras.add("testicles");
        palavras.add("testicular");
        palavras.add("testified");
        palavras.add("testifier");
        palavras.add("testifiers");
        palavras.add("testifies");
        palavras.add("testify");
        palavras.add("testifying");
        palavras.add("testimonial");
        palavras.add("testimonies");
        palavras.add("testimony");
        palavras.add("testimonyxs");
        palavras.add("testing");
        palavras.add("testings");
        palavras.add("testpass");
        palavras.add("tests");
        palavras.add("testy");
        palavras.add("tetanus");
        palavras.add("tete");
        palavras.add("tether");
        palavras.add("tetrachloride");
        palavras.add("tetrafluoride");
        palavras.add("tetrafluouride");
        palavras.add("tetragonal");
        palavras.add("tetrahedra");
        palavras.add("tetrahedral");
        palavras.add("tetrahedron");
        palavras.add("tetravalent");
        palavras.add("teutonic");
        palavras.add("texaco");
        palavras.add("texan");
        palavras.add("texas");
        palavras.add("text");
        palavras.add("textxs");
        palavras.add("textbook");
        palavras.add("textbookxs");
        palavras.add("textbooks");
        palavras.add("textile");
        palavras.add("textilexs");
        palavras.add("textiles");
        palavras.add("textron");
        palavras.add("texts");
        palavras.add("textual");
        palavras.add("textually");
        palavras.add("textural");
        palavras.add("texture");
        palavras.add("textured");
        palavras.add("textures");
        palavras.add("th");
        palavras.add("thai");
        palavras.add("thailand");
        palavras.add("thalia");
        palavras.add("thallium");
        palavras.add("thallophyte");
        palavras.add("than");
        palavras.add("thanatos");
        palavras.add("thank");
        palavras.add("thanked");
        palavras.add("thankful");
        palavras.add("thankfully");
        palavras.add("thankfulness");
        palavras.add("thanking");
        palavras.add("thankless");
        palavras.add("thanklessly");
        palavras.add("thanklessness");
        palavras.add("thanks");
        palavras.add("thanksgiving");
        palavras.add("thankyou");
        palavras.add("that");
        palavras.add("thatxd");
        palavras.add("thatxll");
        palavras.add("thatxs");
        palavras.add("thatch");
        palavras.add("thatches");
        palavras.add("thats");
        palavras.add("thaw");
        palavras.add("thawed");
        palavras.add("thawing");
        palavras.add("thaws");
        palavras.add("thayer");
        palavras.add("the");
        palavras.add("thea");
        palavras.add("theater");
        palavras.add("theaterxs");
        palavras.add("theaters");
        palavras.add("theatre");
        palavras.add("theatric");
        palavras.add("theatrical");
        palavras.add("theatrically");
        palavras.add("theatricals");
        palavras.add("thebear");
        palavras.add("thebes");
        palavras.add("theboss");
        palavras.add("thecat");
        palavras.add("thecrow");
        palavras.add("thecure");
        palavras.add("thedoors");
        palavras.add("thee");
        palavras.add("theforce");
        palavras.add("theft");
        palavras.add("theftxs");
        palavras.add("thefts");
        palavras.add("thegame");
        palavras.add("thegreat");
        palavras.add("their");
        palavras.add("theirs");
        palavras.add("theism");
        palavras.add("theist");
        palavras.add("theking");
        palavras.add("thelma");
        palavras.add("them");
        palavras.add("thematic");
        palavras.add("thematri");
        palavras.add("theme");
        palavras.add("themexs");
        palavras.add("themes");
        palavras.add("themselves");
        palavras.add("then");
        palavras.add("thence");
        palavras.add("thenceforth");
        palavras.add("theocracy");
        palavras.add("theodore");
        palavras.add("theodosian");
        palavras.add("theologian");
        palavras.add("theological");
        palavras.add("theology");
        palavras.add("theorem");
        palavras.add("theoremxs");
        palavras.add("theorems");
        palavras.add("theoretic");
        palavras.add("theoretical");
        palavras.add("theoretically");
        palavras.add("theoretician");
        palavras.add("theoreticians");
        palavras.add("theories");
        palavras.add("theorist");
        palavras.add("theoristxs");
        palavras.add("theorists");
        palavras.add("theorization");
        palavras.add("theorizationxs");
        palavras.add("theorizations");
        palavras.add("theorize");
        palavras.add("theorized");
        palavras.add("theorizer");
        palavras.add("theorizers");
        palavras.add("theorizes");
        palavras.add("theorizing");
        palavras.add("theory");
        palavras.add("theoryxs");
        palavras.add("therapeutic");
        palavras.add("therapies");
        palavras.add("therapist");
        palavras.add("therapistxs");
        palavras.add("therapists");
        palavras.add("therapy");
        palavras.add("therapyxs");
        palavras.add("there");
        palavras.add("therexd");
        palavras.add("therexll");
        palavras.add("therexs");
        palavras.add("thereabouts");
        palavras.add("thereafter");
        palavras.add("thereat");
        palavras.add("thereby");
        palavras.add("therefor");
        palavras.add("therefore");
        palavras.add("therefrom");
        palavras.add("therein");
        palavras.add("thereof");
        palavras.add("thereon");
        palavras.add("theresa");
        palavras.add("thereto");
        palavras.add("theretofore");
        palavras.add("thereunder");
        palavras.add("thereupon");
        palavras.add("therewith");
        palavras.add("thermal");
        palavras.add("thermionic");
        palavras.add("thermistor");
        palavras.add("thermo");
        palavras.add("thermocouple");
        palavras.add("thermodynamic");
        palavras.add("thermodynamics");
        palavras.add("thermoelastic");
        palavras.add("thermoelectric");
        palavras.add("thermofax");
        palavras.add("thermometer");
        palavras.add("thermometerxs");
        palavras.add("thermometers");
        palavras.add("thermometric");
        palavras.add("thermometry");
        palavras.add("thermomigrate");
        palavras.add("thermonuclear");
        palavras.add("thermopile");
        palavras.add("thermoplastic");
        palavras.add("thermopower");
        palavras.add("thermosetting");
        palavras.add("thermostable");
        palavras.add("thermostat");
        palavras.add("thermostatxs");
        palavras.add("thermostatic");
        palavras.add("thermostats");
        palavras.add("therock");
        palavras.add("therockz");
        palavras.add("thesaurus");
        palavras.add("these");
        palavras.add("theses");
        palavras.add("theseus");
        palavras.add("theshit");
        palavras.add("thesims");
        palavras.add("thesis");
        palavras.add("thespian");
        palavras.add("theta");
        palavras.add("thething");
        palavras.add("thetis");
        palavras.add("thetruth");
        palavras.add("thewho");
        palavras.add("they");
        palavras.add("theyxd");
        palavras.add("theyxll");
        palavras.add("theyxre");
        palavras.add("theyxve");
        palavras.add("thiamin");
        palavras.add("thick");
        palavras.add("thicken");
        palavras.add("thickens");
        palavras.add("thicker");
        palavras.add("thickest");
        palavras.add("thicket");
        palavras.add("thicketxs");
        palavras.add("thickets");
        palavras.add("thickish");
        palavras.add("thickly");
        palavras.add("thickness");
        palavras.add("thief");
        palavras.add("thierry");
        palavras.add("thieve");
        palavras.add("thieves");
        palavras.add("thieving");
        palavras.add("thigh");
        palavras.add("thighs");
        palavras.add("thimble");
        palavras.add("thimblexs");
        palavras.add("thimbles");
        palavras.add("thimbu");
        palavras.add("thin");
        palavras.add("thine");
        palavras.add("thing");
        palavras.add("things");
        palavras.add("think");
        palavras.add("thinkable");
        palavras.add("thinkably");
        palavras.add("thinker");
        palavras.add("thinkers");
        palavras.add("thinking");
        palavras.add("thinks");
        palavras.add("thinly");
        palavras.add("thinner");
        palavras.add("thinness");
        palavras.add("thinnest");
        palavras.add("thinning");
        palavras.add("thinnish");
        palavras.add("thiocyanate");
        palavras.add("thiouracil");
        palavras.add("third");
        palavras.add("thirdly");
        palavras.add("thirds");
        palavras.add("thirst");
        palavras.add("thirsted");
        palavras.add("thirsts");
        palavras.add("thirsty");
        palavras.add("thirteen");
        palavras.add("thirteens");
        palavras.add("thirteenth");
        palavras.add("thirties");
        palavras.add("thirtieth");
        palavras.add("thirty");
        palavras.add("this");
        palavras.add("thisxll");
        palavras.add("thistle");
        palavras.add("thistledown");
        palavras.add("thither");
        palavras.add("thomas");
        palavras.add("thomistic");
        palavras.add("thompson");
        palavras.add("thomson");
        palavras.add("thong");
        palavras.add("thor");
        palavras.add("thoreau");
        palavras.add("thoriate");
        palavras.add("thorium");
        palavras.add("thorn");
        palavras.add("thornxs");
        palavras.add("thorns");
        palavras.add("thornton");
        palavras.add("thorny");
        palavras.add("thorough");
        palavras.add("thoroughbred");
        palavras.add("thoroughfare");
        palavras.add("thoroughfarexs");
        palavras.add("thoroughfares");
        palavras.add("thoroughgoing");
        palavras.add("thoroughly");
        palavras.add("thoroughness");
        palavras.add("thorpe");
        palavras.add("thorstein");
        palavras.add("those");
        palavras.add("thou");
        palavras.add("though");
        palavras.add("thought");
        palavras.add("thoughtxs");
        palavras.add("thoughtful");
        palavras.add("thoughtfully");
        palavras.add("thoughtfulness");
        palavras.add("thoughtless");
        palavras.add("thoughtlessly");
        palavras.add("thoughtlessness");
        palavras.add("thoughts");
        palavras.add("thousand");
        palavras.add("thousandfold");
        palavras.add("thousands");
        palavras.add("thousandth");
        palavras.add("thrall");
        palavras.add("thrash");
        palavras.add("thrashed");
        palavras.add("thrasher");
        palavras.add("thrashes");
        palavras.add("thrashing");
        palavras.add("thread");
        palavras.add("threadbare");
        palavras.add("threaded");
        palavras.add("threader");
        palavras.add("threaders");
        palavras.add("threading");
        palavras.add("threads");
        palavras.add("threat");
        palavras.add("threaten");
        palavras.add("threatened");
        palavras.add("threatening");
        palavras.add("threatens");
        palavras.add("threats");
        palavras.add("three");
        palavras.add("threexs");
        palavras.add("threefold");
        palavras.add("threes");
        palavras.add("threescore");
        palavras.add("threesome");
        palavras.add("threonine");
        palavras.add("thresh");
        palavras.add("threshold");
        palavras.add("thresholdxs");
        palavras.add("thresholds");
        palavras.add("threw");
        palavras.add("thrice");
        palavras.add("thrift");
        palavras.add("thrifty");
        palavras.add("thrill");
        palavras.add("thrilled");
        palavras.add("thriller");
        palavras.add("thrillers");
        palavras.add("thrilling");
        palavras.add("thrillingly");
        palavras.add("thrills");
        palavras.add("thrips");
        palavras.add("thrive");
        palavras.add("thrived");
        palavras.add("thrives");
        palavras.add("thriving");
        palavras.add("throat");
        palavras.add("throated");
        palavras.add("throats");
        palavras.add("throaty");
        palavras.add("throb");
        palavras.add("throbbed");
        palavras.add("throbbing");
        palavras.add("throbs");
        palavras.add("throes");
        palavras.add("thrombosis");
        palavras.add("throne");
        palavras.add("thronexs");
        palavras.add("thrones");
        palavras.add("throng");
        palavras.add("throngxs");
        palavras.add("throngs");
        palavras.add("throttle");
        palavras.add("throttled");
        palavras.add("throttles");
        palavras.add("throttling");
        palavras.add("through");
        palavras.add("throughout");
        palavras.add("throughput");
        palavras.add("throw");
        palavras.add("throwaway");
        palavras.add("throwback");
        palavras.add("thrower");
        palavras.add("throwing");
        palavras.add("thrown");
        palavras.add("throws");
        palavras.add("thrum");
        palavras.add("thrumming");
        palavras.add("thrush");
        palavras.add("thrust");
        palavras.add("thrusted");
        palavras.add("thruster");
        palavras.add("thrusters");
        palavras.add("thrusting");
        palavras.add("thrusts");
        palavras.add("thruway");
        palavras.add("thuban");
        palavras.add("thud");
        palavras.add("thudding");
        palavras.add("thuds");
        palavras.add("thug");
        palavras.add("thugxs");
        palavras.add("thuggee");
        palavras.add("thuglife");
        palavras.add("thugs");
        palavras.add("thule");
        palavras.add("thulium");
        palavras.add("thumb");
        palavras.add("thumbed");
        palavras.add("thumbing");
        palavras.add("thumbnail");
        palavras.add("thumbnils");
        palavras.add("thumbs");
        palavras.add("thump");
        palavras.add("thumped");
        palavras.add("thumperz");
        palavras.add("thumping");
        palavras.add("thunder");
        palavras.add("thunderbird");
        palavras.add("thunderbolt");
        palavras.add("thunderboltxs");
        palavras.add("thunderbolts");
        palavras.add("thunderclap");
        palavras.add("thundered");
        palavras.add("thunderer");
        palavras.add("thunderers");
        palavras.add("thunderflower");
        palavras.add("thundering");
        palavras.add("thunderous");
        palavras.add("thunders");
        palavras.add("thundershower");
        palavras.add("thunderstorm");
        palavras.add("thunderstormxs");
        palavras.add("thunderstorms");
        palavras.add("thurman");
        palavras.add("thursday");
        palavras.add("thursdayxs");
        palavras.add("thursdays");
        palavras.add("thus");
        palavras.add("thusly");
        palavras.add("thwack");
        palavras.add("thwart");
        palavras.add("thwarted");
        palavras.add("thwarting");
        palavras.add("thxzzzz");
        palavras.add("thy");
        palavras.add("thyme");
        palavras.add("thymine");
        palavras.add("thymus");
        palavras.add("thyratron");
        palavras.add("thyroglobulin");
        palavras.add("thyroid");
        palavras.add("thyroidal");
        palavras.add("thyronine");
        palavras.add("thyrotoxic");
        palavras.add("thyroxine");
        palavras.add("thyself");
        palavras.add("ti");
        palavras.add("tiber");
        palavras.add("tiberius");
        palavras.add("tibet");
        palavras.add("tibetan");
        palavras.add("tibia");
        palavras.add("tiburon");
        palavras.add("tic");
        palavras.add("tick");
        palavras.add("ticked");
        palavras.add("ticker");
        palavras.add("tickers");
        palavras.add("ticket");
        palavras.add("ticketxs");
        palavras.add("tickets");
        palavras.add("ticking");
        palavras.add("tickle");
        palavras.add("tickled");
        palavras.add("tickles");
        palavras.add("tickling");
        palavras.add("ticklish");
        palavras.add("ticks");
        palavras.add("tid");
        palavras.add("tidal");
        palavras.add("tidally");
        palavras.add("tidbit");
        palavras.add("tide");
        palavras.add("tided");
        palavras.add("tideland");
        palavras.add("tides");
        palavras.add("tidewater");
        palavras.add("tidied");
        palavras.add("tidiness");
        palavras.add("tiding");
        palavras.add("tidings");
        palavras.add("tidy");
        palavras.add("tidying");
        palavras.add("tie");
        palavras.add("tied");
        palavras.add("tientsin");
        palavras.add("tier");
        palavras.add("tiers");
        palavras.add("ties");
        palavras.add("tiffany");
        palavras.add("tift");
        palavras.add("tiger");
        palavras.add("tigerxs");
        palavras.add("tigerzzz");
        palavras.add("tigercat");
        palavras.add("tigers");
        palavras.add("tigger");
        palavras.add("tiggerz");
        palavras.add("tiggerx");
        palavras.add("tight");
        palavras.add("tighten");
        palavras.add("tightened");
        palavras.add("tightener");
        palavras.add("tighteners");
        palavras.add("tightening");
        palavras.add("tightenings");
        palavras.add("tightens");
        palavras.add("tighter");
        palavras.add("tightest");
        palavras.add("tightly");
        palavras.add("tightness");
        palavras.add("tigress");
        palavras.add("tigris");
        palavras.add("til");
        palavras.add("tilde");
        palavras.add("tile");
        palavras.add("tiled");
        palavras.add("tiles");
        palavras.add("tiling");
        palavras.add("till");
        palavras.add("tillable");
        palavras.add("tilled");
        palavras.add("tiller");
        palavras.add("tillers");
        palavras.add("tilling");
        palavras.add("tills");
        palavras.add("tilt");
        palavras.add("tilted");
        palavras.add("tilth");
        palavras.add("tilting");
        palavras.add("tilts");
        palavras.add("tim");
        palavras.add("timber");
        palavras.add("timbered");
        palavras.add("timbering");
        palavras.add("timberland");
        palavras.add("timbers");
        palavras.add("timbre");
        palavras.add("time");
        palavras.add("timed");
        palavras.add("timely");
        palavras.add("timeout");
        palavras.add("timepiece");
        palavras.add("timer");
        palavras.add("timers");
        palavras.add("times");
        palavras.add("timeshare");
        palavras.add("timesharing");
        palavras.add("timetable");
        palavras.add("timetablexs");
        palavras.add("timetables");
        palavras.add("timeworn");
        palavras.add("timex");
        palavras.add("timid");
        palavras.add("timidity");
        palavras.add("timidly");
        palavras.add("timing");
        palavras.add("timings");
        palavras.add("timon");
        palavras.add("timothy");
        palavras.add("tin");
        palavras.add("tinxs");
        palavras.add("tina");
        palavras.add("tincture");
        palavras.add("tinder");
        palavras.add("tine");
        palavras.add("tinfoil");
        palavras.add("tinge");
        palavras.add("tinged");
        palavras.add("tingle");
        palavras.add("tingled");
        palavras.add("tingles");
        palavras.add("tingling");
        palavras.add("tinier");
        palavras.add("tiniest");
        palavras.add("tinily");
        palavras.add("tininess");
        palavras.add("tinker");
        palavras.add("tinkered");
        palavras.add("tinkering");
        palavras.add("tinkers");
        palavras.add("tinkle");
        palavras.add("tinkled");
        palavras.add("tinkles");
        palavras.add("tinkling");
        palavras.add("tinnier");
        palavras.add("tinniest");
        palavras.add("tinnily");
        palavras.add("tinniness");
        palavras.add("tinning");
        palavras.add("tinny");
        palavras.add("tins");
        palavras.add("tinsel");
        palavras.add("tint");
        palavras.add("tinted");
        palavras.add("tinting");
        palavras.add("tints");
        palavras.add("tintype");
        palavras.add("tiny");
        palavras.add("tioga");
        palavras.add("tip");
        palavras.add("tipxs");
        palavras.add("tipoff");
        palavras.add("tipped");
        palavras.add("tipper");
        palavras.add("tipperxs");
        palavras.add("tipperary");
        palavras.add("tippers");
        palavras.add("tipping");
        palavras.add("tipple");
        palavras.add("tippy");
        palavras.add("tips");
        palavras.add("tipsy");
        palavras.add("tiptoe");
        palavras.add("tirade");
        palavras.add("tirana");
        palavras.add("tire");
        palavras.add("tired");
        palavras.add("tiredly");
        palavras.add("tireless");
        palavras.add("tirelessly");
        palavras.add("tirelessness");
        palavras.add("tires");
        palavras.add("tiresome");
        palavras.add("tiresomely");
        palavras.add("tiresomeness");
        palavras.add("tiring");
        palavras.add("tissue");
        palavras.add("tissuexs");
        palavras.add("tissues");
        palavras.add("tit");
        palavras.add("titan");
        palavras.add("titanate");
        palavras.add("titanic");
        palavras.add("titanium");
        palavras.add("titer");
        palavras.add("titers");
        palavras.add("titfuck");
        palavras.add("tithe");
        palavras.add("tither");
        palavras.add("tithes");
        palavras.add("titian");
        palavras.add("titillate");
        palavras.add("title");
        palavras.add("titled");
        palavras.add("titleist");
        palavras.add("titles");
        palavras.add("titman");
        palavras.add("titmouse");
        palavras.add("titrate");
        palavras.add("tits");
        palavras.add("titular");
        palavras.add("titus");
        palavras.add("tmjxnzzz");
        palavras.add("tn");
        palavras.add("tnt");
        palavras.add("to");
        palavras.add("toad");
        palavras.add("toadxs");
        palavras.add("toads");
        palavras.add("toady");
        palavras.add("toast");
        palavras.add("toasted");
        palavras.add("toaster");
        palavras.add("toasting");
        palavras.add("toastmaster");
        palavras.add("toasts");
        palavras.add("tobacco");
        palavras.add("tobago");
        palavras.add("toby");
        palavras.add("tobydog");
        palavras.add("toccata");
        palavras.add("today");
        palavras.add("todayxll");
        palavras.add("todd");
        palavras.add("toddle");
        palavras.add("toe");
        palavras.add("toexs");
        palavras.add("toefl");
        palavras.add("toejam");
        palavras.add("toenail");
        palavras.add("toes");
        palavras.add("toffee");
        palavras.add("tofu");
        palavras.add("tog");
        palavras.add("together");
        palavras.add("togetherness");
        palavras.add("togging");
        palavras.add("toggle");
        palavras.add("toggled");
        palavras.add("toggles");
        palavras.add("toggling");
        palavras.add("togo");
        palavras.add("togs");
        palavras.add("toil");
        palavras.add("toiled");
        palavras.add("toiler");
        palavras.add("toilet");
        palavras.add("toiletxs");
        palavras.add("toiletry");
        palavras.add("toilets");
        palavras.add("toiling");
        palavras.add("toils");
        palavras.add("toilsome");
        palavras.add("tokamak");
        palavras.add("token");
        palavras.add("tokenxs");
        palavras.add("tokens");
        palavras.add("tokyo");
        palavras.add("told");
        palavras.add("toledo");
        palavras.add("tolerability");
        palavras.add("tolerable");
        palavras.add("tolerably");
        palavras.add("tolerance");
        palavras.add("tolerances");
        palavras.add("tolerant");
        palavras.add("tolerantly");
        palavras.add("tolerate");
        palavras.add("tolerated");
        palavras.add("tolerates");
        palavras.add("tolerating");
        palavras.add("toleration");
        palavras.add("tolkien");
        palavras.add("toll");
        palavras.add("tolled");
        palavras.add("tollgate");
        palavras.add("tollhouse");
        palavras.add("tolls");
        palavras.add("tolstoy");
        palavras.add("toluene");
        palavras.add("tom");
        palavras.add("tomahawk");
        palavras.add("tomahawkxs");
        palavras.add("tomahawks");
        palavras.add("tomato");
        palavras.add("tomatoes");
        palavras.add("tomb");
        palavras.add("tombxs");
        palavras.add("tomblike");
        palavras.add("tombs");
        palavras.add("tombstone");
        palavras.add("tomcat");
        palavras.add("tome");
        palavras.add("tomlinson");
        palavras.add("tommie");
        palavras.add("tommy");
        palavras.add("tommyboy");
        palavras.add("tomograph");
        palavras.add("tomography");
        palavras.add("tomorrow");
        palavras.add("tompkins");
        palavras.add("ton");
        palavras.add("tonxs");
        palavras.add("tonal");
        palavras.add("tone");
        palavras.add("toned");
        palavras.add("toner");
        palavras.add("tones");
        palavras.add("tong");
        palavras.add("tongs");
        palavras.add("tongue");
        palavras.add("tongued");
        palavras.add("tongues");
        palavras.add("toni");
        palavras.add("tonic");
        palavras.add("tonicxs");
        palavras.add("tonics");
        palavras.add("tonight");
        palavras.add("toning");
        palavras.add("tonk");
        palavras.add("tonnage");
        palavras.add("tons");
        palavras.add("tonsil");
        palavras.add("tonsillitis");
        palavras.add("tony");
        palavras.add("too");
        palavras.add("toodle");
        palavras.add("took");
        palavras.add("tool");
        palavras.add("toolbox");
        palavras.add("tooled");
        palavras.add("tooler");
        palavras.add("toolers");
        palavras.add("tooling");
        palavras.add("toolkit");
        palavras.add("toolmake");
        palavras.add("toolman");
        palavras.add("tools");
        palavras.add("toolsmith");
        palavras.add("toomuch");
        palavras.add("toonarmy");
        palavras.add("toot");
        palavras.add("tooth");
        palavras.add("toothbrush");
        palavras.add("toothbrushxs");
        palavras.add("toothbrushes");
        palavras.add("toothpaste");
        palavras.add("toothpick");
        palavras.add("toothpickxs");
        palavras.add("toothpicks");
        palavras.add("tootle");
        palavras.add("tootsie");
        palavras.add("top");
        palavras.add("topaz");
        palavras.add("topcoat");
        palavras.add("topeka");
        palavras.add("toper");
        palavras.add("topgallant");
        palavras.add("topic");
        palavras.add("topicxs");
        palavras.add("topical");
        palavras.add("topically");
        palavras.add("topics");
        palavras.add("topmost");
        palavras.add("topnotch");
        palavras.add("topocentric");
        palavras.add("topography");
        palavras.add("topological");
        palavras.add("topologies");
        palavras.add("topologize");
        palavras.add("topology");
        palavras.add("topping");
        palavras.add("topple");
        palavras.add("toppled");
        palavras.add("topples");
        palavras.add("toppling");
        palavras.add("tops");
        palavras.add("topsoil");
        palavras.add("topspin");
        palavras.add("topsy");
        palavras.add("tor");
        palavras.add("torah");
        palavras.add("torch");
        palavras.add("torchxs");
        palavras.add("torches");
        palavras.add("tore");
        palavras.add("tori");
        palavras.add("toriamos");
        palavras.add("torment");
        palavras.add("tormented");
        palavras.add("tormenter");
        palavras.add("tormenters");
        palavras.add("tormenting");
        palavras.add("torn");
        palavras.add("tornado");
        palavras.add("tornadoes");
        palavras.add("toroid");
        palavras.add("toroidal");
        palavras.add("toronto");
        palavras.add("torpedo");
        palavras.add("torpedoes");
        palavras.add("torpid");
        palavras.add("torpor");
        palavras.add("torque");
        palavras.add("torr");
        palavras.add("torrance");
        palavras.add("torrent");
        palavras.add("torrentxs");
        palavras.add("torrents");
        palavras.add("torrid");
        palavras.add("torsion");
        palavras.add("torso");
        palavras.add("tort");
        palavras.add("tortoise");
        palavras.add("tortoisexs");
        palavras.add("tortoises");
        palavras.add("tortoiseshell");
        palavras.add("tortuous");
        palavras.add("torture");
        palavras.add("tortured");
        palavras.add("torturer");
        palavras.add("torturers");
        palavras.add("tortures");
        palavras.add("torturing");
        palavras.add("torus");
        palavras.add("torusxs");
        palavras.add("toruses");
        palavras.add("tory");
        palavras.add("toshiba");
        palavras.add("toss");
        palavras.add("tossed");
        palavras.add("tosses");
        palavras.add("tossing");
        palavras.add("tot");
        palavras.add("total");
        palavras.add("totaled");
        palavras.add("totaling");
        palavras.add("totalitarian");
        palavras.add("totalities");
        palavras.add("totality");
        palavras.add("totalityxs");
        palavras.add("totalled");
        palavras.add("totaller");
        palavras.add("totallers");
        palavras.add("totalling");
        palavras.add("totally");
        palavras.add("totals");
        palavras.add("tote");
        palavras.add("totem");
        palavras.add("totemic");
        palavras.add("tottenha");
        palavras.add("tottenham");
        palavras.add("totter");
        palavras.add("tottered");
        palavras.add("tottering");
        palavras.add("totters");
        palavras.add("touch");
        palavras.add("touchable");
        palavras.add("touchdown");
        palavras.add("touched");
        palavras.add("touches");
        palavras.add("touchier");
        palavras.add("touchiest");
        palavras.add("touchily");
        palavras.add("touchiness");
        palavras.add("touching");
        palavras.add("touchingly");
        palavras.add("touchstone");
        palavras.add("touchy");
        palavras.add("tough");
        palavras.add("toughen");
        palavras.add("tougher");
        palavras.add("toughest");
        palavras.add("toughly");
        palavras.add("toughness");
        palavras.add("tour");
        palavras.add("toured");
        palavras.add("touring");
        palavras.add("tourist");
        palavras.add("touristxs");
        palavras.add("tourists");
        palavras.add("tournament");
        palavras.add("tournamentxs");
        palavras.add("tournaments");
        palavras.add("tours");
        palavras.add("tousle");
        palavras.add("tout");
        palavras.add("tow");
        palavras.add("toward");
        palavras.add("towards");
        palavras.add("towboat");
        palavras.add("towed");
        palavras.add("towel");
        palavras.add("toweling");
        palavras.add("towelled");
        palavras.add("towelling");
        palavras.add("towels");
        palavras.add("tower");
        palavras.add("towered");
        palavras.add("towering");
        palavras.add("towers");
        palavras.add("towhead");
        palavras.add("towhee");
        palavras.add("town");
        palavras.add("townxs");
        palavras.add("townhouse");
        palavras.add("towns");
        palavras.add("townsend");
        palavras.add("township");
        palavras.add("townshipxs");
        palavras.add("townships");
        palavras.add("townsman");
        palavras.add("townsmen");
        palavras.add("toxic");
        palavras.add("toxicology");
        palavras.add("toxin");
        palavras.add("toy");
        palavras.add("toyed");
        palavras.add("toying");
        palavras.add("toyota");
        palavras.add("toys");
        palavras.add("trace");
        palavras.add("traceable");
        palavras.add("traced");
        palavras.add("tracer");
        palavras.add("tracers");
        palavras.add("tracery");
        palavras.add("traces");
        palavras.add("trachea");
        palavras.add("tracing");
        palavras.add("tracings");
        palavras.add("track");
        palavras.add("trackage");
        palavras.add("tracked");
        palavras.add("tracker");
        palavras.add("trackers");
        palavras.add("tracking");
        palavras.add("tracks");
        palavras.add("tract");
        palavras.add("tractxs");
        palavras.add("tractability");
        palavras.add("tractable");
        palavras.add("tractive");
        palavras.add("tractor");
        palavras.add("tractorxs");
        palavras.add("tractors");
        palavras.add("tracts");
        palavras.add("tracy");
        palavras.add("trade");
        palavras.add("traded");
        palavras.add("trademark");
        palavras.add("trademarkxs");
        palavras.add("trademarks");
        palavras.add("tradeoff");
        palavras.add("trader");
        palavras.add("traders");
        palavras.add("trades");
        palavras.add("tradesman");
        palavras.add("tradesmen");
        palavras.add("trading");
        palavras.add("tradition");
        palavras.add("traditionxs");
        palavras.add("traditional");
        palavras.add("traditionally");
        palavras.add("traditions");
        palavras.add("traffic");
        palavras.add("trafficxs");
        palavras.add("trafficked");
        palavras.add("trafficker");
        palavras.add("traffickerxs");
        palavras.add("traffickers");
        palavras.add("trafficking");
        palavras.add("traffics");
        palavras.add("trag");
        palavras.add("tragedian");
        palavras.add("tragedies");
        palavras.add("tragedy");
        palavras.add("tragedyxs");
        palavras.add("tragic");
        palavras.add("tragically");
        palavras.add("tragicomic");
        palavras.add("trail");
        palavras.add("trailblaze");
        palavras.add("trailed");
        palavras.add("trailer");
        palavras.add("trailers");
        palavras.add("trailhead");
        palavras.add("trailing");
        palavras.add("trailings");
        palavras.add("trails");
        palavras.add("trailside");
        palavras.add("train");
        palavras.add("trained");
        palavras.add("trainee");
        palavras.add("traineexs");
        palavras.add("trainees");
        palavras.add("trainer");
        palavras.add("trainers");
        palavras.add("training");
        palavras.add("trainman");
        palavras.add("trainmen");
        palavras.add("trains");
        palavras.add("traipse");
        palavras.add("trait");
        palavras.add("traitxs");
        palavras.add("traitor");
        palavras.add("traitorxs");
        palavras.add("traitorous");
        palavras.add("traitors");
        palavras.add("traits");
        palavras.add("trajectories");
        palavras.add("trajectory");
        palavras.add("trajectoryxs");
        palavras.add("tram");
        palavras.add("trammel");
        palavras.add("tramp");
        palavras.add("tramped");
        palavras.add("tramping");
        palavras.add("trample");
        palavras.add("trampled");
        palavras.add("trampler");
        palavras.add("tramples");
        palavras.add("trampling");
        palavras.add("tramps");
        palavras.add("tramway");
        palavras.add("trance");
        palavras.add("trancexs");
        palavras.add("trances");
        palavras.add("tranny");
        palavras.add("tranquil");
        palavras.add("tranquility");
        palavras.add("tranquillity");
        palavras.add("tranquilly");
        palavras.add("transact");
        palavras.add("transaction");
        palavras.add("transactionxs");
        palavras.add("transactions");
        palavras.add("transalpine");
        palavras.add("transam");
        palavras.add("transatlantic");
        palavras.add("transceiver");
        palavras.add("transcend");
        palavras.add("transcended");
        palavras.add("transcendent");
        palavras.add("transcendental");
        palavras.add("transcending");
        palavras.add("transcends");
        palavras.add("transconductance");
        palavras.add("transcontinental");
        palavras.add("transcribe");
        palavras.add("transcribed");
        palavras.add("transcriber");
        palavras.add("transcribers");
        palavras.add("transcribes");
        palavras.add("transcribing");
        palavras.add("transcript");
        palavras.add("transcriptxs");
        palavras.add("transcription");
        palavras.add("transcriptionxs");
        palavras.add("transcriptions");
        palavras.add("transcripts");
        palavras.add("transducer");
        palavras.add("transduction");
        palavras.add("transect");
        palavras.add("transept");
        palavras.add("transexual");
        palavras.add("transfer");
        palavras.add("transferxs");
        palavras.add("transferable");
        palavras.add("transferal");
        palavras.add("transferalxs");
        palavras.add("transferals");
        palavras.add("transferee");
        palavras.add("transference");
        palavras.add("transferor");
        palavras.add("transferral");
        palavras.add("transferred");
        palavras.add("transferrer");
        palavras.add("transferrerxs");
        palavras.add("transferrers");
        palavras.add("transferring");
        palavras.add("transfers");
        palavras.add("transfinite");
        palavras.add("transfix");
        palavras.add("transform");
        palavras.add("transformable");
        palavras.add("transformation");
        palavras.add("transformationxs");
        palavras.add("transformational");
        palavras.add("transformations");
        palavras.add("transformed");
        palavras.add("transformer");
        palavras.add("transformers");
        palavras.add("transforming");
        palavras.add("transforms");
        palavras.add("transfusable");
        palavras.add("transfuse");
        palavras.add("transfusion");
        palavras.add("transgress");
        palavras.add("transgressed");
        palavras.add("transgression");
        palavras.add("transgressionxs");
        palavras.add("transgressions");
        palavras.add("transgressor");
        palavras.add("transient");
        palavras.add("transiently");
        palavras.add("transients");
        palavras.add("transistor");
        palavras.add("transistorxs");
        palavras.add("transistors");
        palavras.add("transit");
        palavras.add("transite");
        palavras.add("transition");
        palavras.add("transitional");
        palavras.add("transitioned");
        palavras.add("transitions");
        palavras.add("transitive");
        palavras.add("transitively");
        palavras.add("transitiveness");
        palavras.add("transitivity");
        palavras.add("transitory");
        palavras.add("translatability");
        palavras.add("translatable");
        palavras.add("translate");
        palavras.add("translated");
        palavras.add("translates");
        palavras.add("translating");
        palavras.add("translation");
        palavras.add("translational");
        palavras.add("translations");
        palavras.add("translator");
        palavras.add("translatorxs");
        palavras.add("translators");
        palavras.add("transliterate");
        palavras.add("translucent");
        palavras.add("transmissible");
        palavras.add("transmission");
        palavras.add("transmissionxs");
        palavras.add("transmissions");
        palavras.add("transmit");
        palavras.add("transmits");
        palavras.add("transmittable");
        palavras.add("transmittal");
        palavras.add("transmittance");
        palavras.add("transmitted");
        palavras.add("transmitter");
        palavras.add("transmitterxs");
        palavras.add("transmitters");
        palavras.add("transmitting");
        palavras.add("transmogrification");
        palavras.add("transmogrify");
        palavras.add("transmutation");
        palavras.add("transmute");
        palavras.add("transoceanic");
        palavras.add("transom");
        palavras.add("transpacific");
        palavras.add("transparencies");
        palavras.add("transparency");
        palavras.add("transparencyxs");
        palavras.add("transparent");
        palavras.add("transparently");
        palavras.add("transpiration");
        palavras.add("transpire");
        palavras.add("transpired");
        palavras.add("transpires");
        palavras.add("transpiring");
        palavras.add("transplant");
        palavras.add("transplantation");
        palavras.add("transplanted");
        palavras.add("transplanting");
        palavras.add("transplants");
        palavras.add("transpond");
        palavras.add("transport");
        palavras.add("transportability");
        palavras.add("transportation");
        palavras.add("transported");
        palavras.add("transporter");
        palavras.add("transporters");
        palavras.add("transporting");
        palavras.add("transports");
        palavras.add("transposable");
        palavras.add("transpose");
        palavras.add("transposed");
        palavras.add("transposes");
        palavras.add("transposing");
        palavras.add("transposition");
        palavras.add("transship");
        palavras.add("transshipped");
        palavras.add("transshipping");
        palavras.add("transversal");
        palavras.add("transverse");
        palavras.add("transvestite");
        palavras.add("transylvania");
        palavras.add("trap");
        palavras.add("trapxs");
        palavras.add("trapezium");
        palavras.add("trapezoid");
        palavras.add("trapezoidxs");
        palavras.add("trapezoidal");
        palavras.add("trapezoids");
        palavras.add("trapped");
        palavras.add("trapper");
        palavras.add("trapperxs");
        palavras.add("trappers");
        palavras.add("trapping");
        palavras.add("trappings");
        palavras.add("traps");
        palavras.add("trash");
        palavras.add("trashy");
        palavras.add("trastevere");
        palavras.add("trauma");
        palavras.add("traumatic");
        palavras.add("travail");
        palavras.add("travel");
        palavras.add("traveled");
        palavras.add("traveler");
        palavras.add("travelers");
        palavras.add("traveling");
        palavras.add("travelings");
        palavras.add("travelogue");
        palavras.add("travels");
        palavras.add("traversable");
        palavras.add("traversal");
        palavras.add("traversalxs");
        palavras.add("traversals");
        palavras.add("traverse");
        palavras.add("traversed");
        palavras.add("traverses");
        palavras.add("traversing");
        palavras.add("travertine");
        palavras.add("travesties");
        palavras.add("travesty");
        palavras.add("travestyxs");
        palavras.add("travis");
        palavras.add("trawl");
        palavras.add("tray");
        palavras.add("trayxs");
        palavras.add("trays");
        palavras.add("treacheries");
        palavras.add("treacherous");
        palavras.add("treacherously");
        palavras.add("treachery");
        palavras.add("treacheryxs");
        palavras.add("tread");
        palavras.add("treading");
        palavras.add("treadle");
        palavras.add("treadmill");
        palavras.add("treads");
        palavras.add("treason");
        palavras.add("treasonous");
        palavras.add("treasure");
        palavras.add("treasured");
        palavras.add("treasurer");
        palavras.add("treasures");
        palavras.add("treasuries");
        palavras.add("treasuring");
        palavras.add("treasury");
        palavras.add("treasuryxs");
        palavras.add("treat");
        palavras.add("treated");
        palavras.add("treaties");
        palavras.add("treating");
        palavras.add("treatise");
        palavras.add("treatisexs");
        palavras.add("treatises");
        palavras.add("treatment");
        palavras.add("treatmentxs");
        palavras.add("treatments");
        palavras.add("treats");
        palavras.add("treaty");
        palavras.add("treatyxs");
        palavras.add("treble");
        palavras.add("tree");
        palavras.add("treexs");
        palavras.add("treefrog");
        palavras.add("treelike");
        palavras.add("trees");
        palavras.add("treetop");
        palavras.add("treetopxs");
        palavras.add("treetops");
        palavras.add("trefoil");
        palavras.add("trek");
        palavras.add("trekxs");
        palavras.add("trekking");
        palavras.add("treks");
        palavras.add("trellis");
        palavras.add("tremble");
        palavras.add("trembled");
        palavras.add("trembles");
        palavras.add("trembling");
        palavras.add("tremendous");
        palavras.add("tremendously");
        palavras.add("tremor");
        palavras.add("tremorxs");
        palavras.add("tremors");
        palavras.add("tremulous");
        palavras.add("trench");
        palavras.add("trenchant");
        palavras.add("trencher");
        palavras.add("trencherman");
        palavras.add("trenchermen");
        palavras.add("trenches");
        palavras.add("trend");
        palavras.add("trending");
        palavras.add("trends");
        palavras.add("trendy");
        palavras.add("trenton");
        palavras.add("trepidation");
        palavras.add("trespass");
        palavras.add("trespassed");
        palavras.add("trespasser");
        palavras.add("trespassers");
        palavras.add("trespasses");
        palavras.add("tress");
        palavras.add("tressxs");
        palavras.add("tresses");
        palavras.add("trestle");
        palavras.add("trevelyan");
        palavras.add("trizaz");
        palavras.add("triable");
        palavras.add("triac");
        palavras.add("triad");
        palavras.add("trial");
        palavras.add("trialxs");
        palavras.add("trials");
        palavras.add("triangle");
        palavras.add("trianglexs");
        palavras.add("triangles");
        palavras.add("triangular");
        palavras.add("triangularly");
        palavras.add("triangulate");
        palavras.add("triangulum");
        palavras.add("trianon");
        palavras.add("triassic");
        palavras.add("triatomic");
        palavras.add("tribal");
        palavras.add("tribble");
        palavras.add("tribe");
        palavras.add("tribexs");
        palavras.add("tribes");
        palavras.add("tribesman");
        palavras.add("tribesmen");
        palavras.add("tribulate");
        palavras.add("tribunal");
        palavras.add("tribunalxs");
        palavras.add("tribunals");
        palavras.add("tribune");
        palavras.add("tribunexs");
        palavras.add("tribunes");
        palavras.add("tributary");
        palavras.add("tribute");
        palavras.add("tributexs");
        palavras.add("tributes");
        palavras.add("triceratops");
        palavras.add("trichinella");
        palavras.add("trichloroacetic");
        palavras.add("trichloroethane");
        palavras.add("trichotomy");
        palavras.add("trichrome");
        palavras.add("tricia");
        palavras.add("trick");
        palavras.add("tricked");
        palavras.add("trickery");
        palavras.add("trickier");
        palavras.add("trickiest");
        palavras.add("trickiness");
        palavras.add("tricking");
        palavras.add("trickle");
        palavras.add("trickled");
        palavras.add("trickles");
        palavras.add("trickling");
        palavras.add("tricks");
        palavras.add("trickster");
        palavras.add("tricky");
        palavras.add("trident");
        palavras.add("tridiagonal");
        palavras.add("tried");
        palavras.add("triennial");
        palavras.add("trier");
        palavras.add("triers");
        palavras.add("tries");
        palavras.add("trifle");
        palavras.add("trifler");
        palavras.add("trifles");
        palavras.add("trifling");
        palavras.add("trifluoride");
        palavras.add("trifluouride");
        palavras.add("trig");
        palavras.add("trigger");
        palavras.add("triggered");
        palavras.add("triggering");
        palavras.add("triggers");
        palavras.add("trigonal");
        palavras.add("trigonometric");
        palavras.add("trigonometry");
        palavras.add("trigram");
        palavras.add("trihedral");
        palavras.add("trill");
        palavras.add("trilled");
        palavras.add("trillion");
        palavras.add("trillions");
        palavras.add("trillionth");
        palavras.add("trilobite");
        palavras.add("trilogy");
        palavras.add("trim");
        palavras.add("trimer");
        palavras.add("trimester");
        palavras.add("trimly");
        palavras.add("trimmed");
        palavras.add("trimmer");
        palavras.add("trimmest");
        palavras.add("trimming");
        palavras.add("trimmings");
        palavras.add("trimness");
        palavras.add("trims");
        palavras.add("trinidad");
        palavras.add("trinitarian");
        palavras.add("trinitro");
        palavras.add("trinity");
        palavras.add("trinket");
        palavras.add("trinketxs");
        palavras.add("trinkets");
        palavras.add("trio");
        palavras.add("triode");
        palavras.add("trioxide");
        palavras.add("trip");
        palavras.add("tripxs");
        palavras.add("tripartite");
        palavras.add("tripe");
        palavras.add("triphenylphosphine");
        palavras.add("triple");
        palavras.add("tripled");
        palavras.add("triples");
        palavras.add("triplet");
        palavras.add("tripletxs");
        palavras.add("triplets");
        palavras.add("triplett");
        palavras.add("triplex");
        palavras.add("triplicate");
        palavras.add("tripling");
        palavras.add("tripod");
        palavras.add("tripoli");
        palavras.add("tripper");
        palavras.add("tripping");
        palavras.add("trips");
        palavras.add("triptych");
        palavras.add("trisha");
        palavras.add("trisodium");
        palavras.add("tristan");
        palavras.add("tristate");
        palavras.add("trisyllable");
        palavras.add("trite");
        palavras.add("tritium");
        palavras.add("triton");
        palavras.add("triumph");
        palavras.add("triumphal");
        palavras.add("triumphant");
        palavras.add("triumphantly");
        palavras.add("triumphed");
        palavras.add("triumphing");
        palavras.add("triumphs");
        palavras.add("triune");
        palavras.add("trivalent");
        palavras.add("trivia");
        palavras.add("trivial");
        palavras.add("trivialities");
        palavras.add("triviality");
        palavras.add("trivially");
        palavras.add("trivium");
        palavras.add("trixie");
        palavras.add("trod");
        palavras.add("trodden");
        palavras.add("troglodyte");
        palavras.add("troika");
        palavras.add("trojan");
        palavras.add("troll");
        palavras.add("trollxs");
        palavras.add("trolley");
        palavras.add("trolleyxs");
        palavras.add("trolleys");
        palavras.add("trollop");
        palavras.add("trolls");
        palavras.add("trombone");
        palavras.add("trompe");
        palavras.add("troop");
        palavras.add("trooper");
        palavras.add("troopers");
        palavras.add("troops");
        palavras.add("trophic");
        palavras.add("trophies");
        palavras.add("trophy");
        palavras.add("trophyxs");
        palavras.add("tropic");
        palavras.add("tropicxs");
        palavras.add("tropical");
        palavras.add("tropics");
        palavras.add("tropopause");
        palavras.add("troposphere");
        palavras.add("tropospheric");
        palavras.add("trot");
        palavras.add("trots");
        palavras.add("trotting");
        palavras.add("troubador");
        palavras.add("trouble");
        palavras.add("troubled");
        palavras.add("troublemaker");
        palavras.add("troublemakerxs");
        palavras.add("troublemakers");
        palavras.add("troubles");
        palavras.add("troubleshoot");
        palavras.add("troubleshooter");
        palavras.add("troubleshooters");
        palavras.add("troubleshooting");
        palavras.add("troubleshoots");
        palavras.add("troublesome");
        palavras.add("troublesomely");
        palavras.add("troubling");
        palavras.add("trough");
        palavras.add("trounce");
        palavras.add("troupe");
        palavras.add("trouser");
        palavras.add("trousers");
        palavras.add("trout");
        palavras.add("troutman");
        palavras.add("trowel");
        palavras.add("trowelxs");
        palavras.add("trowels");
        palavras.add("troy");
        palavras.add("truancy");
        palavras.add("truant");
        palavras.add("truantxs");
        palavras.add("truants");
        palavras.add("truce");
        palavras.add("truck");
        palavras.add("trucked");
        palavras.add("trucker");
        palavras.add("truckers");
        palavras.add("trucking");
        palavras.add("trucks");
        palavras.add("truculent");
        palavras.add("trudge");
        palavras.add("trudged");
        palavras.add("trudy");
        palavras.add("true");
        palavras.add("trueblue");
        palavras.add("trued");
        palavras.add("truelove");
        palavras.add("truer");
        palavras.add("trues");
        palavras.add("truest");
        palavras.add("truing");
        palavras.add("truism");
        palavras.add("truismxs");
        palavras.add("truisms");
        palavras.add("truly");
        palavras.add("truman");
        palavras.add("trumbull");
        palavras.add("trump");
        palavras.add("trumped");
        palavras.add("trumpery");
        palavras.add("trumpet");
        palavras.add("trumpeter");
        palavras.add("trumps");
        palavras.add("truncate");
        palavras.add("truncated");
        palavras.add("truncates");
        palavras.add("truncating");

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
