package it.polito.tdp.meteo;

import java.util.*;
import java.util.List;

import it.polito.tdp.meteo.bean.Citta;
import it.polito.tdp.meteo.bean.SimpleCity;
import it.polito.tdp.meteo.db.MeteoDAO;

public class Model {

	private final static int COST = 50;
	private final static int NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN = 3;
	private final static int NUMERO_GIORNI_CITTA_MAX = 6;
	private final static int NUMERO_GIORNI_TOTALI = 15;
    List<Citta> listaCitta=new ArrayList<Citta>();
	public Model() {
    
	}

	public String getUmiditaMedia(int mese) {
        String result="";
        MeteoDAO dao=new MeteoDAO();
        for(Citta localita: dao.getCitta())
          result+= "Localita:  "+localita+"  Umidita Media: "+dao.getAvgRilevamentiLocalitaMese(mese, localita.getNome())+"\n";
       return result;
	}

	public String trovaSequenza(int mese) {
       String result="";
       MeteoDAO dao=new MeteoDAO();
       listaCitta=dao.getCitta();
       for(Citta citta:listaCitta){
    	   citta.setRilevamenti(dao.getAllRilevamentiLocalitaMese(mese,citta.getNome()));
    	   citta.setCounter(0);
       }
       List<SimpleCity> parziale=new ArrayList<SimpleCity>();
       List<SimpleCity> soluzioneCandidata=new ArrayList<SimpleCity>(); 
       
       int livello=0;
       recursive(parziale,livello,soluzioneCandidata);
       
       for(SimpleCity sc:soluzioneCandidata)
    	   result+=sc.getNome()+" \n";
       return result;
	}

	private Double costo(List<SimpleCity> soluzioneCandidata) {

		double score = 0.0;
		if (soluzioneCandidata == null || soluzioneCandidata.size() == 0)
			return Double.MAX_VALUE;

		if(soluzioneCandidata.size()!=0){
		SimpleCity citta=soluzioneCandidata.get(0);
		for(SimpleCity s:soluzioneCandidata){
		if(!s.equals(citta))
		  {
			score+=(double)(COST*s.getCosto()+100);
		  }
		else
		  {
		 	score+=(double)(COST*s.getCosto());
		  }
		 citta=s;
	   }
    }
		
		return score;
	}

	private boolean controllaParziale(List<SimpleCity> parziale) {
		if (parziale == null)
			return false;

		// Se la soluzione parziale e' vuota, e' valida
		if (parziale.size() == 0)
			return true;

		// Controllo sui vincoli del numero di giorni massimo in ciascuna citta
		for (Citta citta : listaCitta) {
			if (citta.getCounter() > NUMERO_GIORNI_CITTA_MAX)
				return false;
		}

		
			SimpleCity citta=parziale.get(0);
			int count =0;
			for(SimpleCity s:parziale){
				if(citta.equals(s))
					count++;
				else{
					if(count>this.NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN)
					{
							citta=s;
							count=1;
				    }
					else
						return false;
				 }
			
			
		}
			return true;
	}

	

	
	

	private void recursive(List<SimpleCity> parziale,int livello,List<SimpleCity> soluzioneCandidata){
		//condizione di terminazione
		
		if(parziale.size()==NUMERO_GIORNI_TOTALI){
			if(costo(parziale)<costo(soluzioneCandidata)  ){
			    soluzioneCandidata.clear();
				soluzioneCandidata.addAll(parziale);
				
			}
		}
		
		for(Citta citta:listaCitta){
			
			SimpleCity c=new SimpleCity(citta.getNome());
			
			  
				        c.setCosto(citta.getRilevamenti().get(livello).getUmidita());
			    		parziale.add(c);
			    		citta.increaseCounter();
			    		
			    		if( this.controllaParziale(parziale))
			    	    recursive(parziale,livello+1,soluzioneCandidata);
			    		
			    	    parziale.remove(livello);
			    	    citta.decreaseCounter();
			    		
			 }
	     

     }
}
