package Util;

import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class FrameworkWeb {

	// Objeto que possibilita a interação com a janela do navegador.
	protected static WebDriver driver = GetDriver();

	// Flag para verificar se é necessário criar outra instância de Driver.
	protected static boolean fechouJanela = true;

	// Objeto wait utilizado para aguardar que determinadas condições aconteçam
	// antes de os testes prosseguirem.
	protected static WebDriverWait wait = null;

	// URL do servidor no qual será rodado os testes.
	private static String urlBase = "http://localhost:5000/";

	// Retorna uma instância de driver para interação com o navegador.
	// Cria uma nova instância do driver caso a instância anterior tenha sido utilizada.
	public static WebDriver GetDriver() {

		if (fechouJanela) {

			String exePath = "C:\\eclipse\\bibliotecas\\chromedriver.exe";
			System.setProperty("webdriver.chrome.driver", exePath);

			WebDriver novaInstanciaDriver = new ChromeDriver();

			fechouJanela = false;

			return novaInstanciaDriver;

		}

		return driver;
	}

	// Acessa uma URL da URL base.
	public static void AcessaUrl(String url) {
		String urlDeAcesso = urlBase + url;

		driver.navigate().to(urlDeAcesso);
	}

	// Cria uma janela do navegador.
	public static void AbreJanelaNavegador() {
		try {
			
			driver = GetDriver();

			GetDriver().manage().window().maximize();

		} catch (IllegalStateException ex) {
			FechaJanela();

			try {
				Thread.sleep(5000);
			} catch (InterruptedException ex2) {

				ex2.printStackTrace();
			}

			AbreJanelaNavegador();

		}
	}

	// Fecha a janela do navegador que foi aberta.
	public static void FechaJanela() {
		GetDriver().quit();

		fechouJanela = true;
	}

	// Aguarda até que todas as requisições Ajax sejam concluídas.
	public static boolean AguardaFinalizacaoChamadasAjax() {

		WebDriverWait wait = new WebDriverWait(GetDriver(), 30);

		// Espera até JQuery ser carregado.
		ExpectedCondition<Boolean> jQueryLoad = new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply(WebDriver driver) {
				try {
					return ((Long) ((JavascriptExecutor) GetDriver()).executeScript("return jQuery.active") == 0);
				} catch (Exception e) {
					// jQuery não presente.
					return true;
				}
			}
		};

		// Espera até Javascript ser carregado.
		ExpectedCondition<Boolean> jsLoad = new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply(WebDriver driver) {
				return ((JavascriptExecutor) GetDriver()).executeScript("return document.readyState").toString()
						.equals("complete");
			}
		};

		return wait.until(jQueryLoad) && wait.until(jsLoad);
	}

	// Retorna o elemento encontrado pelo localizador informado.
	public static WebElement GetElemento(By localizador) {

		WebElement elemento = null;

		AguardaFinalizacaoChamadasAjax();

		try {
			GetWaitObject().until(ExpectedConditions.presenceOfElementLocated(localizador));

			elemento = GetDriver().findElement(localizador);
		} catch (Exception excecao) {

			Logs.LogaErro(localizador.toString(), excecao);
		}

		return elemento;

	}

	// Retorna uma instância de wait para definir tempos e condições de espera.
	public static WebDriverWait GetWaitObject() {
		wait = new WebDriverWait(GetDriver(), 50);

		wait.pollingEvery(500, TimeUnit.MILLISECONDS);

		return wait;
	}

	// Aguarda até que um elemento fique visível na tela.
	public static void AguardaElementoVisivel(By localizador) {

		try {
			GetWaitObject().until(ExpectedConditions.visibilityOfElementLocated(localizador));
		} catch (Exception ex) {
			throw ex;
		}

	}

	// Aguarda até que um elemento possa ser clicado.
	public static void AguardaElementoClicavel(By localizador) {

		try {
			GetWaitObject().until(ExpectedConditions.elementToBeClickable(localizador));
		} catch (Exception ex) {
			throw ex;
		}

	}

	// Atualiza pagina.
    public static void AtualizaPagina()
    {
        GetDriver().navigate().refresh();
    }
    
    //Muda resolução da tela
    public static void MudaResolucaoJanelaNavegador(int largura, int altura)
    {
        driver.manage().window().setSize(new Dimension(largura, altura));
    }

}
