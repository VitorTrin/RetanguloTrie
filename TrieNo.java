/**
 * Created by vitor on 19/11/16.
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TrieNo
{

    public TrieNo pai;
//    private TrieNo[] filhos;
    public HashMap<Character,TrieNo> filhos;
    public boolean eFolha; // pra saber se tem filhos
    public boolean ePalavra;
    public char chave;
    public int profundidade;

    public TrieNo()
    { //usado pela raiz

        filhos = new HashMap<>(); //caracteres no alfabeto
        eFolha = true;
        ePalavra = false;
        profundidade = 0;
    }

    public TrieNo(char caractere)
    {

        this();
        this.chave = caractere;

    }

    protected void adicionaPalavra(String palavra) {

        eFolha = false;
        Character charPos = palavra.charAt(0); //pra indexar o array com os char. Apenas minúsculas

        if(filhos.get(charPos) == null)
        {// não existe

            TrieNo novo = new TrieNo( palavra.charAt(0));
            novo.pai = this;
            novo.profundidade = profundidade + 1;
            filhos.put(charPos, novo);
        }

        if (palavra.length() > 1)
        {
            filhos.get(charPos).adicionaPalavra(palavra.substring(1));
        }

        else //acabou a palavra
        {
            filhos.get(charPos).ePalavra = true;
        }

    }

    protected TrieNo getNo (char c)
    {
        return filhos.get(c);
    }

    protected List getPalavras()
    {
        List lista = new ArrayList();

        if(ePalavra)
        {
            lista.add(toString());
        }

//        if(!eFolha)
//        {
//            for( int i =0; i < filhos.length; i++)
//            {
//                if ( filhos[i] != null)
//                {
//                    lista.addAll(filhos[i].getPalavras());
//                }
//            }
//        }
        if(!eFolha)
        {
            if(filhos != null && !filhos.isEmpty())
            for ( Character key : filhos.keySet())
            {
                    lista.addAll(filhos.get(key).getPalavras());
            }
        }

        return lista;
    }

    protected List getPalavrasTamanho(int tamanho) // vai testar o tamanho em cada nó. Vale a pena, pq para cedo?
    {
        List lista = new ArrayList();

        if(ePalavra && profundidade == tamanho)
        {
                lista.add(toString());
        }

//        if(!eFolha && (profundidade + 1) <= tamanho)
//        {
//            for( int i =0; i < filhos.length; i++)
//            {
//                if ( filhos[i] != null)
//                {
//                    lista.addAll(filhos[i].getPalavrasTamanho(tamanho));
//                }
//            }
//        }
        if(!eFolha && (profundidade + 1) <= tamanho)
        {
            if(filhos != null && !filhos.isEmpty())
            for ( Character key : filhos.keySet())
            {
                lista.addAll(filhos.get(key).getPalavrasTamanho(tamanho));
            }
        }


        return lista;
    }

    protected boolean achaAlgum(int tamanho) // vai testar o tamanho em cada nó. Vale a pena, pq para cedo?
    {
        List lista = new ArrayList();

        if(ePalavra && profundidade == tamanho)
        {
            return true;
        }

//        if(!eFolha && (profundidade + 1) <= tamanho)
//        {
//            for( int i =0; i < filhos.length; i++)
//            {
//                if ( filhos[i] != null)
//                {
//                    return filhos[i].achaAlgum(tamanho);
//                }
//            }
//        }

        if(!eFolha && (profundidade + 1) <= tamanho)
        {
            if(filhos != null && !filhos.isEmpty())
                for ( Character key : filhos.keySet())
                {
                    return filhos.get(key).achaAlgum(tamanho);
                }

        }

        return false;

    }

    public String toString() // a string q esse nó representa. Complexidade: O(n), sendo n o tamanho da string

    {

        if (pai == null)

        {

            return "";

        }

        else

        {

            return pai.toString() + new String(new char[] {chave});

        }

    }





}
