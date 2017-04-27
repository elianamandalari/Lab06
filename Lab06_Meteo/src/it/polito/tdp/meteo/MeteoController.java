package it.polito.tdp.meteo;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;

public class MeteoController {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private ChoiceBox<Integer> boxMese;

	@FXML
	private Button btnCalcola;

	@FXML
	private Button btnUmidita;

	@FXML
	private TextArea txtResult;
	
    Model model;
    List<Integer> listaMesi=new ArrayList<Integer>();
    
	@FXML
	void doCalcolaSequenza(ActionEvent event) {
		txtResult.appendText("Mese "+boxMese.getValue()+"\n"+"Sequenza città nei primi 15 gg del mese : \n"+model.trovaSequenza(boxMese.getValue()));

	}

	@FXML
	void doCalcolaUmidita(ActionEvent event) {
		
		txtResult.appendText("Mese  "+ boxMese.getValue()+"\n"+model.getUmiditaMedia(boxMese.getValue()));

	}

	@FXML
	void initialize() {
		assert boxMese != null : "fx:id=\"boxMese\" was not injected: check your FXML file 'Meteo.fxml'.";
		assert btnCalcola != null : "fx:id=\"btnCalcola\" was not injected: check your FXML file 'Meteo.fxml'.";
		assert btnUmidita != null : "fx:id=\"btnUmidita\" was not injected: check your FXML file 'Meteo.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Meteo.fxml'.";
	    for(int i=1;i<=12;i++)
	    	listaMesi.add(i);
        boxMese.getItems().addAll(listaMesi);
  	}

	public void setModel(Model model) {
		this.model=model;
		
	}

}
