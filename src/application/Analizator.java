package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.TreeMap;
import java.util.Map.Entry;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Analizator extends Application {
	@SuppressWarnings("unchecked")
	@Override
    public void start(Stage primaryStage) {
		FileChooser plik = new FileChooser();
		plik.getExtensionFilters().add(new FileChooser.ExtensionFilter("Pliki tekstowe", "*.txt"));
		VBox rozmieszczeniePionowo = new VBox(10);
		rozmieszczeniePionowo.setPadding(new Insets(10));
		Button wczytajPlik = new Button("Wczytaj plik");
		rozmieszczeniePionowo.getChildren().add(wczytajPlik);
		Label tekstSurowyLabel = new Label("Tekst przed obróbk¹:");
		TextArea tekstSurowyText = new TextArea();
		tekstSurowyText.setWrapText(true);
		rozmieszczeniePionowo.getChildren().addAll(tekstSurowyLabel, tekstSurowyText);
		wczytajPlik.setOnAction(event -> {
			File plikWybrany = plik.showOpenDialog(primaryStage);
			try {
				BufferedReader czytaj = new BufferedReader(new FileReader(plikWybrany));
				String tekst = czytaj.readLine();
				tekstSurowyText.appendText(tekst);
				tekst = czytaj.readLine();
				while (tekst != null) {
					tekstSurowyText.appendText("\n" + tekst);
					tekst = czytaj.readLine();
				}
				czytaj.close();
			} catch (IOException e) {
				System.out.println("Brak pliku!");
			}
		});
		HBox zamianaZnakow = new HBox(10);
		Label zamienCoLabel = new Label("Zamieñ tekst:");
		TextField zamienCoText = new TextField();
		zamienCoText.setMinWidth(235);
		Label zamienNaLabel = new Label("Na:");
		TextField zamienNaText = new TextField();
		zamienNaText.setMinWidth(235);
		Button zamienPrzycisk = new Button("Zamieñ");
		Button cofnijZamiane = new Button("Cofnij");
		zamianaZnakow.getChildren().addAll(zamienCoLabel, zamienCoText, zamienNaLabel, zamienNaText, zamienPrzycisk, cofnijZamiane);
		rozmieszczeniePionowo.getChildren().add(zamianaZnakow);
		Label tekstPoObrobceLabel = new Label("Tekst po obróbce:");
		TextArea tekstPoObrobceText = new TextArea();
		TextArea tekstTymczasowy = new TextArea();
		tekstPoObrobceText.setEditable(false);
		tekstPoObrobceText.setWrapText(true);
		rozmieszczeniePionowo.getChildren().addAll(tekstPoObrobceLabel, tekstPoObrobceText);
		zamienPrzycisk.setOnAction(event -> {
			tekstTymczasowy.setText(CofniecieZamiany.cofnijZamiane(tekstPoObrobceText.getText()));
			if (tekstPoObrobceText.getText().equals("")) {
				tekstPoObrobceText.setText(ZamianaTekstu.zamienTekst(tekstSurowyText.getText(), zamienCoText.getText(), zamienNaText.getText()));
			} else {
				tekstPoObrobceText.setText(ZamianaTekstu.zamienTekst(tekstPoObrobceText.getText(), zamienCoText.getText(), zamienNaText.getText()));
			}
		});
		cofnijZamiane.setOnAction(event -> tekstPoObrobceText.setText(tekstTymczasowy.getText()));
		HBox przyciskiDolne = new HBox(815);
		Button analizaCzestosciPrzycisk = new Button("Analiza czêstoœci");
		analizaCzestosciPrzycisk.setOnAction(event -> {
			TreeMap<String, Integer> dane = new TreeMap<>(ZliczanieSlow.zliczSlowa(tekstPoObrobceText.getText()));
			CategoryAxis osX = new CategoryAxis();
			osX.setLabel("Max. dziesiêæ najczêstszych s³ów");
			NumberAxis osY = new NumberAxis();
			osY.setLabel("Czêstoœæ wystêpowania (liczba)");
			BarChart<String, Number> wykres = new BarChart<>(osX, osY);
			XYChart.Series<String, Number> dataSeries = new XYChart.Series<>();
			for (Entry<String, Integer> entry : dane.entrySet()) {
		        String slowo = entry.getKey();
		        Number liczba = entry.getValue();
		        XYChart.Data<String, Number> wynik = new XYChart.Data<>(slowo, liczba);
		        System.out.println(wynik);
		        dataSeries.getData().add(wynik);
		    }
			wykres.getData().addAll(dataSeries);
			wykres.setLegendVisible(false);
			VBox vbox = new VBox(wykres);
			StackPane secondaryLayout = new StackPane();
            secondaryLayout.getChildren().add(vbox);
            Scene secondScene = new Scene(secondaryLayout, 800, 400);
            Stage newWindow = new Stage();
            newWindow.setTitle("Wizualizacja");
            newWindow.setScene(secondScene);
            newWindow.show();
		});
		Button zamknijPrzycisk = new Button("Zamknij");
		zamknijPrzycisk.setOnAction(event -> Platform.exit());
		przyciskiDolne.getChildren().addAll(analizaCzestosciPrzycisk, zamknijPrzycisk);
		rozmieszczeniePionowo.getChildren().add(przyciskiDolne);
		
		Scene scene = new Scene(rozmieszczeniePionowo, 1000, 500);
		primaryStage.setTitle("Analiza Tekstu");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	public static void main(String[] args) {
        launch(args);
    }
}