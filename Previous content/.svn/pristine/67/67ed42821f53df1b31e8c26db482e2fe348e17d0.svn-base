package br.gov.ufpe.mb;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;

import br.gov.ufpe.domain.Result;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;

@ManagedBean(name = "clienteMB", eager = true)
public class ClienteMB {

	public String message;
	 
	public List<Result> resultados; 
 
	public ClienteMB() {
		message = "Insert your query here."; 
	} 

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
 
	public List<Result> getResultados() {
		return resultados; 
	} 

	public void setResultados(List<Result> resultados) {
		this.resultados = resultados;
	}

	public void executeQuery() {   

		Query query = QueryFactory.create(message);
		QueryExecution qe = QueryExecutionFactory.create(query);
		resultados = new ArrayList<Result>();   
 
		try { 
			ResultSet rs = qe.execSelect(); 
 
			while (rs.hasNext()) {  
				 
				QuerySolution row = rs.next();     
				
				Result result = new Result(); 
				if(row.getResource("notification") != null) { 
					result.setNotification(row.getResource("notification").toString().replaceAll("ntdo:HealthSurveillanceNotificationAction@", ""));
				}
				if(row.getResource("patient") != null) { 
				result.setPatient(row.getResource("patient").toString().replaceAll("http://www.cin.ufpe.br/~ntdo/OWLFiles/ntdo.owl#", ""));
				}
				if(row.getResource("disease") != null) {
				result.setDisease(row.getResource("disease").toString().replaceAll("ntdo:Tuberculosis@", ""));
				}
				if(row.getResource("causeOfDeath") != null) {
				result.setCauseOfDeath(row.getResource("causeOfDeath").toString().replaceAll("biotop:PathologicalDisposition@", ""));
				}
				resultados.add(result);  
			}
		} finally { 
			qe.close(); 
		}
		
		System.out.println("\nall done.");
	}

}
 