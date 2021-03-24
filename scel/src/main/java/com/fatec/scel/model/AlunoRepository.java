package com.fatec.scel.model;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
@Repository
public interface AlunoRepository extends CrudRepository<Aluno, Long> {
    List<Aluno> findByNome(String nome);
    public Aluno findByRa(@Param("ra") String ra);
}
