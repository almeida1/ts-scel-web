package com.fatec.scel.po;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

class REQ021CadastrarLivro {

	private WebDriver driver;
	private Map<String, Object> vars;
	JavascriptExecutor js;
	
	public void setup() {
		System.setProperty("webdriver.chrome.driver", "browserDriver/chromedriver.exe");
		ChromeOptions chromeOptions = new ChromeOptions();
		chromeOptions.addArguments("--remote-allow-origins=*", "ignore-certificate-errors");
		driver = new ChromeDriver(chromeOptions);
		driver.get("http://localhost:8080");
	}
	
	@Test
	public void ct01_cadastrar_livro_com_sucesso() {
		setup();
		PageLogin pageLogin = new PageLogin(driver);
		pageLogin.login("jose", "123");
		PageCadastrarLivro pageLivro = new PageCadastrarLivro(driver);
		pageLivro.cadastrar("5555", "Sommerville", "Engenharia");
		assertEquals("Lista de livros", pageLivro.getResultadoCadastroComSucesso());
		driver.quit();
	}

}
