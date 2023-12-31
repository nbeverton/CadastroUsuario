package infra;

import modelo.Pessoa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class DAO<E> {

    private static EntityManagerFactory emf;
    private EntityManager em;
    private Class<E> classe;

    static {
        try{
            emf = Persistence.createEntityManagerFactory("projeto-CadastroPessoas");
        } catch (Exception e){

        }
    }

    public DAO(){
        this(null);
    }
    public DAO(Class<E> classe){
        this.classe = classe;
        em = emf.createEntityManager();
    }

    public DAO<E> abrirTransacao(){
        em.getTransaction().begin();
        return this;
    }

    public DAO<E> fecharTransacao(){
        em.getTransaction().commit();
        return this;
    }

    public DAO<E> incluir(E entidade){
        em.persist(entidade);
        return this;
    }

    public DAO<E> remover(Long id){
        E entidade = em.find(classe, id);
        if (entidade != null){
        em.remove(entidade);
        }
        return this;
    }

    public E consultarPorId(Long id){
        E entidade = em.find(classe, id);
        return entidade;
    }

    //Método para processamento de todos os comandos:
    public DAO<E> incluirAtomico(E entidade) {
        return this.abrirTransacao().incluir(entidade).fecharTransacao();
    }

    public void fechar(){
        em.close();
    }
}
