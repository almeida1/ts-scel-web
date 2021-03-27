package com.fatec.scel;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.fatec.scel.model.Emprestimo;
import com.fatec.scel.model.EmprestimoRepository;

@DataJpaTest
class REQ04RegistrarEmprestimoTests {
	@Autowired
	private EmprestimoRepository emprestimoRepository;

	@Test
	void quando_componente_injetado_not_null() {
		assertNotNull(emprestimoRepository);
	}

	@Test
	void quando_registra_emprestimo_pesquisa_pelo_ra_isbn() {
		emprestimoRepository.save(new Emprestimo("1111", "2222"));
		assertNotNull(emprestimoRepository.findByIsbnRa("1111", "2222"));
	}

	@Test
	void quando_data_atual_eh_hoje_data_devolucao_eh_hoje_mais_oito_dias() {
		//Iterable<Emprestimo> emprestimo = emprestimoServico.findAll();
		//if (emprestimo != null) System.out.println(">>>>>>>>> emprestimo nao nulo");
		//emprestimo.forEach(e -> {if (e.getDataDevolucaoPrevista() != null) System.out.println(">>>>>>>" + e.getDataDevolucaoPrevista());});
		Emprestimo emprestimo = new Emprestimo("1111", "2222");
        
        DateTime dataAtual = new DateTime();
        emprestimo.setDataEmprestimo(dataAtual);
        emprestimo.setDataDevolucaoPrevista();
		emprestimoRepository.save(emprestimo);
		List<Emprestimo> emprestimos = emprestimoRepository.findByIsbnRa("1111", "2222");
		Emprestimo umEmprestimo = emprestimos.get(0);
		//efetua o calculo de data para comparar com o resultado obtido
		DateTimeFormatter fmt = DateTimeFormat.forPattern("YYYY/MM/dd");
		DateTime data = fmt.parseDateTime(emprestimo.getDataEmprestimo());
		String dataDevolucaoPrevista = data.plusDays(8).toString(fmt);
		assertEquals(dataDevolucaoPrevista, umEmprestimo.getDataDevolucaoPrevista());

	}

}
