package com.fatec.scel.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import com.fatec.scel.model.Aluno;
import com.fatec.scel.model.Emprestimo;
import com.fatec.scel.model.Livro;
import com.fatec.scel.servico.AlunoServico;
import com.fatec.scel.servico.EmprestimoServico;
import com.fatec.scel.servico.LivroServico;

@Controller
@RequestMapping(path = "/sig")
public class EmprestimoController {
	Logger logger = LogManager.getLogger(EmprestimoController.class);
	@Autowired
	EmprestimoServico emprestimoServico;
	@Autowired
	private LivroServico livroServico;
	@Autowired
	private AlunoServico alunoServico;
	
	@GetMapping("/emprestimos")
	public ModelAndView consultaEmprestimo() {
		logger.info("1. form consultarEmprestimo chamado ");
		ModelAndView modelAndView = new ModelAndView("consultarEmprestimo");
		modelAndView.addObject("emprestimos", emprestimoServico.findAll());
		logger.info("2. carregou objeto emprestimos com dados ");
		return modelAndView;
	}

	@GetMapping("/emprestimo")
	public ModelAndView registrarEmprestimo(Emprestimo emprestimo) {
		logger.info("retorna form registrarEmprestimo chamada da classe controller");
		ModelAndView mv = new ModelAndView("registrarEmprestimo");
		mv.addObject("emprestimo", emprestimo);
		return mv;
	}

	@PostMapping("/emprestimos")
	public ModelAndView save(@Valid Emprestimo emprestimo, BindingResult result) {
		logger.info("registra o emprestimo - save");
		ModelAndView modelAndView = new ModelAndView("registrarEmprestimo");
		if (result.hasErrors()) {
			logger.info("entrada de dados invalida na pagina registrar emprestimo");
			return modelAndView;
		}
		try {
			Livro livro = null;
			Aluno aluno = null;
			livro = livroServico.findByIsbn(emprestimo.getIsbn());
			aluno = alunoServico.findByRa(emprestimo.getRa());
			List<Emprestimo> emprestimos = emprestimoServico.findByIsbnRa(emprestimo.getIsbn(), emprestimo.getRa());
			boolean emprestimoEmAberto = false;
			for (Emprestimo umEmprestimo : emprestimos) {
				if (umEmprestimo.getDataDevolucao() == null) {
					emprestimoEmAberto = true;
				}
			}
			if ((livro != null && aluno != null && emprestimos == null)
					|| (livro != null && aluno != null && emprestimoEmAberto == false)) {
				logger.info(" achou livro/aluno no db e nao existe emprestimo cadastrado");
				DateTime dataAtual = new DateTime();
				emprestimo.setDataEmprestimo(dataAtual);
				emprestimo.setDataDevolucaoPrevista();
				emprestimoServico.save(emprestimo);
				modelAndView.addObject("message", "Emprestimo registrado");
			} else {
				logger.info("não achou livro/aluno no db");
				modelAndView.addObject("message", "Livro/Aluno não localizado ou existe emprestimo em aberto");
			}
		} catch (Exception e) {
			logger.info("erro nao esperado no cadastro de emprestimo ===> " + e.getMessage());
		}
		return modelAndView;
	}
	

	@GetMapping("/emprestimos/{id}")
	public ModelAndView devolucao(@PathVariable("id") Long id) {
		logger.info("retorna o form de consulta emprestimo com a baixa registrada");
		Optional<Emprestimo> optionalEmprestimo = emprestimoServico.findById(id);
		Emprestimo emprestimo = optionalEmprestimo.get();
		
		DateTime dataAtual = new DateTime();
		DateTimeFormatter fmt = DateTimeFormat.forPattern("YYYY/MM/dd");
		emprestimo.setDataDevolucao(dataAtual.toString(fmt));
		emprestimoServico.save(emprestimo);
		ModelAndView modelAndView = new ModelAndView("consultarEmprestimo");
		modelAndView.addObject("emprestimos", emprestimoServico.findAll());
		return modelAndView;
	}
}