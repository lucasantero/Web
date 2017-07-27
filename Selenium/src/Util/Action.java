package Util;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;

import Util.Logs;

public class Action extends FrameworkWeb {

	// Metodo para realiza click em qualquer elemento do sistema.
	public static void ClicaElemento(By localizador) {
		WebElement element = null;

		Actions actions = null;

		AguardaFinalizacaoChamadasAjax();

		try {
			element = GetElemento(localizador);

			AguardaElementoVisivel(localizador);

			actions = new Actions(GetDriver());

			actions.moveToElement(element);

			actions.perform();

			AguardaElementoClicavel(localizador);

			element.click();

			AguardaFinalizacaoChamadasAjax();

		} catch (Exception excecao) {
			if (excecao instanceof StaleElementReferenceException) {

				ClicaElemento(localizador);

			} else {

				Logs.LogaErro(localizador.toString(), excecao);

			}
		}

	}

	// Digita um texto em um campo.
	public static void DigitaCampo(By localizador, String texto) {

		WebElement field = GetElemento(localizador);

		try {

			AguardaFinalizacaoChamadasAjax();

			AguardaElementoVisivel(localizador);

			// os campos para escolher arquivos não podem ser limpados
			if ((!localizador.toString().contains("file"))) {
				field.clear();

				Thread.sleep(250);
			}

			field.sendKeys(texto);

			AguardaFinalizacaoChamadasAjax();

		} catch (Exception excecao) {

			Logs.LogaErro(localizador.toString(), excecao);

		}

	}

	// Seleciona um elemento em uma lista estática.
	public static void SelecionaElementoDeUmaListaEstatica(By locator, String element) {
		
		AguardaFinalizacaoChamadasAjax();

		AguardaElementoVisivel(locator);

		new Select(GetDriver().findElement(locator)).selectByVisibleText(element);
	}

	// Verifica se um elemento está presente na tela atual.
	public static boolean VerificaElementoPresente(By localizador) {
		try {
			
			GetDriver().findElement(localizador);

			return true;
			
		} catch (ElementNotFoundException ex) {
			
			return false;
			
		}
	}

    // Apaga todos os caracteres em um campo texto.
    public static void LimpaCampoTexto(By localizador)
    {
        try
        {
            WebElement element = GetElemento(localizador);

            element.clear();
        }
        catch (Exception ex)
        {
            
        	Logs.LogaErro(localizador.toString(), ex);
        
        }
    }

}