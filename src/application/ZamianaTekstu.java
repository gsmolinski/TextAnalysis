package application;

public class ZamianaTekstu {
	public static String zamienTekst (String doZamiany, String zamienCo, String zamienNaCo) {
		String wynik = doZamiany.replaceAll(zamienCo, zamienNaCo);
		return wynik;
	}

}