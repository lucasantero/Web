package Helpers;

import Util.Action;
import Util.UIMap;

public class LoginLogoutHelper extends Action {

	// Faz login usando o usuário e senha passados por parametros.
	public static void login(String usuario, String senha)
    {
		Action.DigitaCampo(UIMap.LoginScreen.textField_Usuario, usuario);

		Action.DigitaCampo(UIMap.LoginScreen.textField_Senha, senha);

		Action.ClicaElemento(UIMap.LoginScreen.button_Entrar);
    }

}
