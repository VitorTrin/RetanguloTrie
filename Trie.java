/**
 * Created by vitor on 19/11/16.
 */

import java.util.ArrayList;
import java.util.List;

public class Trie {

    private TrieNo raiz;

    public Trie()
    {
        raiz = new TrieNo();
    }

    public void adicionaPalavra(String palavra)
    {
        raiz.adicionaPalavra(palavra);
    }


    public List getPalavras( String prefixo)
    {
        TrieNo ultimoNo = raiz;
        for( int i = 0 ; i < prefixo.length(); i++)
        {
            ultimoNo = ultimoNo.getNo(prefixo.charAt(i));

            if(ultimoNo == null) //não achou a palavra
                return new ArrayList();

        }

        return ultimoNo.getPalavras();

    }


    public List getPalavrasTamanho(String prefixo, int tamanho)
    {
        TrieNo ultimoNo = raiz;
        for( int i = 0 ; i < prefixo.length(); i++)
        {
            ultimoNo = ultimoNo.getNo(prefixo.charAt(i));

            if(ultimoNo == null) //não achou a palavra
                return new ArrayList();

        }

        return ultimoNo.getPalavrasTamanho(tamanho);
    }

    public boolean achaAlgum(String prefixo, int tamanho)
    {
        TrieNo ultimoNo = raiz;
        for( int i = 0 ; i < prefixo.length(); i++)
        {
            ultimoNo = ultimoNo.getNo(prefixo.charAt(i));

            if(ultimoNo == null) //não achou a palavra
                return false;

        }

        return ultimoNo.achaAlgum(tamanho);
    }


}



