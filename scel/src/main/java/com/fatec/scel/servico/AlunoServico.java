package com.fatec.scel.servico;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import com.fatec.scel.model.Endereco;
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
	
	public ModelAndView save(Aluno aluno) {
		ModelAndView modelAndView = new ModelAndView("consultarAluno");
		try {
			String endereco = obtemEndereco(aluno.getCep());
			if (endereco != "") {
				logger.info(">>>>>> 4. cep valido antes do save ==> " + endereco.toString());
				aluno.setEndereco(endereco);
				repository.save(aluno);
				modelAndView.addObject("alunos", repository.findAll());
			}

		} catch (Exception e) { // captura validacoes na camada de persistencia
			modelAndView.setViewName("cadastrarAluno");
			modelAndView.addObject("message", "Dados invalidos");
			logger.error(">>>>>> 4. erro nao esperado ==> " + e.getMessage());
		}
		return modelAndView;
	}
	public String obtemEndereco(String cep) {
		RestTemplate template = new RestTemplate();
		String url = "https://viacep.com.br/ws/{cep}/json/";
		Endereco endereco = template.getForObject(url, Endereco.class, cep);
		logger.info(">>>>>>> 3. obtem endereco ==> " + endereco.toString());
		return endereco.getLogradouro();
	}
	

}
