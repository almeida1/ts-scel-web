package com.fatec.scel.servico;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import com.fatec.scel.model.Endereco;
import com.fatec.scel.model.Livro;
import com.fatec.scel.model.Aluno;
import com.fatec.scel.model.AlunoRepository;
@Service
public class AlunoServico {
	Logger logger = LogManager.getLogger(AlunoServico.class);
	@Autowired
	private AlunoRepository repository;

	public Iterable<Aluno> findAll() {
		return repository.findAll();
	}
	public Aluno findByRa(String ra) {
		return repository.findByRa(ra);
	}
	public void deleteById(Long id) {
		repository.deleteById(id);
	}
	public Aluno findById(Long id) {
		return repository.findById(id).get();
	}
	
	public void save(Aluno aluno) {
		repository.save(aluno);
	}
	

}
