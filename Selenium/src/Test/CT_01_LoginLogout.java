package Test;

import Util.DataTest;
import Util.FrameworkWeb;
import Helpers.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CT_01_LoginLogout {

	@Before
	public void abreSistema() {

		FrameworkWeb.AbreJanelaNavegador();
		FrameworkWeb.AcessaUrl("");
		
		System.out.println("Metodo de inicio do teste finalizado.");

	}

	@Test
	public void login() {

		LoginLogoutHelper.login(DataTest.usuarioAdmin, DataTest.senhaAdmin);
		
		System.out.println("Metodo de login do sistema finalizado.");

	}
	
	@After
	public void fechaSistema() {
		
		FrameworkWeb.FechaJanela();
		
		System.out.println("Metodo de final do teste finalizado.");
		
	}

}
