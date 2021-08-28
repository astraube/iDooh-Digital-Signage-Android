package br.com.i9algo.taxiadv.v2.views;

import androidx.fragment.app.DialogFragment;

public class BaseFragment extends DialogFragment {

	// Dados da model que esta sendo exibida na tela
	private String mName = null;

	// Path do icone que vai exibir na fragment
	private String mIconPath = null;

	public BaseFragment() {
		setName(getTag());
	}


	// Util para enviar relatorios para Google Analytics
	// e identificar qual fragment esta ativa
	public String getName() { return this.mName; }
	public void setName(String value) { this.mName = value; }

	public String getIconPath() { return this.mIconPath; }
	public void setIconPath(String value) { this.mIconPath = value; }
    
	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}
}
