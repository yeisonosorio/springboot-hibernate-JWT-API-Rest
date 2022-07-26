package com.cursojava.curso.dao;

import com.cursojava.curso.models.Usuario;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;


@Repository
@Transactional
public class UsuarioDaoImp implements UsuarioDao{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Usuario> getUsuarios() {
        String query = "FROM Usuario";
        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public void eliminar(long id) {
    Usuario usuario = entityManager.find(Usuario.class, id);
        entityManager.remove(usuario);
    }

    @Override
    public void registrar(Usuario usuario) {
        entityManager.merge(usuario);
    }

    @Override
    public Usuario obtenerUsuariosCredenciales(Usuario usuario) {
        String query = "FROM Usuario WHERE email = :email" ;
        //recuperamos el usuario de la base de datos
         List<Usuario> lista = entityManager.createQuery(query)
                .setParameter("email" ,usuario.getEmail())
                .getResultList();
        if (lista.isEmpty()){
            return  null;
        }
         String passwordHas = lista.get(0).getPassword();
        Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
       if( argon2.verify(passwordHas, usuario.getPassword())){
           return lista.get(0);
       }return null;

    }


}
