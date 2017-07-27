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

	// Objeto que possibilita a intera��o com a janela do navegador.
	protected static WebDriver driver = GetDriver();

	// Flag para verificar se � necess�rio criar outra inst�ncia de Driver.
	protected static boolean fechouJanela = true;

	// Objeto wait utilizado para aguardar que determinadas condi��es aconte�am
	// antes de os testes prosseguirem.
	protected static WebDriverWait wait = null;

	// URL do servidor no qual ser� rodado os testes.
	private static String urlBase = "http://localhost:5000/";

	// Retorna uma inst�ncia de driver para intera��o com o navegador.
	// Cria uma nova inst�ncia do driver caso a inst�ncia anterior tenha sido utilizada.
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

	// Aguarda at� que todas as requisi��es Ajax sejam conclu�das.
	public static boolean AguardaFinalizacaoChamadasAjax() {

		WebDriverWait wait = new WebDriverWait(GetDriver(), 30);

		// Espera at� JQuery ser carregado.
		ExpectedCondition<Boolean> jQueryLoad = new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply(WebDriver driver) {
				try {
					return ((Long) ((JavascriptExecutor) GetDriver()).executeScript("return jQuery.active") == 0);
				} catch (Exception e) {
					// jQuery n�o presente.
					return true;
				}
			}
		};

		// Espera at� Javascript ser carregado.
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

	// Retorna uma inst�ncia de wait para definir tempos e condi��es de espera.
	public static WebDriverWait GetWaitObject() {
		wait = new WebDriverWait(GetDriver(), 50);

		wait.pollingEvery(500, TimeUnit.MILLISECONDS);

		return wait;
	}

	// Aguarda at� que um elemento fique vis�vel na tela.
	public static void AguardaElementoVisivel(By localizador) {

		try {
			GetWaitObject().until(ExpectedConditions.visibilityOfElementLocated(localizador));
		} catch (Exception ex) {
			throw ex;
		}

	}

	// Aguarda at� que um elemento possa ser clicado.
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
    
    //Muda resolu��o da tela
    public static void MudaResolucaoJanelaNavegador(int largura, int altura)
    {
        driver.manage().window().setSize(new Dimension(largura, altura));
    }

}
